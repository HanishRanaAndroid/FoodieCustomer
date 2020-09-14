package com.valle.foodieapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.valle.foodieapp.R;
import com.valle.foodieapp.base.BaseActivity;
import com.valle.foodieapp.models.ForgetPasswordModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class NewPasswordActivity extends BaseActivity implements NetworkResponceListener {

    @BindView(R.id.etNewPassword)
    AppCompatEditText etNewPassword;
    @BindView(R.id.etConfPassword)
    AppCompatEditText etConfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword);
        bindView(this);
    }

    @OnClick(R.id.tvChnagePassword)
    void OnClicktvChnagePassword() {

        String newPassword = etNewPassword.getText().toString();
        String confPassword = etConfPassword.getText().toString();


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

        showProgressDialog(NewPasswordActivity.this);
        makeHttpCall(this, Apis.FORGET_PASSWORD, getRetrofitInterface().forgetPassword(getIntent().getStringExtra("phoneNumber"), newPassword));
    }

    @Override
    public void onSuccess(String url, String responce) {
       hideProgressDialog(NewPasswordActivity.this);

        switch (url) {

            case Apis.FORGET_PASSWORD:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        ForgetPasswordModel forgetPasswordModel = new Gson().fromJson(responce, ForgetPasswordModel.class);
                        Toast.makeText(NewPasswordActivity.this, forgetPasswordModel.response.msg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(NewPasswordActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        hideProgressDialog(NewPasswordActivity.this);
    }
}
