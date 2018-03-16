package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Constants;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.events.GameTriggerEvent;
import com.walkPark.walkinthepark.fragments.HomeFragment;
import com.walkPark.walkinthepark.fragments.LeaderboardFragment;
import com.walkPark.walkinthepark.models.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class MainActivity extends BaseActivity {

    @BindView(R.id.layoutBottomBar) View layoutBottomBar;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.layoutTopBar) View layoutTopBar;

    @BindView(R.id.buttonAccount) View buttonAccount;
    @BindView(R.id.buttonRoute) View buttonRoute;
    @BindView(R.id.buttonLeaderboard) View buttonLeaderboard;

    @BindView(R.id.imageAccount) ImageView imageAccount;
    @BindView(R.id.imageRoute) ImageView imageRoute;
    @BindView(R.id.imageLeaderboard) ImageView imageLeaderboard;

    @BindView(R.id.textAccount) TextView textAccount;
    @BindView(R.id.textLeaderboard) TextView textLeaderboard;
    @BindView(R.id.textRoute) TextView textRoute;


    private boolean doubleBackToExitPressedOnce = false;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentNameList = new ArrayList<>();

    private UserInfo user;

    private final String TAG = getClass().getName();

    private Typeface regularTypeface;
    private Typeface lightTypeface;
    private Typeface boldTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        user = Prefs.getUserProfile();
        //Setup font
        boldTypeface = TypefaceUtils.load(getAssets(), "fonts/DINNextLTPro-Bold.otf");
        lightTypeface = TypefaceUtils.load(getAssets(), "fonts/DINNextLTPro-Medium.otf");
        regularTypeface = TypefaceUtils.load(getAssets(), "fonts/DINNextLTPro-Regular.otf");

        initFragments();

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void initUI() {
        changeFragment(1);
        textRoute.setTypeface(boldTypeface);
        textAccount.setTypeface(regularTypeface);
        textLeaderboard.setTypeface(regularTypeface);

        imageAccount.setImageDrawable(ContextCompat
                .getDrawable(MainActivity.this, R.drawable.ic_account_inactive));
        imageLeaderboard.setImageDrawable(ContextCompat
                .getDrawable(MainActivity.this, R.drawable.ic_leaderboard_inactive));
        imageRoute.setImageDrawable(ContextCompat
                .getDrawable(MainActivity.this, R.drawable.ic_routes_active));

    }

    private void initFragments() {
        fragmentList.add(HomeFragment.newInstance(null)); // Leaderboard
        fragmentList.add(HomeFragment.newInstance(null)); // Routes
        fragmentList.add(LeaderboardFragment.newInstance(null)); // Account Settings

        fragmentNameList.add(getString(R.string.fragment_leaderboard));
        fragmentNameList.add(getString(R.string.fragment_routes));
        fragmentNameList.add(getString(R.string.fragment_account_settings));
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = Prefs.getUserProfile();
    }

    private void changeFragment(int position) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.layoutContent,
                        fragmentList.get(position),
                        fragmentNameList.get(position))
                .commit();
    }

    @OnClick(R.id.buttonAccount)
    public void goToAccount() {
        changeFragment(0);
        imageAccount.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_account_active));
        imageRoute.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_routes_inactive));
        imageLeaderboard.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_leaderboard_inactive));

        textAccount.setTypeface(boldTypeface);
        textLeaderboard.setTypeface(regularTypeface);
        textRoute.setTypeface(regularTypeface);

        textAccount.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.black));
        textLeaderboard.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
        textRoute.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
    }

    @OnClick(R.id.buttonRoute)
    public void goToRoute() {
        changeFragment(1);
        imageAccount.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_account_inactive));
        imageRoute.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_routes_active));
        imageLeaderboard.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_leaderboard_inactive));

        textAccount.setTypeface(regularTypeface);
        textLeaderboard.setTypeface(regularTypeface);
        textRoute.setTypeface(boldTypeface);

        textAccount.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
        textLeaderboard.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
        textRoute.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.black));
    }

    @OnClick(R.id.buttonLeaderboard)
    public void goToLeaderBoard() {
        changeFragment(2);
        imageAccount.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_account_inactive));
        imageRoute.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_routes_inactive));
        imageLeaderboard.setImageDrawable(
                ContextCompat.getDrawable(MainActivity.this,
                        R.drawable.ic_leaderboard_active));

        textAccount.setTypeface(regularTypeface);
        textLeaderboard.setTypeface(boldTypeface);
        textRoute.setTypeface(regularTypeface);

        textAccount.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
        textLeaderboard.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.black));
        textRoute.setTextColor(
                ContextCompat.getColor(MainActivity.this, R.color.pink_dark));
    }

    @Subscribe
    public void onEvent(GameTriggerEvent event) {
        Intent intent = new Intent(MainActivity.this, GameDetailsActivity.class);
        intent.putExtra(Constants.INTENT_GAME_SELECTED, Parcels.wrap(event.getRoute()));
        startActivity(intent);
    }
}
