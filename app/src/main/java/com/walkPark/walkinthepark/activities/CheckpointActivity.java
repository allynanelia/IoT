package com.walkPark.walkinthepark.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
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
import com.walkPark.walkinthepark.events.CompleteCheckPointEvent;
import com.walkPark.walkinthepark.models.CheckPoint;
import com.walkPark.walkinthepark.models.Route;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Nullable;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private CheckPointAdapter adapter;
    private List<CheckPoint> checkPoints = new ArrayList<>();
    private Route route;

    private int workTime;
    private Handler workTimeHandler;

    public BeaconManager beaconManager;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    private ArrayList<Region> regions = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpoints);

        ButterKnife.bind(this);

        workTimeHandler = new Handler();


        regions.add(new Region("iot04", Identifier.parse("0x02696f74736d757367303407"),
                null, null ));
        regions.add(new Region("iot46", Identifier.parse("0x02696f74736d757367343607"),
                null, null));
        regions.add(new Region("iot48", Identifier.parse("0x02696f74736d757367343807"),
                null, null));

        route = Parcels.unwrap(getIntent()
                .getParcelableExtra(Constants.INTENT_CHECKPOINTS_RETURN));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);
        }

        initUI();

        loadData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    beaconManager = BeaconManager.getInstanceForApplication(this);
                    beaconManager.bind(this);
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
                    beaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
                    beaconManager.bind(this);

                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                }
            }
        }
    }

    private void initUI() {
        textTitle.setText(route.getName());
        textPoint.setText(route.getCheckpoints().get(0).getPoints());

        double counterDValue = calculateCheckPointsDone(route);
        Double d = new Double(counterDValue);
        int i = d.intValue();
        progressDuration.setProgress(i);

        adapter = new CheckPointAdapter(CheckpointActivity.this, route.getCheckpoints());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    private Runnable rCountdown = new Runnable() {
        @Override
        public void run() {
            final long minsLeft = workTime / 3600;
            final long secsLeft = workTime / 60;

            textMinutes.setText(String.format("%02d", minsLeft));
            textSeconds.setText(String.format("%02d", secsLeft));

            workTime++;
            workTimeHandler.postDelayed(rCountdown, 1);
        }
    };

    @Subscribe
    public void onEvent(CompleteCheckPointEvent event) {

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
                                + " and it's distance is " + beacon.getDistance() + " its size is > " + beacons.size());
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
}