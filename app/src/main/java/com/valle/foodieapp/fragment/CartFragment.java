package com.valle.foodieapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.adapter.CartItemsListAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.SelectAddressInterface;
import com.valle.foodieapp.listeners.UpdateCartListener;
import com.valle.foodieapp.models.AddressModel;
import com.valle.foodieapp.models.ApplyCouponCodeModel;
import com.valle.foodieapp.models.CartModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CartFragment extends BaseFragment implements SelectAddressInterface, NetworkResponceListener, UpdateCartListener {

    @BindView(R.id.rvCartItems)
    RecyclerView rvCartItems;

    @BindView(R.id.tvAddressSelected)
    AppCompatTextView tvAddressSelected;

    @BindView(R.id.tvItemsTotal)
    AppCompatTextView tvItemsTotal;

    @BindView(R.id.tvDeliveryFees)
    AppCompatTextView tvDeliveryFees;

    @BindView(R.id.tvServiceTax)
    AppCompatTextView tvServiceTax;

    @BindView(R.id.rlDiscount)
    RelativeLayout rlDiscount;

    @BindView(R.id.tvDiscount)
    AppCompatTextView tvDiscount;

    @BindView(R.id.tvGrandTotal)
    AppCompatTextView tvGrandTotal;

    private CartModel cartModel;
    private ApplyCouponCodeModel applyCouponCodeModel;

    @BindView(R.id.flCustomNote)
    FrameLayout flCustomNote;

    @BindView(R.id.tvNote)
    AppCompatTextView tvNote;

    @BindView(R.id.tvRemoveCoupon)
    AppCompatTextView tvRemoveCoupon;

    @BindView(R.id.nsMainView)
    NestedScrollView nsMainView;

    private AddressModel.responseData.ItemsData itemsData;
    private String customNote = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCartItems.setLayoutManager(verticalLayoutManager);

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        callCartApi();
        callAddressListApi();
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(nsMainView, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        callCartApi();
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

    @OnClick(R.id.ivDeleteNote)
    void OnClickivDeleteNote() {
        tvNote.setText("");
        flCustomNote.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvAddCustomNote)
    void oClicktvAddCustomNote() {
        OpenCustomNote(getActivity());
    }

    @OnClick(R.id.tvAnyPromoCode)
    void oClicktvAnyPromoCode() {
        OpenPromoCode(getActivity());
    }

    @OnClick(R.id.tvAddAddress)
    void oClicktvAddAddress() {
        ((HomeTabActivity) getActivity()).addFragment(new AddAddressFragment(this));
    }

    @OnClick(R.id.tvSelectAddress)
    void oClicktvSelectAddress() {
        ((HomeTabActivity) getActivity()).addFragment(new SelectAddressFragment(this));
    }

    @OnClick(R.id.tvSContinueToPayment)
    void OnClicktvSContinueToPayment() {

        if (itemsData == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.plz_add_delivery_address), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int amount = Integer.parseInt(tvGrandTotal.getText().toString().split("\\$")[1]);
            if (amount < 10000) {
                Toast.makeText(getActivity(), "El monto del pedido debe ser igual a 10000", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString("Rest_Id", cartModel.response.Items.get(0).Rest_Id);
        String makeOrderResponse = makeOrderResponse();
        bundle.putString("Ordered_Items", makeOrderResponse);
        bundle.putString("Grand_Total", tvGrandTotal.getText().toString().split("\\$")[1]);
        bundle.putString("Custom_Note", customNote);
        ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new PaymentScreenFragment(), bundle);
    }

    private void callCartApi() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            if (itemsData != null) {
                jsonObject.put("Cust_Lat", itemsData.Latitude);
                jsonObject.put("Cust_Long", itemsData.Longitude);
            }
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.LIST_CART, getRetrofitInterface().getCartList(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenCustomNote(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_custom_note);
        dialog.setCancelable(true);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        AppCompatEditText etCustomNote = dialog.findViewById(R.id.etCustomNote);
        AppCompatTextView tvSubmitNote = dialog.findViewById(R.id.tvSubmitNote);

        tvSubmitNote.setOnClickListener(v -> {

            String note = etCustomNote.getText().toString();

            if (TextUtils.isEmpty(note)) {
                etCustomNote.setError(getResources().getString(R.string.plz_write_your_note));
                etCustomNote.setFocusable(true);
                return;
            }

            flCustomNote.setVisibility(View.VISIBLE);
            tvNote.setText(getResources().getString(R.string.note) + note);
            customNote = note;
            dialog.dismiss();

        });

        dialog.show();
    }

    private void OpenPromoCode(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_layout_promocode);
        dialog.setCancelable(true);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        AppCompatEditText etPromocode = dialog.findViewById(R.id.etPromocode);
        AppCompatTextView etSubmit = dialog.findViewById(R.id.etSubmit);

        etSubmit.setOnClickListener(v -> {
            String promocode = etPromocode.getText().toString();
            if (TextUtils.isEmpty(promocode)) {
                etPromocode.setError(getResources().getString(R.string.plz_entr_promo));
                etPromocode.setFocusable(true);
                return;
            }

            applyCouponCode(promocode);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void OnAddressSelected(AddressModel.responseData.ItemsData itemsData) {
        this.itemsData = itemsData;
        tvAddressSelected.setText(itemsData.Address2);
        String Address_Id = itemsData.Id;
        String Address1 = itemsData.Address1;
        String Address2 = itemsData.Address2;
        String Landmark = itemsData.Landmark;
        String Type = itemsData.Type;
        String Latitude = itemsData.Latitude;
        String Longitude = itemsData.Longitude;

        String Is_Primary = "";

        if (!TextUtils.isEmpty(Latitude) && !TextUtils.isEmpty(Longitude)) {
            Is_Primary = "1";
        } else {
            Is_Primary = "0";
        }

        String customerId = new SharedPrefModule(getActivity()).getUserId();
        showProgressDialog(getActivity());
        makeHttpCall(this, Apis.UPDATE_ADDRESS, getRetrofitInterface().updateAddress(Address_Id, Address1, Address2, Landmark, Type, Latitude, Longitude, Is_Primary, customerId));
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Cust_Lat", !TextUtils.isEmpty(Latitude) ? Latitude : "");
            jsonObject.put("Cust_Long", !TextUtils.isEmpty(Longitude) ? Longitude : "");
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.LIST_CART, getRetrofitInterface().getCartList(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyCouponCode(String coupon) {
        try {

            String restId = cartModel.response.Items.get(0).Rest_Id;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("User_Id", restId);
            jsonObject.put("Discount_Code", coupon);

            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.TOP_DISCOUNTED_RESTAURANT, getRetrofitInterface().applyCounponCode(jsonObject.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAddressListApi() {
        try {
            String userId = new SharedPrefModule(getActivity()).getUserId();
            makeHttpCall(this, Apis.LIST_ADDRESS, getRetrofitInterface().getAddressList(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());

        switch (url) {

            case Apis.LIST_CART:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        cartModel = new Gson().fromJson(responce, CartModel.class);
                        if (cartModel.response.Items.size() > 0) {
                            setData();
                        } else {
                            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new HomeScreenFragment(), null);
                        }
                    }
                    else {
                        ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new HomeScreenFragment(), null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.UPDATE_CART:

            case Apis.DELETE_CART:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        callCartApi();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.UPDATE_ADDRESS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS))  {


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.TOP_DISCOUNTED_RESTAURANT:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        applyCouponCodeModel = new Gson().fromJson(responce, ApplyCouponCodeModel.class);

                        String discount_percentage = applyCouponCodeModel.response.response.get(0).Discount_Percentage;
                        if (!TextUtils.isEmpty(discount_percentage)) {
                            int total = 0;
                            for (int i = 0; i < cartModel.response.Items.size(); i++) {
                                total = total + (Integer.parseInt(cartModel.response.Items.get(i).Item_Details.get(0).Item_Price) * Integer.parseInt(cartModel.response.Items.get(i).Item_Quantity));
                            }
                            tvDiscount.setText("");
                            double discount = (total * Integer.parseInt(discount_percentage)) / 100;
                            tvDiscount.append(CommonUtils.getColoredString(applyCouponCodeModel.response.response.get(0).Discount_Code, getResources().getColor(R.color.green)));
                            tvDiscount.append("  -$" + discount);
                            double grandTotal = (total + Integer.parseInt(cartModel.response.Other_Charges.Delivery_Charges) + ((total / 100) * (Integer.parseInt(cartModel.response.Other_Charges.Service_Tax)))) - discount;
                            tvGrandTotal.setText(getResources().getString(R.string.to_pay) + grandTotal);
                            tvRemoveCoupon.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.plz_enter_valid_copon_code), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.LIST_ADDRESS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        AddressModel addressModel = new Gson().fromJson(responce, AddressModel.class);
                        for (int i = 0; i < addressModel.response.Items.size(); i++) {
                            if (addressModel.response.Items.get(i).Is_Primary.equalsIgnoreCase("1")) {
                                tvAddressSelected.setText(addressModel.response.Items.get(i).Address2);
                                itemsData = addressModel.response.Items.get(i);
                                break;
                            }
                        }

                        try {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
                            jsonObject1.put("Cust_Lat", itemsData.Latitude);
                            jsonObject1.put("Cust_Long", itemsData.Longitude);
                            showProgressDialog(getActivity());
                            makeHttpCall(CartFragment.this, Apis.LIST_CART, getRetrofitInterface().getCartList(jsonObject1.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
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
        hideProgressDialog(getActivity());
    }

    private void setData() {
        if (cartModel == null) {
            return;
        }

        tvDeliveryFees.setText(!TextUtils.isEmpty(cartModel.response.Other_Charges.Delivery_Charges) ? "$" + cartModel.response.Other_Charges.Delivery_Charges : "Calculadora");

        int total = 0;
        for (int i = 0; i < cartModel.response.Items.size(); i++) {
            total = total + (Integer.parseInt(cartModel.response.Items.get(i).Item_Details.get(0).Item_Price) * Integer.parseInt(cartModel.response.Items.get(i).Item_Quantity));
        }

        tvItemsTotal.setText("$" + total);

        tvServiceTax.setText("$" + ((total / 100) * (Integer.parseInt(cartModel.response.Other_Charges.Service_Tax))));

        if (applyCouponCodeModel != null) {
            String discount_percentage = applyCouponCodeModel.response.response.get(0).Discount_Percentage;
            if (!TextUtils.isEmpty(discount_percentage)) {
                tvDiscount.setText("");
                double discount = (total * Integer.parseInt(discount_percentage)) / 100;
                tvDiscount.append(CommonUtils.getColoredString(applyCouponCodeModel.response.response.get(0).Discount_Code, getResources().getColor(R.color.green)));
                tvDiscount.append("  -$" + discount);
                double grandTotal = (total + Integer.parseInt(cartModel.response.Other_Charges.Delivery_Charges) + ((total / 100) * (Integer.parseInt(cartModel.response.Other_Charges.Service_Tax)))) - discount;
                tvGrandTotal.setText(getResources().getString(R.string.to_pay) + grandTotal);
                tvRemoveCoupon.setVisibility(View.VISIBLE);
            }
        } else {
            int grandTotal = total + Integer.parseInt(cartModel.response.Other_Charges.Delivery_Charges) + ((total / 100) * (Integer.parseInt(cartModel.response.Other_Charges.Service_Tax)));
            tvGrandTotal.setText(getResources().getString(R.string.to_pay) + grandTotal);
        }

        CartItemsListAdapter cartItemsListAdapter = new CartItemsListAdapter(getActivity(), cartModel.response.Items, this);
        rvCartItems.setAdapter(cartItemsListAdapter);

    }

    @OnClick(R.id.tvRemoveCoupon)
    void OnClicktvRemoveCoupon() {
        if (cartModel == null) {
            return;
        }

        int total = 0;
        for (int i = 0; i < cartModel.response.Items.size(); i++) {
            total = total + (Integer.parseInt(cartModel.response.Items.get(i).Item_Details.get(0).Item_Price) * Integer.parseInt(cartModel.response.Items.get(i).Item_Quantity));
        }
        int grandTotal = total + Integer.parseInt(cartModel.response.Other_Charges.Delivery_Charges) + ((total / 100) * (Integer.parseInt(cartModel.response.Other_Charges.Service_Tax)));
        tvGrandTotal.setText(getResources().getString(R.string.to_pay) + grandTotal);
        tvRemoveCoupon.setVisibility(View.GONE);
        tvDiscount.setText("$0");
    }

    @Override
    public void updateCart(CartModel.responseData.ItemsList itemsList, String quantity) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Rest_Id", itemsList.Rest_Id);
            jsonObject.put("Item_Quantity", quantity);
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.UPDATE_CART, getRetrofitInterface().updateCart(jsonObject.toString(), itemsList.Id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveItem(CartModel.responseData.ItemsList itemsList) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", itemsList.Id);
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.UPDATE_CART, getRetrofitInterface().deleteCart(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeOrderResponse() {
        if (cartModel == null) {
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();

            List<CartModel.responseData.ItemsList> items = cartModel.response.Items;
            for (int i = 0; i < items.size(); i++) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("Id", items.get(i).Item_Id);
                jsonObject1.put("Quantity", items.get(i).Item_Quantity);
                jsonObject1.put("Item_Name", items.get(i).Item_Details.get(0).Item_Name);
                jsonObject1.put("Item_Category", items.get(i).Item_Details.get(0).Item_Category);
                jsonObject1.put("Item_Price", items.get(i).Item_Details.get(0).Item_Price);
                jsonObject1.put("Item_Flavor_Type", items.get(i).Item_Flavor_Type);
                jsonObject1.put("Item_Image", items.get(i).Item_Details.get(0).Item_Image);
                jsonArray.put(jsonObject1);
            }

            jsonObject.put("Items", jsonArray);

            JSONObject addressJson = new JSONObject();
            if (itemsData != null) {
                addressJson.put("Map_Address", itemsData.Address1);
                addressJson.put("Address", itemsData.Address2);
                addressJson.put("Delivery_Lat", itemsData.Latitude);
                addressJson.put("Delivery_Long", itemsData.Longitude);
            }

            jsonObject.put("Delivery_Address", addressJson);
            jsonObject.put("Tax", tvServiceTax.getText().toString().split("\\$")[1]);
            jsonObject.put("Delivery_Charges", cartModel.response.Other_Charges.Delivery_Charges);

            if (applyCouponCodeModel != null) {
                String tt = tvDiscount.getText().toString().split("\\$")[1];
                jsonObject.put("Promo_Code", Double.valueOf(tt) > 0 ? applyCouponCodeModel.response.response.get(0).Discount_Code : "");
                jsonObject.put("Discount_Amount", tvDiscount.getText().toString().split("\\$")[1]);
                jsonObject.put("Grand_Total", tvGrandTotal.getText().toString().split("\\$")[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
