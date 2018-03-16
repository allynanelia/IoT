
package com.walkPark.walkinthepark.dialogs;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class CheckPointDialog extends DialogFragment {

    @BindView(R.id.buttonClose) ImageView buttonClose;
    @BindView(R.id.imageHintOrSuccess) ImageView imageHintOrSuccess;
    @BindView(R.id.textHintOrSuccess) TextView textHintOrSuccess;
    @BindView(R.id.viewKonfetti) KonfettiView viewKonfetti;

    private Unbinder unbinder;
    private final String TAG = getClass().getName();
    private String image;
    private String desc;

    public static CheckPointDialog newInstance(String image, String desc) {
        Bundle args = new Bundle();
        args.putString(Constants.CHECKPOINT_IMAGE, image);
        args.putString(Constants.CHECKPOINT_DESC, desc);
        CheckPointDialog fragment = new CheckPointDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.WalkInTheParkDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_checklist_completed, container, false);

        unbinder = ButterKnife.bind(this, root);

        if (getArguments() != null) {
            image = getArguments().getString(Constants.CHECKPOINT_IMAGE);
            desc = getArguments().getString(Constants.CHECKPOINT_DESC);
        }

        initUI();

        return root;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void initUI() {
        buttonClose.setVisibility(View.GONE);
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(image)) {
                    if (getContext() != null) {
                        Glide.with(getContext())
                                .load(image)
                                .placeholder(R.color.placeholder)
                                .centerCrop()
                                .into(imageHintOrSuccess);
                    }
                }
                textHintOrSuccess.setText(desc);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonClose.setVisibility(View.VISIBLE);
            }
        }, 200);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
