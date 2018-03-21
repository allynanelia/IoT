package com.walkPark.walkinthepark.fragments;

import android.widget.TextView;

import com.example.walkinthepark.R;
import com.roger.catloadinglibrary.CatLoadingView;

import org.w3c.dom.Text;

import butterknife.BindView;

public class ProfileFragment {

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.steps) TextView tvSteps;
    @BindView(R.id.tvCaloriesBurned) TextView tvCaloriesBurned;
    @BindView(R.id.tvPoints) TextView tvPoints;
    @BindView(R.id.time_spent) TextView tvTimeSpent;

    CatLoadingView mView;

}
