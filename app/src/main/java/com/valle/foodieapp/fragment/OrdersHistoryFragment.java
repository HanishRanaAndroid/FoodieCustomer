package com.valle.foodieapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.adapter.MyOrdersListAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.WishlistListener;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;

public class OrdersHistoryFragment extends BaseFragment implements NetworkResponceListener, WishlistListener {

    private MyOrdersListAdapter myOrdersListAdapter;

    @BindView(R.id.rvMyOrders)
    RecyclerView rvMyOrders;

    @BindView(R.id.llNoOrderFound)
    LinearLayoutCompat llNoOrderFound;

    @BindView(R.id.llHistory)
    LinearLayoutCompat llHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvMyOrders.setLayoutManager(horizontalLayoutManager);

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        setOrderHistory();
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(llHistory, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        setOrderHistory();
                    } else {
                        showSnakBar();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void setOrderHistory() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());

            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.ORDER_HISTORY, getRetrofitInterface().getOrderHistory(jsonObject.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {
            case Apis.ORDER_HISTORY:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        try {
                            JSONArray jsonArrayOrderedInfo = jsonObject.getJSONObject("response").getJSONArray("orders_Info");

                            for (int i = 0; i < jsonArrayOrderedInfo.length(); i++) {
                                JSONArray jsonArrayItems = jsonArrayOrderedInfo.getJSONObject(i).getJSONObject("Ordered_Items").getJSONArray("Items");
                                for (int j = 0; j < jsonArrayItems.length(); j++) {
                                    if (!jsonArrayItems.getJSONObject(j).has("Item_Flavor_Type")) {
                                        jsonArrayItems.getJSONObject(j).put("Item_Flavor_Type", "");
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        OrderPlacedModel orderPlacedModel = new Gson().fromJson(responce, OrderPlacedModel.class);
                        myOrdersListAdapter = new MyOrdersListAdapter(getActivity(), orderPlacedModel, this);
                        rvMyOrders.setAdapter(myOrdersListAdapter);
                        handleVisibility(orderPlacedModel.response.orders_Info.size() > 0);
                    } else {
                        handleVisibility(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.UPDATE_WISHLIST:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        Toast.makeText(getActivity(), getResources().getString(R.string.added_succesfully), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        hideProgressDialog(getActivity());
    }

    private void handleVisibility(boolean isVisible) {
        rvMyOrders.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        llNoOrderFound.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void OnClickItem(OrderPlacedModel.responseData.orders_InfoData orders_infoData, String wishlisted) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", orders_infoData.Id);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("Wishlist", wishlisted);

            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.UPDATE_WISHLIST, getRetrofitInterface().submitWishList(jsonObject.toString(), jsonObject1.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnRepeatOrder(OrderPlacedModel.responseData.orders_InfoData orders_infoData) {

    }
}
