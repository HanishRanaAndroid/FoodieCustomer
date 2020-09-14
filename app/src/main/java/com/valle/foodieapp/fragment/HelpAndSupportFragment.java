package com.valle.foodieapp.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpAndSupportFragment extends BaseFragment implements NetworkResponceListener {

    @BindView(R.id.tvOrderNumber)
    AppCompatTextView tvOrderNumber;

    @BindView(R.id.etComplaintQuery)
    AppCompatEditText etComplaintQuery;

    @BindView(R.id.llMain)
    LinearLayoutCompat llMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_and_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        tvOrderNumber.setText("Número de orden: " + getArguments().getString("order_number"));
    }

    @OnClick(R.id.btSubmitComplaint)
    void OnClickbtSubmitComplaint() {

        String complaint = etComplaintQuery.getText().toString();

        if (TextUtils.isEmpty(complaint)) {
            etComplaintQuery.setError("Por favor ingrese su consulta");
            etComplaintQuery.setFocusable(true);
            return;
        }

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar(llMain);
            return;
        }

        String userId = new SharedPrefModule(getActivity()).getUserId();
        showProgressDialog(getActivity());
        makeHttpCall(this, Apis.SUBMIT_TICKET, getRetrofitInterface().submitQuery(userId, "Customer", getArguments().getString("order_id"), complaint));

    }

    /*{
        "status": "success",
            "response": {
        "msg": "",
                "Items": 7,
                "Ticket": {
            "User_Id": "95",
                    "Order_Id": "1",
                    "Token_Number": "9517916",
                    "Type": "Customer",
                    "Message": "sdfsdfs",
                    "Status": "Pending",
                    "Action": "Pending"
        }
    }
    }
    */
    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {
            case Apis.SUBMIT_TICKET:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        String tokenNumber = jsonObject.getJSONObject("response").getJSONObject("Ticket").getString("Token_Number");

                        new AlertDialog.Builder(getActivity()).setTitle("Atencion").setMessage("Su numero de token es " + tokenNumber + "\nLa asistencia de Valle Food lo llamará muy pronto.").setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> {
                            dialog.dismiss();
                            getActivity().onBackPressed();
                        }).create().show();

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
