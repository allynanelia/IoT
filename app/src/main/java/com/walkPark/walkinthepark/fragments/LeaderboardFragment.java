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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.adapters.LeaderboardAdapter;
import com.walkPark.walkinthepark.backend.LeaderBoardInterface;
import com.walkPark.walkinthepark.backend.RouteInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.Leaderboard;
import com.walkPark.walkinthepark.models.LeaderboardResponse;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.RouteResponse;
import com.walkPark.walkinthepark.models.UserInfo;

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
    @BindView(R.id.steps_num) TextView tvStepsNum;
    @BindView(R.id.steps) TextView tvSteps;
    @BindView(R.id.spinner) MaterialSpinner spinner;
    @BindView(R.id.route_name) TextView tvRouteName;
    @BindView(R.id.text) TextView tvText;
    @BindView(R.id.position) TextView tvPosition;

    CatLoadingView mView;

    private LinearLayoutManager layoutManager;
    private LeaderboardAdapter adapter;
    private List<Route> routeList = new ArrayList<>();
    private List<String> routeNameList = new ArrayList<>();
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
                initData1();
            }
        }, 1000);

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
        tvPosition.setVisibility(View.VISIBLE);
        tvSteps.setVisibility(View.VISIBLE);

        List<UserInfo> userList = userListByRoutes.get(1);
        tvStepsNum.setText(userList.get(0).getPoints());
        tvTopName.setText(userList.get(0).getPlayer_name());
        userList.remove(0);

        adapter = new LeaderboardAdapter(getContext(), userList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mView.dismiss();
    }

    private void initData1() {
        final RouteInterface routeInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(RouteInterface.class);

        Call<RouteResponse> call = routeInterface.getAllRoutes(1);
        call.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    routeList.addAll(response.body().getRoute());
                    for(Route r: routeList) {
                        routeNameList.add(r.getName());
                    }
                    spinner.setItems(routeNameList);
                    tvRouteName.setText(routeNameList.get(0));
                    spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                            Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                        }
                    });

                    initData2();

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

    private void initData2(){
        final LeaderBoardInterface leaderBoardInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(LeaderBoardInterface.class);

        Call<LeaderboardResponse> call2 = leaderBoardInterface.getLeaderboard();
        call2.enqueue(new Callback<LeaderboardResponse>() {
            @Override
            public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                if (response.isSuccessful()) {
                    List<Leaderboard> leaderboardList = response.body().getLeaderboard();
                    for(Leaderboard i: leaderboardList) {
                        userListByRoutes.append(Integer.parseInt(i.getRoute_id()), i.getPlayers());
                    }
                    initUI();
                } else {
                    Toast.makeText(getContext(), "Error loading",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading API",
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}
