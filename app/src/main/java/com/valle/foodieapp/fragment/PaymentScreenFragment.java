package com.valle.foodieapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.application.BaseApplication;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.PayUMoneyTransactionListener;
import com.valle.foodieapp.listeners.WompiSuccessListner;
import com.valle.foodieapp.models.CreateOrderModel;
import com.valle.foodieapp.models.GetPaymentLinkModel;
import com.valle.foodieapp.models.LoginModel;
import com.valle.foodieapp.models.OrderPaymentSuccessfulModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.AppEnvironment;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.valle.foodieapp.utils.CommonUtils.getCurrentDate;
import static com.valle.foodieapp.utils.CommonUtils.getCurrentTime;

public class PaymentScreenFragment extends BaseFragment implements NetworkResponceListener, PayUMoneyTransactionListener, WompiSuccessListner {

    @BindView(R.id.ivCash)
    AppCompatImageView ivCash;

    @BindView(R.id.ivOnline)
    AppCompatImageView ivOnline;

    @BindView(R.id.tvOrderAmount)
    AppCompatTextView tvOrderAmount;

    @BindView(R.id.tvCashGrandTotal)
    AppCompatTextView tvCashGrandTotal;

    @BindView(R.id.tvTransactionFeesCharges)
    AppCompatTextView tvTransactionFeesCharges;

    @BindView(R.id.tvWompiGrandTotal)
    AppCompatTextView tvWompiGrandTotal;

    @BindView(R.id.rlMainPayment)
    RelativeLayout rlMainPayment;

    private Timer timer;

    private String PaymentStatus = "";
    private String PaymentType = "";

    private final String CASH = "Cash";
    private final String ONLINE = "Online";

    private double wompiAmount = 0;
    private double codAmount = 0;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    private JSONObject jsonObject = new JSONObject();
    private String OrderNumber = "";
    private CreateOrderModel createOrderModel;

    @BindView(R.id.progressBar)
    CircleProgressBar circleProgressBar;

    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

//Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("XYZ");

//Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle(getResources().getString(R.string.app_name));

        payUmoneyConfig.disableExitConfirmation(false);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;

        try {
            amount = Double.parseDouble(getArguments().getString("Grand_Total"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";

//String txnId = "TXNID720431525261327973";
        String userLoginResponseData = new SharedPrefModule(getActivity()).getUserLoginResponseData();
        LoginModel.responseData.UserInfoData loginModel = new Gson().fromJson(userLoginResponseData, LoginModel.responseData.UserInfoData.class);

        String phone = loginModel.Mobile;
        String productName = "Valle food";
        String firstName = !TextUtils.isEmpty(loginModel.Full_Name) ? loginModel.Full_Name : "";
        String email = !TextUtils.isEmpty(loginModel.Email) ? loginModel.Email : "xyz@gmail.com";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";
        AppEnvironment appEnvironment = ((BaseApplication) getActivity().getApplication()).getAppEnvironment();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
// generateHashFromServer(mPaymentParams);

            /* *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, getActivity(), R.style.AppTheme_pink, true);
        } catch (Exception e) {
// some exception occurred

        }
    }

///////////////////////////////////////////////////

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((BaseApplication) getActivity().getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);
        return paymentParam;
    }

/////////////////////////////////////////////////////////////

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

// Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

// Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
//Success Transaction
                } else {
//Failure Transaction
                }

// Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

// Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();



                *//*new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*//*

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("", "Both objects are null!");
            }
        }
    }*/


    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (Exception ignored) {
        }
        return hexString.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_screen, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        String grandTotal = getArguments().getString("Grand_Total");
        tvOrderAmount.setText(!TextUtils.isEmpty(grandTotal) ? getResources().getString(R.string.order_amount) + grandTotal : "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        circleProgressBar.setShowArrow(true);
        circleProgressBar.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        circleProgressBar.setCircleBackgroundEnabled(true);
        ((HomeTabActivity) getActivity()).setPayUMoenyTransactionListner(this);
        int i = (int) (Math.random() * ((2) + 1));
        @SuppressLint("DefaultLocale") String id = String.format("%04d", new Random().nextInt(10000));
        OrderNumber = "ON" + new SharedPrefModule(getActivity()).getUserId() + getArguments().getString("Rest_Id") + id;
        setData();
    }

    private void setData() {
        double orderAmount = Double.valueOf(getArguments().getString("Grand_Total"));
        double wompiTransactionFee = (orderAmount / 100) * 3;
        wompiAmount = orderAmount + wompiTransactionFee;
        tvCashGrandTotal.setText(getResources().getString(R.string.grand_total) + orderAmount);
        tvTransactionFeesCharges.setText(getResources().getString(R.string.trans_fess) + wompiTransactionFee);
        tvWompiGrandTotal.setText(getResources().getString(R.string.grand_total) + wompiAmount);
    }

    @OnClick(R.id.rlCashOnDelivery)
    void OnClickrlCashOnDelivery() {
        ivCash.setVisibility(View.VISIBLE);
        ivOnline.setVisibility(View.GONE);
        PaymentStatus = "UnPaid";
        PaymentType = CASH;
    }

    @OnClick(R.id.rlOnline)
    void OnClickrlOnline() {
        ivCash.setVisibility(View.GONE);
        ivOnline.setVisibility(View.VISIBLE);
        PaymentStatus = "UnPaid";
        PaymentType = ONLINE;
    }

    @OnClick(R.id.tvSContinueToPlaceOrder)
    void OnClicktvSContinueToPlaceOrder() {

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar(rlMainPayment);
            return;
        }

        if (TextUtils.isEmpty(PaymentType)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.plz_chose_payment_type), Toast.LENGTH_SHORT).show();
            return;
        }

        // if (PaymentType.equalsIgnoreCase(CASH)) {
        String userId = new SharedPrefModule(getActivity()).getUserId();
        String restId = getArguments().getString("Rest_Id");
        String OrderedItems = getArguments().getString("Ordered_Items");
        String GrandTotal = PaymentType.equalsIgnoreCase(CASH) ? getArguments().getString("Grand_Total") : String.valueOf(wompiAmount);
        String customNote = getArguments().getString("Custom_Note");
        circleProgressBar.setVisibility(View.VISIBLE);
        //showProgressDialog(getActivity());
        makeHttpCall(this, Apis.CREATE_ORDER, getRetrofitInterface().createOrder(userId, restId, OrderedItems, GrandTotal, PaymentStatus, PaymentType, jsonObject.toString(), customNote, OrderNumber));
        /*} else {
            launchPayUMoneyFlow();
        }*/
    }

    @Override
    public void onSuccess(String url, String responce) {
        switch (url) {
            case Apis.CREATE_ORDER:
                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        createOrderModel = new Gson().fromJson(responce, CreateOrderModel.class);
                        if (!PaymentType.equalsIgnoreCase(CASH)) {
                            try {
                                JSONObject jsonObject1 = new JSONObject();
                                assert getArguments() != null;
                                double amount = wompiAmount * 100;
                                jsonObject1.put("amount_in_cents", amount);
                                jsonObject1.put("currency", "COP");
                                jsonObject1.put("name", "Valle Food");
                                jsonObject1.put("description", "Order Number" + OrderNumber);
                                //  jsonObject1.put("expires_at", getCurrentDate() + "T" + getCurrentTime() + "Z");
                                jsonObject1.put("single_use", false);
                                jsonObject1.put("redirect_url", "http://weburlforclients.com/gamma/vallafood/api/v1/PaymentCallback/" + OrderNumber);
                                jsonObject1.put("collect_shipping", false);
                                // showProgressDialog(getActivity());
                                makeHttpCall(this, Apis.GET_PAYMENT_LINK, getRetrofitInterfaceForPayment().getPaymentLink(jsonObject1.toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            circleProgressBar.setVisibility(View.GONE);
                            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new OrderPlacedAlertFragment(createOrderModel), null);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.GET_PAYMENT_LINK:

                try {
                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getJSONObject("data").has("id")) {
                        GetPaymentLinkModel getPaymentLinkModel = new Gson().fromJson(responce, GetPaymentLinkModel.class);
                        circleProgressBar.setVisibility(View.GONE);
                        ((HomeTabActivity) getActivity()).addFragment(new WompiPaymentCheckoutFragment(getPaymentLinkModel, this));

                    } else {
                        circleProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case Apis.GET_ORDER_PAYMENT_STATUS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        circleProgressBar.setVisibility(View.GONE);
                        timer.cancel();
                        OrderPaymentSuccessfulModel orderPaymentSuccessfulModel = new Gson().fromJson(responce, OrderPaymentSuccessfulModel.class);
                        String status = orderPaymentSuccessfulModel.response.orders_Info.get(0).Payment_History.status;
                        if (status.equalsIgnoreCase("APPROVED")) {
                            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new OrderPlacedAlertFragment(createOrderModel), null);
                        } else {
                            circleProgressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(getActivity()).setTitle(status).setMessage(getResources().getString(R.string.transaction_failed)).setPositiveButton(getString(R.string.retry_to_pay), (dialog, which) -> {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject1 = new JSONObject();
                                    assert getArguments() != null;
                                    double amount = wompiAmount * 100;
                                    jsonObject1.put("amount_in_cents", amount);
                                    jsonObject1.put("currency", "COP");
                                    jsonObject1.put("name", "Valle Food");
                                    jsonObject1.put("description", "Order Number" + OrderNumber);
                                    jsonObject1.put("expires_at", getCurrentDate() + "T" + getCurrentTime() + "Z");
                                    jsonObject1.put("single_use", false);
                                    jsonObject1.put("redirect_url", "http://weburlforclients.com/gamma/vallafood/api/v1/PaymentCallback/" + OrderNumber);
                                    jsonObject1.put("collect_shipping", false);
                                    // showProgressDialog(getActivity());
                                    circleProgressBar.setVisibility(View.VISIBLE);
                                    makeHttpCall(this, Apis.GET_PAYMENT_LINK, getRetrofitInterfaceForPayment().getPaymentLink(jsonObject1.toString()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                                dialog.dismiss();
                                ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new HomeScreenFragment(), null);
                            }).create().show();

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        circleProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSucessfullTransaction(String payuResponse, String merchantResponse) {
        try {
            PaymentStatus = "Paid";
            PaymentType = "Online";
            jsonObject.put("payuResponse", payuResponse);
            jsonObject.put("merchantResponse", merchantResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String userId = new SharedPrefModule(getActivity()).getUserId();
        String restId = getArguments().getString("Rest_Id");
        String OrderedItems = getArguments().getString("Ordered_Items");
        String GrandTotal = getArguments().getString("Grand_Total");
        String customNote = getArguments().getString("Custom_Note");
        circleProgressBar.setVisibility(View.VISIBLE);
        // showProgressDialog(getActivity());
        makeHttpCall(this, Apis.CREATE_ORDER, getRetrofitInterface().createOrder(userId, restId, OrderedItems, GrandTotal, PaymentStatus, PaymentType, jsonObject.toString(), customNote, OrderNumber));
    }

    @Override
    public void onFailureOfTransaction() {
        Toast.makeText(getActivity(), "Transaction Failed. Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentSuccess(String response) {
        circleProgressBar.setVisibility(View.VISIBLE);
        timer = new Timer();
        // showProgressDialog(getActivity());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Id", createOrderModel.response.orders_Info.Id);
                        jsonObject.put("Payment_History !=", ":");
                        makeHttpCall(PaymentScreenFragment.this, Apis.GET_ORDER_PAYMENT_STATUS, getRetrofitInterface().getPaymentStatus(jsonObject.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
        }, 0, 1000);
    }
}
