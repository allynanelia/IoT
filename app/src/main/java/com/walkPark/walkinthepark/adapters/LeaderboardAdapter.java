package com.walkPark.walkinthepark.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.dialogs.CheckPointDialog;
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

    public LeaderboardAdapter(Context context, List<UserInfo> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaderboardAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_leaderboard_positions, parent, false));
    }

    @Override
    public void onBindViewHolder(final LeaderboardAdapter.ViewHolder holder, int position) {

            UserInfo user = userList.get(position);

            holder.stepsNum.setVisibility(View.VISIBLE);
            holder.stepsNum.setText(user.getPoints());

            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(user.getPlayer_name());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.steps_num) TextView stepsNum;
        @BindView(R.id.name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
