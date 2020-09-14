package com.valle.foodieapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.base.BaseActivity;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.utils.CommonUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements NetworkResponceListener {

    @BindView(R.id.etPhoneNumber)
    AppCompatEditText etPhoneNumber;

    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;

    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;

    @BindView(R.id.etConfirmPassword)
    AppCompatEditText etConfirmPassword;

    @BindView(R.id.ivPasswordVisi)
    AppCompatImageView ivPasswordVisi;

    @BindView(R.id.ivConfPassVisi)
    AppCompatImageView ivConfPassVisi;

    @BindView(R.id.rlMainRegister)
    RelativeLayout rlMainRegister;

    @BindView(R.id.etName)
    AppCompatEditText etName;

    private String Yes = "Y";
    private String No = "N";

    @OnClick(R.id.ivPasswordVisi)
    void OnClickivPasswordVisibility() {
        if (etPassword.getTag().toString().equalsIgnoreCase(Yes)) {
            etPassword.setTag(No);
            etPassword.setTransformationMethod(null);
            ivPasswordVisi.setImageDrawable(getResources().getDrawable(R.drawable.pass_show));
        } else {
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
            ivPasswordVisi.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
            etPassword.setTag(Yes);
        }
    }

    @OnClick(R.id.ivConfPassVisi)
    void OnClickivConfPassVisi() {
        if (etConfirmPassword.getTag().toString().equalsIgnoreCase(Yes)) {
            etConfirmPassword.setTag(No);
            etConfirmPassword.setTransformationMethod(null);
            ivConfPassVisi.setImageDrawable(getResources().getDrawable(R.drawable.pass_show));
        } else {
            etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            ivConfPassVisi.setImageDrawable(getResources().getDrawable(R.drawable.pass_hide));
            etConfirmPassword.setTag(Yes);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView(this);
        etPassword.setTag(No);
        etConfirmPassword.setTag(No);
    }

    @OnClick(R.id.tvSignup)
    void OnClicktvDeliveryBoySignup() {

        if (!CommonUtils.isNetworkAvailable(RegisterActivity.this)) {
            showSnakBar(rlMainRegister);
            return;
        }

        String phoneNumber = etPhoneNumber.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confPassword = etConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError(getResources().getString(R.string.plz_enter_phone_number));
            etPhoneNumber.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(name)) {
            etName.setError(getResources().getString(R.string.plz_enter_your_name));
            etName.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getResources().getString(R.string.plz_enter_your_email));
            etEmail.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.plz_enter_your_password));
            etPassword.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(confPassword)) {
            etConfirmPassword.setError(getResources().getString(R.string.plz_enter_conf_password));
            etConfirmPassword.setFocusable(true);
            return;
        }

        if (!password.equalsIgnoreCase(confPassword)) {
            etConfirmPassword.setError(getResources().getString(R.string.password_not_matched));
            etConfirmPassword.setFocusable(true);
            return;
        }

        showProgressDialog(RegisterActivity.this);
        makeHttpCall(RegisterActivity.this, Apis.REGISTER, getRetrofitInterface().register(name, phoneNumber, email, password, "", "Customer"));
    }

    @OnClick(R.id.tvAlreadyHaveAAccount)
    void OnClicktvAlreadyHaveAAccount() {
        super.onBackPressed();
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(RegisterActivity.this);

        switch (url) {
            case Apis.REGISTER:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        Intent intent = new Intent(RegisterActivity.this, OTPVerificationActivity.class);
                        intent.putExtra("responce", responce);
                        startActivity(intent);
                    } else if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.ERROR)) {
                        new AlertDialog.Builder(RegisterActivity.this).setTitle(getResources().getString(R.string.alert)).setMessage(jsonObject.getString("response").toLowerCase().contains("email") ? "ID de correo electrónico ya registrado. Por favor, elija otro" : jsonObject.getString("response").toLowerCase().contains("mobile") ? "Número de móvil ya registrado, elija otro" : jsonObject.getString("response")).setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss()).create().show();
                        /* Toast.makeText(RegisterActivity.this, jsonObject.getString("response"), Toast.LENGTH_SHORT).show();*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        hideProgressDialog(RegisterActivity.this);
        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
}
