package com.walkPark.walkinthepark.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.adapters.CheckPointAdapter;
import com.walkPark.walkinthepark.events.CompleteCheckPointEvent;
import com.walkPark.walkinthepark.models.CheckPoint;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Nullable;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nanelia on 27/2/18.
 */

public class CheckpointActivity extends BaseActivity {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private CheckPointAdapter adapter;
    private List<CheckPoint> checkPoints = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpoints);

        ButterKnife.bind(this);

        checkPoints = Parcels.unwrap(getIntent()
                .getParcelableExtra(Constants.INTENT_CHECKPOINTS_RETURN));

        initUI();
    }

    private void initUI() {
        adapter = new CheckPointAdapter(CheckpointActivity.this, checkPoints);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onEvent(CompleteCheckPointEvent event) {

    }

}
