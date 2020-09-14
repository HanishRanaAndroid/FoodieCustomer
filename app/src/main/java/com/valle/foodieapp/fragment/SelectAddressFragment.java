package com.valle.foodieapp.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.adapter.AddressListAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.SelectAddressInterface;
import com.valle.foodieapp.models.AddressModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectAddressFragment extends BaseFragment implements SelectAddressInterface, NetworkResponceListener {

    @BindView(R.id.rvListAddress)
    RecyclerView rvListAddress;

    @BindView(R.id.llMainSelectedAdd)
    LinearLayoutCompat llMainSelectedAdd;

    private SelectAddressInterface selectAddressInterface;

    public SelectAddressFragment(SelectAddressInterface selectAddressInterface) {
        this.selectAddressInterface = selectAddressInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvListAddress.setLayoutManager(verticalLayoutManager);

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        callAddressListApi();
    }

    private void callAddressListApi() {
        try {
            showProgressDialog(getActivity());
            String userId = new SharedPrefModule(getActivity()).getUserId();
            makeHttpCall(this, Apis.LIST_ADDRESS, getRetrofitInterface().getAddressList(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(llMainSelectedAdd, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        callAddressListApi();
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

    @OnClick(R.id.tvContinue)
    void OnClicktvContinue() {
        // ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new PaymentScreenFragment(), null);
    }

    @Override
    public void OnAddressSelected(AddressModel.responseData.ItemsData itemsData) {
        selectAddressInterface.OnAddressSelected(itemsData);
        ((HomeTabActivity) getActivity()).removeFragment();
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {

            case Apis.LIST_ADDRESS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        AddressModel addressModel = new Gson().fromJson(responce, AddressModel.class);
                        AddressListAdapter addressListAdapter = new AddressListAdapter(getActivity(), addressModel.response.Items, this);
                        rvListAddress.setAdapter(addressListAdapter);
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
