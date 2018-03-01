package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.models.CheckPoint;
import com.walkPark.walkinthepark.models.Route;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Boon Sing on 19-Feb-18.
 */

public class GameDetailsActivity extends BaseActivity {
    @BindView(R.id.buttonLeft) ImageView buttonLeft;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.gameImage) ImageView gameImage;
    @BindView(R.id.startButton) Button startButton;
    @BindView(R.id.info) TextView info;
    @BindView(R.id.steps) TextView steps;

    private Route route;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_details);

        ButterKnife.bind(this);

        route = Parcels.unwrap(getIntent().getParcelableExtra(Constants.INTENT_GAME_SELECTED));

        initUI();

    }

    private void initUI() {
        Glide.with(this)
                .load(route.getImage_url())
                .into(gameImage);

        textTitle.setText(route.getName());
        info.setText(route.getDescription());
        steps.setText(route.getEstimated_steps());

    }

    @OnClick(R.id.buttonLeft)
    public void backButton () {
        finish();
    }

    @OnClick(R.id.startButton)
    public void startButtonClick() {
        //TODO
        //Call API and return the list of checkpoints to next page
        List<CheckPoint> checkPoints = new ArrayList<>();
        Intent intent = new Intent(GameDetailsActivity.this,
                CheckpointActivity.class);
        intent.putExtra(Constants.INTENT_CHECKPOINTS_RETURN, Parcels.wrap(checkPoints));
        finish();
    }








}
