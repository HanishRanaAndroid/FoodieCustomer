package com.valle.foodieapp.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.valle.foodieapp.R;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.ChangePasswordModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseFragment implements NetworkResponceListener {

    @BindView(R.id.etOldPassword)
    AppCompatEditText etOldPassword;

    @BindView(R.id.etNewPassword)
    AppCompatEditText etNewPassword;

    @BindView(R.id.etConfPassword)
    AppCompatEditText etConfPassword;

    @BindView(R.id.rlNewPassword)
    RelativeLayout rlNewPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
    }

    @OnClick(R.id.tvChnagePassword)
    void OnClicktvChnagePassword() {

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar(rlNewPassword);
            return;
        }

        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confPassword = etConfPassword.getText().toString();

        if (TextUtils.isEmpty(oldPassword)) {
            etOldPassword.setError(getResources().getString(R.string.please_enter_your_old_password));
            etOldPassword.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError(getResources().getString(R.string.please_enter_new_password));
            etNewPassword.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(confPassword)) {
            etConfPassword.setError(getResources().getString(R.string.please_enter_conf_password));
            etConfPassword.setFocusable(true);
            return;
        }

        if (!newPassword.equalsIgnoreCase(confPassword)) {
            etConfPassword.setError(getResources().getString(R.string.password_not_matched));
            etConfPassword.setFocusable(true);
            return;
        }

        showProgressDialog(getActivity());
        String userId = new SharedPrefModule(getActivity()).getUserId();
        makeHttpCall(this, Apis.CHNAGE_PASSWORD, getRetrofitInterface().changePassword(userId, oldPassword, newPassword));

    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {

            case Apis.CHNAGE_PASSWORD:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        ChangePasswordModel changePasswordModel = new Gson().fromJson(responce, ChangePasswordModel.class);
                        Toast.makeText(getActivity(), changePasswordModel.response.msg, Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    private void clearFields() {
        etOldPassword.setText("");
        etNewPassword.setText("");
        etConfPassword.setText("");
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        hideProgressDialog(getActivity());
    }
}
