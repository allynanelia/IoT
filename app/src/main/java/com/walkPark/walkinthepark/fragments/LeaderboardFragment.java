package com.walkPark.walkinthepark.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.adapters.GameListAdapter;
import com.walkPark.walkinthepark.adapters.LeaderboardAdapter;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.RouteResponse;
import com.walkPark.walkinthepark.models.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.name) TextView tvTopName;
    @BindView(R.id.rank) TextView tvTopRank;
    @BindView(R.id.spinner) MaterialSpinner spinner;

    CatLoadingView mView;

    private LeaderboardAdapter adapter;
    private List<UserInfo> leaderBoardList = new ArrayList<>();
    private List<String> spinnerList = new ArrayList<>();
    private SparseArray<List<UserInfo>> userListByRoutes = new SparseArray<>();
    private final String TAG = getClass().getName();
    private Unbinder unbinder;

    public static LeaderboardFragment newInstance(Bundle args) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initUI() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //progressBar.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new LeaderboardAdapter(Glide.with(this), leaderBoardList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setUserList(leaderBoardList);
            adapter.notifyDataSetChanged();
        }
        mView.dismiss();
    }

    private void initData() {
        final RouteInterface routeInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(RouteInterface.class);

        Call<RouteResponse> call = routeInterface.getAllRoutes(1);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
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

    private void initMockData(){

        spinner.setItems("SMU", "Fort Canning", "Little India", "Botanic Gardens", "Chinatown");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        leaderBoardList = new ArrayList<>();
        UserInfo ui = new UserInfo();
        ui.setUser_name("James");
        ui.setUser_step("10000");
        leaderBoardList.add(ui);

    }
    private void populateSpinner(){

    }
}
