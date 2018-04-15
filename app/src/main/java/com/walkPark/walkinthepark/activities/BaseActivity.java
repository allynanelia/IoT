package com.walkPark.walkinthepark.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.walkPark.walkinthepark.R;
import com.rajeefmk.runtimepermissionhandler.PermissionUtils;
import com.rajeefmk.runtimepermissionhandler.PermissionsApi;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Boon Sing on 28-Feb-18.
 */

public class BaseActivity extends AppCompatActivity implements PermissionsApi.PermissionCallback {

    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionsApi.getInstance().setPermissionCallback(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (this.isFinishing()) {
            return;
        }

        PermissionUtils.onRequestPermissionsResult(
                this, requestCode, permissions, grantResults, null);
    }

    @Override
    public void onPermissionGranted(int i) {
        Toast.makeText(this,
                getString(R.string.toast_permission_granted), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(int i) {
        Toast.makeText(this,
                getString(R.string.toast_permission_denied), Toast.LENGTH_SHORT).show();
    }
}
