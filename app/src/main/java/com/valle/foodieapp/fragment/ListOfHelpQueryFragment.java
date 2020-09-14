package com.valle.foodieapp.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.valle.foodieapp.R;
import com.valle.foodieapp.adapter.AdapterListQuery;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.ListQueryModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.utils.CommonUtils;

import org.json.JSONObject;

import butterknife.BindView;

public class ListOfHelpQueryFragment extends BaseFragment implements NetworkResponceListener {

    @BindView(R.id.rvQuery)
    RecyclerView rvQuery;

    @BindView(R.id.tvQueryNotFound)
    AppCompatTextView tvQueryNotFound;

    @BindView(R.id.llMain)
    LinearLayoutCompat llMain;

    private AdapterListQuery adapterListQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_help_query, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvQuery.setLayoutManager(verticalLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar(llMain);
            return;
        }
        showProgressDialog(getActivity());
        makeHttpCall(this, Apis.LIST_TICKET, getRetrofitInterface().getQuery());
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());
        switch (url) {
            case Apis.LIST_TICKET:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        ListQueryModel listQueryModel = new Gson().fromJson(responce, ListQueryModel.class);
                        adapterListQuery=new AdapterListQuery(getActivity(),listQueryModel.response.Items);
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
