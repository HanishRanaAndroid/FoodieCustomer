package com.valle.foodieapp.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.valle.foodieapp.network.NetworkResponceListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkManagerToUploadImageData {

    private NetworkResponceListener networkResponceListener;

    public NetworkManagerToUploadImageData(NetworkResponceListener networkResponceListener) {
        this.networkResponceListener = networkResponceListener;
    }

    @SuppressLint("CheckResult")
    public void getNetworkResponce(String url, Object o) {

        ((Call<String>)o).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                networkResponceListener.onSuccess(url, response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure", "//" + t.getMessage());
                networkResponceListener.onFailure(url, t);
            }
        });

    }
}
