package com.walkPark.walkinthepark.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.adapters.GameListAdapter;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.RouteResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    //@BindView(R.id.progressBar) ProgressBar progressBar;

    CatLoadingView mView;

    private List<Route> routeList;
    private final String TAG = getClass().getName();
    private GameListAdapter adapter;
    private Unbinder unbinder;
    private static String userID;

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        userID = args.getString("userID");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        mView = new CatLoadingView();
        mView.show(getChildFragmentManager(), "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 1000);
        //initData();

        //initUI();

        return root;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initUI();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private void initUI() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //progressBar.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new GameListAdapter(Glide.with(this), routeList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new GameListAdapter(Glide.with(this), routeList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        mView.dismiss();
    }

    private void initData() {
        final RouteInterface routeInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(RouteInterface.class);
        if(userID==null){
            Prefs.getUserProfile().getPlayer_id();
        }
        Call<RouteResponse> call = routeInterface.getAllRoutes(Integer.parseInt(userID));
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    routeList = new ArrayList<>();
                    routeList.addAll(response.body().getRoute());
                    initUI();
                } else {
                    Toast.makeText(getContext(), "Error loading",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading API",
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}
