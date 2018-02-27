package com.walkPark.walkinthepark.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.events.GameTriggerEvent;
import com.walkPark.walkinthepark.models.Game;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private final String TAG = getClass().getName();
    private RequestManager glide;
    private List<Game> gameList;

    //Check

    public GameListAdapter(RequestManager glide, List<Game> gameList) {
        this.glide = glide;
        this.gameList = gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_generic_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (gameList != null) {
            glide.load(gameList.get(position).getGame_image_url()).placeholder(R.color.placeholder)
                    .centerCrop().into(holder.image);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(gameList.get(position).getGame_name());
        }
    }

    @Override
    public int getItemCount() {
        if (gameList != null) {
            return gameList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView) View rootView;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.text) TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameList.get(getAdapterPosition());
                    EventBus.getDefault().post(new GameTriggerEvent(
                            gameList.get(getAdapterPosition())
                    ));
                }
            });
        }
    }
}
