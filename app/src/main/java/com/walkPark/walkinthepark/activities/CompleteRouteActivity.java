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

    @BindView(R.id.textSecs) TextView timeTaken;
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
        stepsTaken.setText(route.getTotal_steps());
        totalPoints.setText(route.getTotal_steps());

        if(route.getTotal_time_taken()!=null) {

            int time = route.getTotal_time_taken();
            final long hours = (long) time / 3600;
            final long mins = (long) time / 60;
            final long secs = (long) time % 60;

            timeTaken.setText(String.format("%02d", hours)+":"+String.format("%02d", mins)+":"+String.format("%02d", secs));
        } else {
            timeTaken.setText("00:00:00");
        }
    }
    @OnClick(R.id.buttonMenu)
    public void goHome(){
        startActivity(new Intent(CompleteRouteActivity.this,
                MainActivity.class));
        finish();
   }



}
