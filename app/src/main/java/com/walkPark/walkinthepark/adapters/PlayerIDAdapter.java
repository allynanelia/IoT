package com.walkPark.walkinthepark.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.events.GiveUpCheckPointEvent;
import com.walkPark.walkinthepark.events.SelectIDEvent;
import com.walkPark.walkinthepark.models.UserInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerIDAdapter extends RecyclerView.Adapter<PlayerIDAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Integer> idList;
    private int currentID;

    public PlayerIDAdapter(Context context, List<Integer> idList) {
        layoutInflater = LayoutInflater.from(context);
        this.idList = idList;
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    @Override
    public PlayerIDAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerIDAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_player_id, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayerIDAdapter.ViewHolder holder, int position) {

        int user = idList.get(position);
        holder.setCurrentID(user);

        holder.textView.setVisibility(View.VISIBLE);
        holder.textView.setText("Player ID: " + user);

        holder.imageView.setImageResource(R.drawable.ic_account_inactive);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.id_layout) LinearLayout idLayout;
        @BindView(R.id.text_view) TextView textView;
        @BindView(R.id.image_view) ImageView imageView;

        private int currentID;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            idLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new SelectIDEvent(currentID));
                }
            });
        }

        public int getCurrentID() {
            return currentID;
        }

        public void setCurrentID(int currentID) {
            this.currentID = currentID;
        }
    }
}