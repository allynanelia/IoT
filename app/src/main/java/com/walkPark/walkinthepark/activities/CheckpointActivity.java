package com.walkPark.walkinthepark.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.walkPark.walkinthepark.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.adapters.CheckPointAdapter;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.dialogs.CheckPointDialog;
import com.walkPark.walkinthepark.events.CompleteCheckPointEvent;
import com.walkPark.walkinthepark.events.GiveUpCheckPointEvent;
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

public class CheckpointActivity extends BaseActivity implements BeaconConsumer {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.textPoint) TextView textPoint;
    @BindView(R.id.progressDuration) ArcProgress progressDuration;
    @BindView(R.id.textMinutes) TextView textMinutes;
    @BindView(R.id.textSeconds) TextView textSeconds;
    @BindView(R.id.textSteps) TextView textSteps;
    @BindView(R.id.imageShake) ImageView imageShake;


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
    private SensorEventListener sensorEventListenerLA;
    private SensorEventListener sensorEventListenerSD;
    private Sensor detectorSensor;
    private Sensor linearASensor;
    private long steps = 0;
    private long initialSteps = 0;
    private long lastTime = 0;
    private double initalSpeed = 0.0;
    private final static int REQUEST_ENABLE_BT = 1;
    private float lastX = 0, lastY = 0, lastZ = 0;
    private ArrayList<Double> speeds = new ArrayList<>();

    private String currentCheckPointBeacon;

    private String checkPointEntry;

    private int position;
    BluetoothHeadset mBluetoothHeadset;

    private final String TAG = getClass().getName();

    private boolean reload;

    Runnable runnableWobbleSeven = new Runnable() {
        @Override
        public void run() {
            if (imageShake != null) {
                imageShake.animate()
                        .rotationBy(10f)
                        .setInterpolator(new CycleInterpolator(2))
                        .setDuration(2000L)
                        .setStartDelay(500L)
                        .withEndAction(runnableWobbleSeven)
                        .start();
            }
        }
    };

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
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

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
        loadSensors();

        if (reload) {
            adapter.setCheckPointList(route.getCheckpoints());
            adapter.notifyDataSetChanged();
        }
    }

    private void initUI() {
        workTimeHandler = new Handler();
        new Handler().post(runnableWobbleSeven);
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

    private void loadSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //linear accelerometer
        sensorEventListenerLA = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long currentTime = System.currentTimeMillis();
                if ((currentTime - lastTime) > 1000) {
                    long diffTime = (currentTime - lastTime) /1000;
                    lastTime = currentTime;
                    double speedX = initalSpeed + (x-lastX)*diffTime;
                    double speedY = initalSpeed + (y-lastY)*diffTime;
                    double speedZ = initalSpeed + (z-lastZ)*diffTime;

                    double speed = Math.sqrt(speedX*speedX+speedY*speedY+speedZ*speedZ);

                    speeds.add(speed);
                    String s = Double.toString(speed);
                      /*  Toast.makeText(getApplicationContext(), s,
                                Toast.LENGTH_SHORT).show(); */

                    initalSpeed = speed;
                    lastX = x;
                    lastY = y;
                    lastZ = z;

                }
            }

            @Override
            public void onAccuracyChanged (Sensor sensor,int accuracy){
            }

        };

        sensorManager.registerListener(sensorEventListenerLA, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);

        //step detector
        sensorEventListenerSD = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                int value = -1;

                if (values.length > 0) {
                    value = (int) values[0];
                }

                steps++;

                textSteps.setText((int) steps + "");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListenerSD, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR), SensorManager.SENSOR_DELAY_FASTEST);

    }

    private void loadData() {
        workTime = 0;
        if(!reload) {
            workTimeHandler.post(rCountdown);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
     /*   Toast.makeText(getApplicationContext(),"Speed: " + Double.toString(calculateAvgSpeed()),Toast.LENGTH_SHORT).show(); */
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

        loadSensors();
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
    public void onEvent(GiveUpCheckPointEvent event) {

        double speed = calculateAvgSpeed();

        CheckpointDetails cpd = new CheckpointDetails(route.get_id(),
                checkPointEntry
                ,Prefs.getUserProfile().getPlayer_id() , Long.toString(steps), Integer.toString(workTime),Double.toString(speed), true);

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
                        Intent intent = new Intent(CheckpointActivity.this, CompleteRouteActivity.class);
                        intent.putExtra(Constants.ROUTE_END, Parcels.wrap(route));
                        startActivity(intent);
                        finish();
                    } else {
                        reloadUI();
                        loadNewBeacon();
                        //loadData();
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

    @Subscribe
    public void onEvent(CompleteCheckPointEvent event) {

        double speed = calculateAvgSpeed();

        CheckpointDetails cpd = new CheckpointDetails(route.get_id(),
                checkPointEntry
                , Prefs.getUserProfile().getPlayer_id() , Long.toString(steps-initialSteps), Integer.toString(workTime), Double.toString(speed), false);

        initialSteps = steps-initialSteps;

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
                        //route ends
                        Intent intent = new Intent(CheckpointActivity.this, CompleteRouteActivity.class);
                        intent.putExtra(Constants.ROUTE_END, Parcels.wrap(route));
                        startActivity(intent);
                        finish();
                    } else {
                        reloadUI();
                        loadNewBeacon();
                        //loadData();
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
                                                    .getFound_description(), "YOU'VE FOUND IT!")
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
                                                    .getFound_description(), "YOU'VE FOUND IT!")
                                    .show(getSupportFragmentManager(),"dialog_checkpoint");
                            EventBus.getDefault().post(new GiveUpCheckPointEvent());
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

    public double calculateAvgSpeed() {
        double avgSpeed = 0;
        for(int i = 0; i < speeds.size(); i++) {
            avgSpeed += speeds.get(i);
        }

        avgSpeed = avgSpeed / speeds.size();
        speeds.clear();
        return avgSpeed;

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        sensorManager.unregisterListener(sensorEventListenerLA);
        sensorManager.unregisterListener(sensorEventListenerSD);


        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}