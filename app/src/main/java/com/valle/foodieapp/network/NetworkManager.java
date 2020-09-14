package com.valle.foodieapp.network;

import android.annotation.SuppressLint;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkManager {

    private NetworkResponceListener networkResponceListener;
    private String url;

    public NetworkManager(NetworkResponceListener networkResponceListener) {
        this.networkResponceListener = networkResponceListener;
    }

    @SuppressLint("CheckResult")
    public void getNetworkResponce(String url, Object o) {
        this.url = url;
        ((Observable<String>) o).subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                map(result -> result).
                subscribe(this::handleResults, this::handleError);

    }

    private void handleError(Throwable throwable) {
        networkResponceListener.onFailure(url, throwable);
    }

    private void handleResults(String s) {
        networkResponceListener.onSuccess(url, s);
    }
}
