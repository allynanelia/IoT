package com.walkPark.walkinthepark.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.walkinthepark.R;
import com.mohan.ribbonview.RibbonView;
import com.walkPark.walkinthepark.R;
import com.walkPark.walkinthepark.events.GameTriggerEvent;
import com.walkPark.walkinthepark.models.Route;

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
    private List<Route> routeList;

    //Check

    public GameListAdapter(RequestManager glide, List<Route> routeList) {
        this.glide = glide;
        this.routeList = routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_generic_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (routeList != null) {
            glide.load(routeList.get(position).getImage_url()).placeholder(R.color.placeholder)
                    .centerCrop().into(holder.image);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(routeList.get(position).getName());
            if(routeList.get(position).getIs_recommended()){
                holder.ribbon.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (routeList != null) {
            return routeList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView) View rootView;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.text) TextView text;
        @BindView(R.id.ribbonView2) RibbonView ribbon;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    routeList.get(getAdapterPosition());
                    EventBus.getDefault().post(new GameTriggerEvent(
                            routeList.get(getAdapterPosition())
                    ));
                }
            });
        }
    }
}
