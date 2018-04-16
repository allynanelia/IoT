
package com.walkPark.walkinthepark.dialogs;


import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
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
import com.walkPark.walkinthepark.R;
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
    @BindView(R.id.dialog_title) TextView dialogTitle;

    private Unbinder unbinder;
    private final String TAG = getClass().getName();
    private String image;
    private String desc;
    private String type;
    private SoundPool congratsSoundPool;
    private Integer clickSound;

    public static CheckPointDialog newInstance(String image, String desc, String type) {
        Bundle args = new Bundle();
        args.putString(Constants.CHECKPOINT_IMAGE, image);
        args.putString(Constants.CHECKPOINT_DESC, desc);
        args.putString(Constants.CHECKPOINT_TYPE, type);
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
            type = getArguments().getString(Constants.CHECKPOINT_TYPE);
        }

        initUI();

        return root;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        if(congratsSoundPool!=null && clickSound != null) {
            congratsSoundPool.stop(clickSound);
            congratsSoundPool.release();
        }
        super.onDestroyView();

    }

    private void initUI() {
        buttonClose.setVisibility(View.GONE);
        if(type.equals("YOU'VE FOUND IT!")) {
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
        }

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
                if(textHintOrSuccess!=null) {
                    textHintOrSuccess.setText(desc);
                    dialogTitle.setText(type);
                }
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

        if(type.equals("YOU'VE FOUND IT!")) {
            congratsSoundPool = (new SoundPool.Builder()).setMaxStreams(10).build();
            clickSound = congratsSoundPool.load(getContext(), R.raw.clap, 1);
            congratsSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    congratsSoundPool.play(clickSound, 0.99F, 0.99F, 0, 0, 1.0F);
                }
            });
        }
    }
}
