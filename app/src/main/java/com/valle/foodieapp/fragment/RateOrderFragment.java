package com.valle.foodieapp.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class RateOrderFragment extends BaseFragment implements NetworkResponceListener {

    @BindView(R.id.ivRestImage)
    AppCompatImageView ivRestImage;

    @BindView(R.id.tvRestName)
    AppCompatTextView tvRestName;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.etFeedback)
    AppCompatEditText etFeedback;

    @BindView(R.id.scRate)
    ScrollView scRate;

    private float ratingPoints;

    private OrderPlacedModel.responseData.orders_InfoData orders_infoData;

    public RateOrderFragment(OrderPlacedModel.responseData.orders_InfoData orders_infoData) {
        this.orders_infoData = orders_infoData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        ratingBar.setRating(1);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingPoints = rating);
        setData();
    }

    private void setData() {

        if (orders_infoData == null) {
            return;
        }

        try {

            Glide.with(getActivity()).load(Apis.IMAGE_URL + orders_infoData.Rest_Id.Profile_Image).into(ivRestImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvRestName.setText(orders_infoData.Rest_Id.Restaurant_Name);
    }

    @OnClick(R.id.btSubmitRating)
    void OnClicktvSubmitRating() {
         if (!CommonUtils.isNetworkAvailable(getActivity())){
             showSnakBar(scRate);
             return;
         }
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Rating_For", "Restaurant");
            jsonObject.put("To_Id", orders_infoData.Rest_Id.User_Id);
            jsonObject.put("Order_Id", orders_infoData.Id);
            jsonObject.put("Rating", ratingPoints);
            jsonObject.put("Comment", etFeedback.getText().toString());

            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.SUBMIT_RATING, getRetrofitInterface().submitRating(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {
            case Apis.SUBMIT_RATING:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.thanks_for_rating), Toast.LENGTH_SHORT).show();
                        ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new HomeScreenFragment(), null);
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
}
