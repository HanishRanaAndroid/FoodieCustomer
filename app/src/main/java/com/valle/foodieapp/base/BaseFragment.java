package com.valle.foodieapp.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.valle.foodieapp.R;
import com.valle.foodieapp.network.APIClient;
import com.valle.foodieapp.network.APIClientForPayment;
import com.valle.foodieapp.network.APIInterface;
import com.valle.foodieapp.network.NetworkManager;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.utils.APIClientUpdateImage;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.valle.foodieapp.utils.NetworkManagerToUploadImageData;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {

    public ProgressDialog progressDialog;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void bindView(Fragment activity, View view) {
        unbinder = ButterKnife.bind(activity, view);
    }

    public void unBindView() {
        unbinder.unbind();
    }

    public void showProgressDialog(Context activity) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("por favor espera...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideProgressDialog(Context context) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void makeHttpCall(NetworkResponceListener context, String url, Object o) {
        new NetworkManager(context).getNetworkResponce(url, o);
    }

    public APIInterface getRetrofitInterface() {
        return APIClient.getClient().create(APIInterface.class);
    }

    public APIInterface getRetrofitInterfaceForPayment() {
        return APIClientForPayment.getClient().create(APIInterface.class);
    }

    public void makeHttpCallToUploadImageData(NetworkResponceListener context, String url, Object o) {
        new NetworkManagerToUploadImageData(context).getNetworkResponce(url, o);
    }

    public APIInterface getRetrofitInterfaceToUploadImageData() {
        return APIClientUpdateImage.getClient().create(APIInterface.class);
    }

    public void showSnakBar(View viewLayout) {
        Snackbar snackbar = Snackbar
                .make(viewLayout, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showSnakBar(viewLayout);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

}
