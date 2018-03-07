package com.walkPark.walkinthepark.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.walkinthepark.R;

import com.walkPark.walkinthepark.dialogs.CheckPointDialog;
import com.walkPark.walkinthepark.models.CheckPoint;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Boon Sing on 27-Feb-18.
 */

public class CheckPointAdapter extends RecyclerView.Adapter<CheckPointAdapter.ViewHolder> {
    private final String TAG = getClass().getName();
    private List<CheckPoint> checkPointList;
    private Context context;

    public CheckPointAdapter(Context context, List<CheckPoint> checkPointList) {
            this.context = context;
            this.checkPointList = checkPointList;
    }

    public void setCheckPointList(List<CheckPoint> checkPointList) {
        this.checkPointList = checkPointList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_checkpoint_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (checkPointList != null) {
            holder.buttonIndicator.setText(checkPointList.get(position).getId());
            if (checkPointList.get(position).getStatus().equals("0")) {
                holder.checkPointLock.setVisibility(View.VISIBLE);
                holder.buttonCheck.setVisibility(View.GONE);
                holder.buttonInfo.setVisibility(View.GONE);
                holder.buttonHint.setVisibility(View.GONE);
                holder.lockIcon.setVisibility(View.VISIBLE);
                holder.checkPointTitle.setText("Checkpoint " +
                        checkPointList.get(position).getId());
            } else if (checkPointList.get(position).getStatus().equals("1")) {
                holder.checkPointLock.setVisibility(View.GONE);
                holder.buttonCheck.setVisibility(View.VISIBLE);
                holder.buttonHint.setVisibility(View.VISIBLE);
                holder.buttonInfo.setVisibility(View.GONE);
                holder.lockIcon.setVisibility(View.GONE);
                holder.checkPointTitle.setText(checkPointList.get(position).getTitle());
            } else {
                holder.checkPointLock.setVisibility(View.GONE);
                holder.buttonCheck.setVisibility(View.GONE);
                holder.buttonHint.setVisibility(View.GONE);
                holder.buttonInfo.setVisibility(View.VISIBLE);
                holder.lockIcon.setVisibility(View.GONE);
                holder.checkPointTitle.setText(checkPointList.get(position).getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (checkPointList != null) {
            return checkPointList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkPointLock) View checkPointLock;
        @BindView(R.id.buttonCheck) ImageView buttonCheck;
        @BindView(R.id.buttonHint) ImageView buttonHint;
        @BindView(R.id.buttonInfo) ImageView buttonInfo;
        @BindView(R.id.checkPointTitle) TextView checkPointTitle;
        @BindView(R.id.buttonIndicator) TextView buttonIndicator;
        @BindView(R.id.lockIcon) ImageView lockIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            checkPointLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                            "Please complete current checkpoint to unlock",
                            Toast.LENGTH_SHORT).show();
                }
            });

            buttonCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO call beacon API.
                    String image = "";
                    if (true) {
                        showDialog(image);
                    } else {
                        showDialog(image);
                    }
                }
            });

            buttonHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO get the checkpoint hint
                    String image = "";
                    showDialog(image);
                }
            });

            buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO get the info
                    String image = "";
                    showDialog(image);
                }
            });
        }
        public void showDialog(String image) {
            CheckPointDialog.newInstance(image)
                    .show(((AppCompatActivity)context).getSupportFragmentManager() ,
                    "dialog_advertisement");
        }
    }
}
