package com.valle.foodieapp.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.adapter.BestResturantAdapter;
import com.valle.foodieapp.adapter.HomeScreenBannerAdapter;
import com.valle.foodieapp.adapter.HomeScreenSilderTwoAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.LocationListener;
import com.valle.foodieapp.models.RestaurantModel;
import com.valle.foodieapp.models.SecondSliderModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeScreenFragment extends BaseFragment implements LocationListener, NetworkResponceListener {

    @BindView(R.id.rvTopSlider)
    RecyclerView rvTopSlider;

    @BindView(R.id.rvTopSliderTwo)
    RecyclerView rvTopSliderTwo;

    @BindView(R.id.rvListData)
    RecyclerView rvListData;

    @BindView(R.id.llHomeView)
    LinearLayoutCompat llHomeView;

    @BindView(R.id.nsLayout)
    NestedScrollView nsLayout;

    @BindView(R.id.llDataNotFound)
    LinearLayoutCompat llDataNotFound;

    @BindView(R.id.rlMainHome)
    RelativeLayout rlMainHome;

    private RestaurantModel restaurantModel;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.shimmer_view_containerTwo)
    ShimmerFrameLayout mShimmerViewContainerTwo;

    @BindView(R.id.tvNoOfResturants)
    AppCompatTextView tvNoOfResturants;

    @BindView(R.id.tvRecomended)
    AppCompatTextView tvRecomended;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(this, view);
        view.setOnClickListener(null);
        ((HomeTabActivity) getActivity()).setLocationListner(this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvTopSlider.setLayoutManager(horizontalLayoutManager);

        LinearLayoutManager horizontalLayoutManagerTwo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvTopSliderTwo.setLayoutManager(horizontalLayoutManagerTwo);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListData.setLayoutManager(verticalLayoutManager);

    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(rlMainHome, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        onResume();
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

    @OnClick(R.id.fbHelp)
    void onClickHelp() {
        try {

            boolean isWhatsappInstalled = CommonUtils.whatsappInstalledOrNot("com.whatsapp", getActivity());
            if (isWhatsappInstalled) {
                Uri uri = Uri.parse("smsto:" + "3156478939");
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } else {
                new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.alert)).setIcon(getResources().getDrawable(R.drawable.whatsapp_icon)).setMessage("WhatsApp no instalada").setPositiveButton("Instalar ahora", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("market://details?id=com.whatsapp");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(goToMarket);
                        dialog.cancel();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeTabActivity) getActivity()).updateState();
        mShimmerViewContainerTwo.startShimmerAnimation();
        mShimmerViewContainer.startShimmerAnimation();
        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        String locationLattitude = new SharedPrefModule(getActivity()).getLocationLattitude();
        String locationLongitude = new SharedPrefModule(getActivity()).getLocationLongitude();
        if (!TextUtils.isEmpty(locationLattitude) && !TextUtils.isEmpty(locationLongitude)) {
            onLocationChanged(locationLattitude, locationLongitude);
        }
    }

    @Override
    public void onPause() {
        mShimmerViewContainerTwo.stopShimmerAnimation();
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onLocationChanged(String latitude, String longitude) {
        try {
            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                showSnakBar();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Role", "Restaurant");
            jsonObject.put("Latitude", !TextUtils.isEmpty(latitude) ? latitude : "");
            jsonObject.put("Longitude", !TextUtils.isEmpty(longitude) ? longitude : "");
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.TOP_DISCOUNTED_RESTAURANT, getRetrofitInterface().getTopDiscountRestaurant(jsonObject.toString(), "Discount_Percentage", "Restaurant"));
            makeHttpCall(this, Apis.TOP_RATED_RESTAURANTS, getRetrofitInterface().getTopDiscountRestaurant(jsonObject.toString(), "Overall_Rating", "Restaurant"));
            makeHttpCall(this, Apis.SECOND_SLIDER, getRetrofitInterface().getSecondSliderData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String url, String responce) {
        switch (url) {
            case Apis.TOP_DISCOUNTED_RESTAURANT:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    handleShrimmer();
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        tvNoOfResturants.setVisibility(View.VISIBLE);
                        RestaurantModel restaurantModel = new Gson().fromJson(responce, RestaurantModel.class);
                        HomeScreenBannerAdapter homeScreenBannerAdapter = new HomeScreenBannerAdapter(getActivity(),
                                restaurantModel.response.response);
                        rvTopSlider.setAdapter(homeScreenBannerAdapter);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.SECOND_SLIDER:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    handleShrimmer();

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        tvRecomended.setVisibility(View.VISIBLE);
                        SecondSliderModel secondSliderModel = new Gson().fromJson(responce, SecondSliderModel.class);
                        HomeScreenSilderTwoAdapter homeScreenSilderTwoAdapter = new HomeScreenSilderTwoAdapter(getActivity(),
                                secondSliderModel.response);
                        rvTopSliderTwo.setAdapter(homeScreenSilderTwoAdapter);
                    } else {
                        tvRecomended.setVisibility(View.GONE);
                        rvTopSliderTwo.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.TOP_RATED_RESTAURANTS:
                hideProgressDialog(getActivity());

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    handleShrimmerTwo();
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        tvNoOfResturants.setVisibility(View.VISIBLE);
                        restaurantModel = new Gson().fromJson(responce, RestaurantModel.class);
                        BestResturantAdapter bestResturantAdapter = new BestResturantAdapter(getActivity(), restaurantModel.response.response);
                        rvListData.setAdapter(bestResturantAdapter);
                        handleDataNotFoundVisiblity(restaurantModel.response.response.size() > 0);
                    } else {
                        handleDataNotFoundVisiblity(false);
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

    private void handleDataNotFoundVisiblity(boolean isAval) {
        llDataNotFound.setVisibility(isAval ? View.GONE : View.VISIBLE);
        nsLayout.setVisibility(isAval ? View.VISIBLE : View.GONE);
    }

    private void handleShrimmer() {
        if (mShimmerViewContainer.getVisibility() == View.VISIBLE) {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    }

    private void handleShrimmerTwo() {
        if (mShimmerViewContainerTwo.getVisibility() == View.VISIBLE) {
            mShimmerViewContainerTwo.stopShimmerAnimation();
            mShimmerViewContainerTwo.setVisibility(View.GONE);
        }
    }
}
