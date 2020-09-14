package com.valle.foodieapp.network;


public interface NetworkResponceListener {

    void onSuccess(String url, String responce);

    void onFailure(String url, Throwable throwable);
}
