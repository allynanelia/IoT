package com.walkPark.walkinthepark.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.walkPark.walkinthepark.R;
import com.roger.catloadinglibrary.CatLoadingView;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.backend.LeaderBoardInterface;
import com.walkPark.walkinthepark.backend.ProfileInterface;
import com.walkPark.walkinthepark.backend.WalkInTheParkRetrofit;
import com.walkPark.walkinthepark.models.Leaderboard;
import com.walkPark.walkinthepark.models.LeaderboardResponse;
import com.walkPark.walkinthepark.models.Profile;
import com.walkPark.walkinthepark.models.ProfileResponse;
import com.walkPark.walkinthepark.models.Route;
import com.walkPark.walkinthepark.models.UserInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.stepCount) TextView tvSteps;
    @BindView(R.id.tvCaloriesBurned) TextView tvCaloriesBurned;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.time_spent) TextView tvTimeSpent;

    CatLoadingView mView;

    private Profile player;
    private List<Route> completedRouteList;
    private Unbinder unbinder;

    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, root);

        mView = new CatLoadingView();
        mView.show(getChildFragmentManager(), "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
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

    public void initData() {
        final ProfileInterface profileInterface = WalkInTheParkRetrofit
                .getInstance()
                .create(ProfileInterface.class);

        Call<ProfileResponse> call = profileInterface.getProfile(Integer.parseInt(Prefs.getUserProfile().getPlayer_id()));
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    player = response.body().getPlayer();
                    if(response.body().getRoutes()!=null) {
                        completedRouteList = new ArrayList<>();
                        completedRouteList.addAll(response.body().getRoutes());
                    }
                    initUI();
                } else {
                    Toast.makeText(getContext(), "Error loading",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading API",
                        Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void initUI() {
        tvName.setText(player.getName());
        if(player.getTotal_calories()!=null) {
            tvCaloriesBurned.setText(player.getTotal_calories() + " cal");
        } else {
            tvCaloriesBurned.setText("0");
        }
        if(player.getTotal_points()!=null){
            tvPoints.setText(Double.toString(player.getTotal_points()));
        } else {
            tvPoints.setText("0 cal");
        }
        if(player.getTotal_steps_taken()!=null){
            tvSteps.setText(Integer.toString(player.getTotal_steps_taken()));
        } else {
            tvSteps.setText("0");
        }

        if(player.getTotal_time_taken()!=null) {

            double time = player.getTotal_time_taken();
            final long hours = (long) time / 3600;
            final long mins = (long) time / 60;
            final long secs = (long) time % 60;

            tvTimeSpent.setText(String.format("%02d", hours)+":"+String.format("%02d", mins)+":"+String.format("%02d", secs));
        } else {
            tvTimeSpent.setText("00:00:00");
        }

        mView.dismiss();
    }
}
