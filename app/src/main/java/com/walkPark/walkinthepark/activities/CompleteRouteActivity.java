package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.models.Route;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * Created by singy on 3/22/2018.
 */

public class CompleteRouteActivity extends BaseActivity {

    @BindView(R.id.textHours) TextView timeHours;
    @BindView(R.id.textMins) TextView timeMins;
    @BindView(R.id.textSecs) TextView timeSecs;
    @BindView(R.id.textTotalSteps) TextView stepsTaken;
    @BindView(R.id.textTotalPoints) TextView totalPoints;
    @BindView(R.id.buttonMenu) Button backToMain;

    private Route route;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_route);

        ButterKnife.bind(this);

        route = Parcels.unwrap(getIntent()
                .getParcelableExtra(Constants.ROUTE_END));

        initUI();
    }

    public void initUI() {
        stepsTaken.setText(Integer.toString(route.getTotal_steps()));
        totalPoints.setText(Integer.toString(route.getTotal_steps()));

        if(route.getTotal_time_taken()!=null) {

            int time = route.getTotal_time_taken();
            final long hours = (long) time / 3600;
            final long mins = (long) time / 60;
            final long secs = (long) time % 60;

            timeHours.setText(String.format("%02d", hours));
            timeMins.setText(String.format("%02d", mins));
            timeSecs.setText(String.format("%02d", secs));
        } else {
            timeHours.setText("00");
            timeMins.setText("00");
            timeSecs.setText("00");
        }
    }
    @OnClick(R.id.buttonMenu)
    public void goHome(){
        startActivity(new Intent(CompleteRouteActivity.this,
                MainActivity.class));
        finish();
   }



}
