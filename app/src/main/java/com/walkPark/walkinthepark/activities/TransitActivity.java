package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.adapters.GameListAdapter;
import com.walkPark.walkinthepark.adapters.PlayerIDAdapter;
import com.walkPark.walkinthepark.events.GameTriggerEvent;
import com.walkPark.walkinthepark.events.SelectIDEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by singy on 3/20/2018.
 */

public class TransitActivity extends BaseActivity {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private PlayerIDAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transit);

        ButterKnife.bind(this);

        List<Integer> idList = new ArrayList<>();
        for(int i = 1; i<=30; i++) {
            idList.add(i);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlayerIDAdapter(this, idList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(SelectIDEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PlayerID", Parcels.wrap(Integer.toString(event.getPlayerID())));
        startActivity(intent);
    }
}
