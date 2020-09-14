package com.valle.foodieapp.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.adapter.ChatAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.ChatModel;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatFragment extends BaseFragment implements NetworkResponceListener {

    @BindView(R.id.rvChat)
    RecyclerView rvChat;

    @BindView(R.id.etTextMessage)
    AppCompatEditText etTextMessage;

    @BindView(R.id.tvPleaseWait)
    AppCompatTextView tvPleaseWait;

    private ChatModel chatModel = new ChatModel();
    private Timer timer;

    private ChatAdapter chatAdapter;
    private OrderPlacedModel.responseData.orders_InfoData orders_infoData;

    public ChatFragment(OrderPlacedModel.responseData.orders_InfoData orders_infoData) {
        this.orders_infoData = orders_infoData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvChat.setLayoutManager(verticalLayoutManager);
    }

    private void callChatApi() {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Delivery_Boy_Id", orders_infoData.Delivery_Boy_Id.User_Id);

            makeHttpCall(this, Apis.CHAT_API, getRetrofitInterface().chatApi(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    callChatApi();
                });
            }
        }, 0, 4000);
    }

    @OnClick(R.id.ivSelectAttachement)
    void OnClickivSelectAttachement() {

    }

    @OnClick(R.id.tvSendMessage)
    void OnClicktvSendMessage() {
        String message = etTextMessage.getText().toString();

        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            tvPleaseWait.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Delivery_Boy_Id", orders_infoData.Delivery_Boy_Id.User_Id);
            jsonObject.put("Customer_Message", message);
            String value = new Gson().toJson(orders_infoData);
            jsonObject.put("jsonObject", value);
            makeHttpCall(this, Apis.SUBMIT_CHAT, getRetrofitInterface().submitChatApi(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String url, String responce) {
        switch (url) {
            case Apis.CHAT_API:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        chatModel = new Gson().fromJson(responce, ChatModel.class);
                        chatAdapter = new ChatAdapter(getActivity(), chatModel);
                        rvChat.setAdapter(chatAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.SUBMIT_CHAT:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        etTextMessage.setText("");
                        tvPleaseWait.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {

    }
}
