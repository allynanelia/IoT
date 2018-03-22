package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;

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

    @BindView(R.id.viewKonfetti) KonfettiView viewKonfetti;
    //@BindView(R.id.backToMainPage) Button backToMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_route);

        ButterKnife.bind(this);

        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);


    }

//    @OnClick(R.id.backToMainPage)
//    public void goHome(){
//        startActivity(new Intent(CompleteRouteActivity.this,
//                MainActivity.class));
//        finish();
//    }



}
