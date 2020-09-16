package com.valle.foodieapp.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.valle.foodieapp.network.APIClient;
import com.valle.foodieapp.network.APIInterface;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkManager;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DeviceTokenHandlerService extends Service implements NetworkResponceListener {

    private static final String TAG = "DeviceTokenHandler";
    private Timer deviceTokenTimer;
    private String deviceToken;
    private int count = 0;

    @Override
    public void onSuccess(String url, String responce) {
        Log.d(TAG, responce);
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        Log.d(TAG, throwable.getMessage());
    }

    @SuppressLint("StaticFieldLeak")
    public class UpdateToken extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String userId = new SharedPrefModule(getApplicationContext()).getUserId();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("User_Id", userId);

                JSONObject object = new JSONObject();
                object.put("Device_Token", !TextUtils.isEmpty(deviceToken) ? deviceToken : JSONObject.NULL);

                makeHttpCall(DeviceTokenHandlerService.this, Apis.USER_TOKEN, getRetrofitInterface().updateUserPresence(jsonObject.toString(), object.toString()));
                try {
                    if (!TextUtils.isEmpty(deviceToken)) {
                        count++;
                    }
                    if (count == 2) {
                        deviceTokenTimer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");

        try {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                deviceToken = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {

            deviceTokenTimer = new Timer();
            deviceTokenTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        new UpdateToken().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 30000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        try {
            deviceTokenTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void makeHttpCall(NetworkResponceListener context, String url, Object o) {
        new NetworkManager(context).getNetworkResponce(url, o);
    }

    public APIInterface getRetrofitInterface() {
        return APIClient.getClient().create(APIInterface.class);
    }

}