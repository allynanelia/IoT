package com.walkPark.walkinthepark.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinthepark.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.adapters.CheckPointAdapter;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.dialogs.CheckPointDialog;
import com.walkPark.walkinthepark.events.CompleteCheckPointEvent;
import com.walkPark.walkinthepark.models.CheckPoint;
import com.walkPark.walkinthepark.models.CheckpointDetails;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.RouteCheckPointResponse;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Nullable;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nanelia on 27/2/18.
 */

public class CheckpointActivity extends BaseActivity implements BeaconConsumer, SensorEventListener {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.textPoint) TextView textPoint;
    @BindView(R.id.progressDuration) ArcProgress progressDuration;
    @BindView(R.id.textMinutes) TextView textMinutes;
    @BindView(R.id.textSeconds) TextView textSeconds;
    @BindView(R.id.textSteps) TextView textSteps;

    private CheckPointAdapter adapter;
    private List<CheckPoint> checkPoints = new ArrayList<>();
    private Route route;

    private int workTime;
    private Handler workTimeHandler;

    public BeaconManager beaconManager;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 456;
    private ArrayList<Region> regions = new ArrayList<>();

    private Region region;
    private SensorManager sensorManager;
    private Sensor sSensor;
    private long steps = 0;

    private String currentCheckPointBeacon;

    private String checkPointEntry;

    private int position;

    private final String TAG = getClass().getName();

    private boolean reload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpoints);

        ButterKnife.bind(this);

        regions.add(new Region("iot04", Identifier.parse("0x02696f74736d757367303407"),
                        null, null )); //Working
        regions.add(new Region("iot45", Identifier.parse("696F74736D7534352020"),
                null, null)); //Testing
        regions.add(new Region("iot46", Identifier.parse("696F74736D7573673436"),
                null, null)); //Working
        regions.add(new Region("iot48", Identifier.parse("696F74736D7573673438"),
                null, null)); //Testing
        regions.add(new Region("iot49", Identifier.parse("0x02696f74736d757367343907"),
                null, null)); //Working

        route = Parcels.unwrap(getIntent()
                .getParcelableExtra(Constants.INTENT_CHECKPOINTS_RETURN));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_FINE_LOCATION);
        }

        initUI();
        loadNewBeacon();
        loadData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    beaconManager = BeaconManager.getInstanceForApplication(this);
                    beaconManager.bind(this);
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
                    beaconManager.bind(this);
                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                }
            }
        }
    }

    private void retrieveCurrentCheckPointBeacon() {
        for (int i = 0;  i < route.getCheckpoints().size(); i++) {
            CheckPoint cp = route.getCheckpoints().get(i);
            if (cp.getStatus().equals("1")) {
                currentCheckPointBeacon = cp.getBeacon_instance_id();
                checkPointEntry = cp.getId();
                position = i;
                break;
            }
        }
    }

    private void reloadUI() {
        CheckpointActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workTimeHandler = new Handler();
                textTitle.setText(route.getName());
                textPoint.setText(route.getCheckpoints().get(0).getPoints());
                textSteps.setText((int) steps + "");

                double counterDValue = calculateCheckPointsDone(route);
                Double d = new Double(counterDValue);
                int i = d.intValue();
                progressDuration.setProgress(i);

                adapter = new CheckPointAdapter(CheckpointActivity.this, route.getCheckpoints());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CheckpointActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void loadNewBeacon() {
        retrieveCurrentCheckPointBeacon();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, sSensor, SensorManager.SENSOR_DELAY_FASTEST);

        if (reload) {
            adapter.setCheckPointList(route.getCheckpoints());
            adapter.notifyDataSetChanged();
        }
    }

    private void initUI() {
        workTimeHandler = new Handler();
        textTitle.setText(route.getName());
        textPoint.setText(route.getCheckpoints().get(0).getPoints());
        textSteps.setText((int) steps + "");

        double counterDValue = calculateCheckPointsDone(route);
        Double d = new Double(counterDValue);
        int i = d.intValue();
        progressDuration.setProgress(i);

        adapter = new CheckPointAdapter(CheckpointActivity.this, route.getCheckpoints());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //Later do update if boolean true
    }

    private void loadData() {
        workTime = 0;
        workTimeHandler.post(rCountdown);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // Detect the main Eddystone-UID frame:
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=beac,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    private Runnable rCountdown = new Runnable() {
        @Override
        public void run() {
            final long minsLeft = workTime / 60;
            final long secsLeft = workTime % 60;

            textMinutes.setText(String.format("%02d", minsLeft));
            textSeconds.setText(String.format("%02d", secsLeft));

            workTime++;
            workTimeHandler.postDelayed(rCountdown, 1 * 1000);
        }
    };

    @Subscribe
    public void onEvent(CompleteCheckPointEvent event) {
        CheckpointDetails cpd = new CheckpointDetails(route.get_id(),
                checkPointEntry
                ,"1" , Long.toString(steps), Integer.toString(workTime));
        final RouteInterface routeInterface = WalkInTheParkRetrofit
               .getInstance()
               .create(RouteInterface.class);

       Call<RouteCheckPointResponse> call = routeInterface
               .checkpointComplete(cpd);
       call.enqueue(new Callback<RouteCheckPointResponse>() {
           @Override
           public void onResponse(Call<RouteCheckPointResponse> call, Response<RouteCheckPointResponse> response) {
                if (response.isSuccessful()) {
                    reload = true;
                    route = response.body().getRoute();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        startActivity(new Intent(CheckpointActivity.this,
                                MainActivity.class));
                        finish();
                    } else {
                        reloadUI();
                        loadNewBeacon();
                        loadData();
                        recallRangeNotifier();
                    }
                } else {
                    Toast.makeText(CheckpointActivity.this, "Error loading",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, response.errorBody().toString());
                }
           }

           @Override
           public void onFailure(Call<RouteCheckPointResponse> call, Throwable t) {
               Toast.makeText(CheckpointActivity.this, "Error loading API",
                       Toast.LENGTH_SHORT).show();
               t.printStackTrace();
           }
       });
    }

    public void recallRangeNotifier() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        String beaconID = beacon.getId1().toString();
                        Log.i("Success beacon", "ID = " + beaconID
                                + " and i found a beacon >" + currentCheckPointBeacon + "<");
                        if (beaconID.equals(currentCheckPointBeacon) && beacon.getDistance() < 3.0) {
                            Log.i("Came in here", "Hello");
                            //Currently set at 3m
                            CheckPointDialog
                                    .newInstance(route.getCheckpoints()
                                                    .get(position).getImage_url_found()
                                            , route.getCheckpoints().get(position)
                                                    .getFound_description())
                                    .show(getSupportFragmentManager(),"dialog_checkpoint");
                            EventBus.getDefault().post(new CompleteCheckPointEvent());
                            beaconManager.removeRangeNotifier(this);
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.buttonLeft)
    public void backButton () {
        finish();
    }

    private double calculateCheckPointsDone(Route route) {
        List<CheckPoint> checkPoints = route.getCheckpoints();
        double counter = 0.0;
        for (CheckPoint p : checkPoints) {
            if (p.getStatus().equals("2")) {
                counter += 1;
            }
        }
        return counter / checkPoints.size() * 100;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        String beaconID = beacon.getId1().toString();
                        Log.i("Success beacon", "ID = " + beaconID
                                + " and i found a beacon >" + currentCheckPointBeacon + "<");
                        if (beaconID.equals(currentCheckPointBeacon) && beacon.getDistance() < 3.0) {
                            //Currently set at 3m
                            Log.i("Came in here", "Hello");
                            CheckPointDialog
                                    .newInstance(route.getCheckpoints()
                                                    .get(position).getImage_url_found()
                                    , route.getCheckpoints().get(position)
                                                    .getFound_description())
                                    .show(getSupportFragmentManager(),"dialog_checkpoint");
                            EventBus.getDefault().post(new CompleteCheckPointEvent());
                            beaconManager.removeRangeNotifier(this);
                        }
                    }
                }
            }
        });

        try {
            for (Region region : regions) {
                beaconManager.startRangingBeaconsInRegion(region);
                beaconManager.startMonitoringBeaconsInRegion(region);
            }
        } catch (RemoteException e) {   }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }

        textSteps.setText((int) steps + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        sensorManager.unregisterListener(this, sSensor);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}