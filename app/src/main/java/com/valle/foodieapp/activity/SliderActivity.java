package com.valle.foodieapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.valle.foodieapp.R;
import com.valle.foodieapp.base.BaseActivity;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;

public class SliderActivity extends BaseActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        checkLocationPermission();

        View.OnClickListener onClickListener = v -> {
            startActivity(new Intent(this, LoginActivity.class));
        };

        findViewById(R.id.tvLogin).setOnClickListener(onClickListener);
        findViewById(R.id.tvSkip).setOnClickListener(onClickListener);

        findViewById(R.id.tvSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

      /*  PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/
    }

    @Override
    public void onBackPressed() {
        exitFromApp(SliderActivity.this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.plz_allow_loc_per))
                        .setMessage(getResources().getString(R.string.plz_allow_for_best_results))
                        .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(SliderActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SliderActivity.this, permission)) {
                        //denied
                        checkLocationPermission();
                    } else {
                        if (ActivityCompat.checkSelfPermission(SliderActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {

                        } else {
                            //set to never ask again
                            new androidx.appcompat.app.AlertDialog.Builder(SliderActivity.this)
                                    .setTitle("Permiso necesario")
                                    .setMessage("El permiso de ubicación es necesario")
                                    .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", SliderActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
                return;
            }
        }
    }

}
