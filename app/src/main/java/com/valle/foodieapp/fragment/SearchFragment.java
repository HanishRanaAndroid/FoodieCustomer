package com.valle.foodieapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.adapter.SearchBestDishesAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.RestaurantModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchFragment extends BaseFragment implements NetworkResponceListener {

    private RecyclerView rvSearchListData;
    private SearchBestDishesAdapter searchBestDishesAdapter;

    @BindView(R.id.etSearch)
    AppCompatEditText etSearch;

    @BindView(R.id.ivClearText)
    AppCompatImageView ivClearText;

    @BindView(R.id.llDataNotFound)
    LinearLayoutCompat llDataNotFound;

    @BindView(R.id.rlSearchList)
    RelativeLayout rlSearchList;

    @BindView(R.id.llMainSearch)
    LinearLayoutCompat llMainSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        rvSearchListData = view.findViewById(R.id.rvSearchListData);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearchListData.setLayoutManager(verticalLayoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (searchBestDishesAdapter == null) {
                    return;
                }

                searchBestDishesAdapter.filterData(s.toString());
                if (TextUtils.isEmpty(s.toString())) {
                    ivClearText.setVisibility(View.GONE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        callApi();
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(llMainSearch, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        callApi();
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

    private void callApi() {
        try {
            String locationLattitude = new SharedPrefModule(getActivity()).getLocationLattitude();
            String locationLongitude = new SharedPrefModule(getActivity()).getLocationLongitude();
            if (!TextUtils.isEmpty(locationLattitude) && !TextUtils.isEmpty(locationLongitude)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Role", "Restaurant");
                jsonObject.put("Latitude", !TextUtils.isEmpty(locationLattitude) ? locationLattitude : "");
                jsonObject.put("Longitude", !TextUtils.isEmpty(locationLongitude) ? locationLongitude : "");
                showProgressDialog(getActivity());
                makeHttpCall(this, Apis.TOP_RATED_RESTAURANTS, getRetrofitInterface().getTopDiscountRestaurant(jsonObject.toString(), "Overall_Rating", "Restaurant"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivClearText)
    void OnClickivClearText() {
        etSearch.setText("");
        ivClearText.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeTabActivity)getActivity()).updateState();
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());
        switch (url) {
            case Apis.TOP_RATED_RESTAURANTS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        RestaurantModel restaurantModel = new Gson().fromJson(responce, RestaurantModel.class);
                        searchBestDishesAdapter = new SearchBestDishesAdapter(getActivity(), restaurantModel);
                        rvSearchListData.setAdapter(searchBestDishesAdapter);
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
        rlSearchList.setVisibility(isAval ? View.VISIBLE : View.GONE);
    }
}
