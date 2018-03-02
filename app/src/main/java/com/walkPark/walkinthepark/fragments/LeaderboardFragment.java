package com.walkPark.walkinthepark.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.walkinthepark.R;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.adapters.GameListAdapter;
import com.walkPark.walkinthepark.models.Route;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by singy on 3/2/2018.
 */

public class LeaderboardFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.name) TextView tvTopName;
    @BindView(R.id.rank) TextView tvTopRank;


    CatLoadingView mView;

    private List<Route> routeList = new ArrayList<>();
    private final String TAG = getClass().getName();
    private Unbinder unbinder;

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
