package com.walkPark.walkinthepark.activities;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.models.Game;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Boon Sing on 19-Feb-18.
 */

public class GameDetailsActivity extends AppCompatActivity {
    @BindView(R.id.buttonLeft) ImageView buttonLeft;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.gameImage) ImageView gameImage;
    @BindView(R.id.startButton) Button startButton;
    @BindView(R.id.info) TextView info;
    @BindView(R.id.steps) TextView steps;
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_details);

        ButterKnife.bind(this);

        game = Parcels.unwrap(getIntent().getParcelableExtra(Constants.INTENT_GAME_SELECTED));

        initUI();

    }

    private void initUI() {
        Glide.with(this)
                .load(game.getGame_image_url())
                .into(gameImage);

        textTitle.setText(game.getGame_name());
        info.setText(game.getGame_description());
        steps.setText(game.getGame_est_steps());

    }

    @OnClick(R.id.buttonLeft)
    public void backButton () {
        finish();
    }

    @OnClick(R.id.startButton)
    public void startButtonClick() {
        //to do
    }








}
