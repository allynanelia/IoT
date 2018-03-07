package com.walkPark.walkinthepark.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Nullable;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nanelia on 27/2/18.
 */

public class CheckpointActivity extends BaseActivity {

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpoints);

        ButterKnife.bind(this);

        workTimeHandler = new Handler();

        route = Parcels.unwrap(getIntent()
                .getParcelableExtra(Constants.INTENT_CHECKPOINTS_RETURN));

        initUI();

        loadData();
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

}
