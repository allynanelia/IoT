package com.walkPark.walkinthepark.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkinthepark.R;
import com.walkPark.walkinthepark.Prefs;
import com.walkPark.walkinthepark.events.GameTriggerEvent;
import com.walkPark.walkinthepark.events.UpdateBottomBarEvent;
import com.walkPark.walkinthepark.fragments.HomeFragment;
import com.walkPark.walkinthepark.models.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.layoutBottomBar) View layoutBottomBar;
    @BindView(R.id.buttonRight) Button buttonRight;
    @BindView(R.id.textTitle) TextView textTitle;
    @BindView(R.id.layoutTopBar) View layoutTopBar;
    @BindView(R.id.buttonHome) ImageView buttonHome;
    @BindView(R.id.buttonHome2) ImageView buttonHome2;
    @BindView(R.id.buttonHome3) ImageView buttonHome3;
    @BindView(R.id.buttonHome4) ImageView buttonHome4;

    private boolean doubleBackToExitPressedOnce = false;
    private List<Fragment> fragmentList = new ArrayList<>();

    private UserInfo user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initUI();

        initFragments();

        user = Prefs.getUserProfile();
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
        buttonRight.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonHome2.setOnClickListener(this);
        buttonHome3.setOnClickListener(this);
        buttonHome4.setOnClickListener(this);
    }

    private void initFragments() {
        fragmentList.add(HomeFragment.newInstance(null)); // 0
        fragmentList.add(HomeFragment.newInstance(null)); // 1
        fragmentList.add(HomeFragment.newInstance(null)); // 2
        fragmentList.add(HomeFragment.newInstance(null)); // 3
    }

    private Fragment getFragment(int fragmentId) {
        switch (fragmentId) {
            case R.string.fragment_title_home1:
                return fragmentList.get(0);
            case R.string.fragment_title_home2:
                return fragmentList.get(1);
            case R.string.fragment_title_home3:
                return fragmentList.get(2);
            case R.string.fragment_title_home4:
                return fragmentList.get(3);
            default:
                return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        user = Prefs.getUserProfile();
    }

    private void changeFragment(int fragmentId, boolean addToBackStack) {
        Fragment fragment = getFragment(fragmentId);
        String fragmentName = getString(fragmentId);

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentName);

        if (currentFragment == null || !currentFragment.isVisible()) {
            if (addToBackStack) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layoutContent, fragment, fragmentName)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layoutContent, fragment, fragmentName)
                        .commit();
            }
        }
    }

    private void updateBottomSelector(String fragment_title) {
        switch (fragment_title) {
            case "Home":
                buttonHome.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_share));
                //E.g.
                break;
            case "Home2":
                break;
            case "Home3":
                break;
            case "Home4":
                break;

        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonHome) {
            changeFragment(R.string.fragment_title_home1, true);
        } else if (v == buttonHome2) {
            changeFragment(R.string.fragment_title_home2, true);
        } else if (v == buttonHome3) {
            changeFragment(R.string.fragment_title_home3, true);
        } else if (v == buttonHome4) {
            changeFragment(R.string.fragment_title_home4, true);
        }
    }

    @Subscribe
    public void onEvent(UpdateBottomBarEvent event) {
        updateBottomSelector(event.getFragment_title());
    }

    @Subscribe
    public void onEvent(GameTriggerEvent event) {
        startActivity(new Intent(MainActivity.this, GameDetailsActivity.class));
    }
}
