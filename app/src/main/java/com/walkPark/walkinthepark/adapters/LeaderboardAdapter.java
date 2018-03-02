package com.walkPark.walkinthepark.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.dialogs.CheckPointDialog;
import com.walkPark.walkinthepark.models.CheckPoint;
import com.walkPark.walkinthepark.models.UserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by singy on 3/2/2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>{

    private List<UserInfo> userList;
    private Context context;

    public LeaderboardAdapter(List<UserInfo> userList) {
        this.userList = userList;
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        //@BindView(R.id.name) TextView tvName;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.steps_num) TextView stepsNum;
        @BindView(R.id.level) TextView level;
        @BindView(R.id.name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);


        }

        public void showDialog(String image) {
            CheckPointDialog.newInstance(image)
                    .show(((AppCompatActivity)context).getSupportFragmentManager() ,
                            "dialog_advertisement");
        }
    }
}
