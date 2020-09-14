package com.valle.foodieapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.CreateOrderModel;
import com.valle.foodieapp.network.NetworkResponceListener;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderPlacedAlertFragment extends BaseFragment implements NetworkResponceListener {

    private Animation animZoomIn, animZoomOut;

    @BindView(R.id.tvOrderPlaced)
    AppCompatTextView tvOrderPlaced;

    private CreateOrderModel createOrderModel;

    public OrderPlacedAlertFragment(CreateOrderModel createOrderModel) {
        this.createOrderModel = createOrderModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_placed_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        animZoomIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.zoom_in);
        tvOrderPlaced.startAnimation(animZoomIn);

/*
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", createOrderModel.response.orders_Info.Id);
            makeHttpCall(this, Apis.ASSIGN_DELIVERY_BOY, getRetrofitInterface().assignDelivery(jsonObject.toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    @OnClick(R.id.btTrackOrder)
    void OnClickbtTrackOrder() {

        ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new OrdersHistoryFragment(), null);

    }

    @Override
    public void onSuccess(String url, String responce) {

    }

    @Override
    public void onFailure(String url, Throwable throwable) {

    }
}
