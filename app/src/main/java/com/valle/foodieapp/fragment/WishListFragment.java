package com.valle.foodieapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.adapter.WishlishAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class WishListFragment extends BaseFragment implements NetworkResponceListener, WishlistListener {

    @BindView(R.id.rvWishlist)
    RecyclerView rvWishlist;

    @BindView(R.id.llNoOrderFound)
    LinearLayoutCompat llNoOrderFound;

    @BindView(R.id.llWishlist)
    LinearLayoutCompat llWishlist;

    private WishlishAdapter wishlishAdapter;
    private OrderPlacedModel orderPlacedModel;

    private String id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvWishlist.setLayoutManager(verticalLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        getWishList();
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(llWishlist, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        getWishList();
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

    private void getWishList() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Wishlist", "Yes");
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());

            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.ORDER_HISTORY, getRetrofitInterface().getWishList(jsonObject.toString()));

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
                        orderPlacedModel = new Gson().fromJson(responce, OrderPlacedModel.class);
                        wishlishAdapter = new WishlishAdapter(getActivity(), orderPlacedModel.response.orders_Info, this);
                        rvWishlist.setAdapter(wishlishAdapter);
                        handleVisibility(orderPlacedModel.response.orders_Info.size() > 0);
                    } else {
                        handleVisibility(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.UPDATE_WISHLIST:

            case Apis.REPEAT_ORDER:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        for (int i = 0; i < orderPlacedModel.response.orders_Info.size(); i++) {
                            if (orderPlacedModel.response.orders_Info.get(i).Id.equalsIgnoreCase(id)) {
                                orderPlacedModel.response.orders_Info.remove(i);
                                break;
                            }
                        }
                        wishlishAdapter.notifyDataSetChanged();
                        handleVisibility(orderPlacedModel.response.orders_Info.size() > 0);

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
        rvWishlist.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        llNoOrderFound.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void OnClickItem(OrderPlacedModel.responseData.orders_InfoData orders_infoData, String wishlisted) {
        try {
            id = orders_infoData.Id;
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
        try {
            id = orders_infoData.Id;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Rest_Id", orders_infoData.Rest_Id.User_Id);
            JSONArray jsonArray = new JSONArray();

            List<OrderPlacedModel.responseData.orders_InfoData.Ordered_ItemsData.ItemsData> dataList = orders_infoData.Ordered_Items.Items;
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject items = new JSONObject();
                items.put("Item_Id", dataList.get(i).Id);
                items.put("Item_Quantity", dataList.get(i).Quantity);
                items.put("Item_Name", dataList.get(i).Item_Name);
                items.put("Item_Image", dataList.get(i).Item_Image);
                items.put("Item_Flavor_Type", dataList.get(i).Item_Flavor_Type);
                jsonArray.put(items);
            }
            jsonObject.put("Items", jsonArray);
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.REPEAT_ORDER, getRetrofitInterface().repeatOrder(jsonObject.toString(), orders_infoData.Id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
