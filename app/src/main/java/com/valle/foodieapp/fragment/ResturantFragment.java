package com.valle.foodieapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.adapter.AdapterFlavour;
import com.valle.foodieapp.adapter.ResturantDishListAdapter;
import com.valle.foodieapp.adapter.ResturantDishesListAdapter;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.AddToCartListner;
import com.valle.foodieapp.models.CartModel;
import com.valle.foodieapp.models.MenuItemListModel;
import com.valle.foodieapp.models.RestaurantModel;
import com.valle.foodieapp.models.SecondSliderModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ResturantFragment extends BaseFragment implements NetworkResponceListener, AddToCartListner {

    @BindView(R.id.rvResturantItems)
    RecyclerView rvResturantItems;

    @BindView(R.id.tvRestDiscountPercentage)
    AppCompatTextView tvRestDiscountPercentage;

    @BindView(R.id.tvRestName)
    AppCompatTextView tvRestName;

    @BindView(R.id.tvOrderTime)
    AppCompatTextView tvOrderTime;

    @BindView(R.id.tvRestRating)
    AppCompatTextView tvRestRating;

    @BindView(R.id.tvPercentageOff)
    AppCompatTextView tvPercentageOff;

    @BindView(R.id.ivRestCoverImage)
    AppCompatImageView ivRestCoverImage;

    @BindView(R.id.rlMainRestaurant)
    RelativeLayout rlMainRestaurant;

    private RestaurantModel.responseData.responseList responseList;
    private SecondSliderModel.responseData responseListData;

    private MenuItemListModel menuItemListModel;
    private MenuItemListModel.responseData.ItemsData itemsDataRecom;
    private MenuItemListModel.responseData.ItemsData itemsData;
    private Timer timer;

    private boolean bPresenceOnOFF = true;

    private boolean restaurantResponse = true;

    public static String flavour = "";

    public final String FILTER = "Filter";
    public final String REQUEST_TYPE = "type";
    private Dialog flavorDialogView;


    public ResturantFragment(RestaurantModel.responseData.responseList responseList) {
        this.responseList = responseList;
    }

    public ResturantFragment(SecondSliderModel.responseData responseList) {
        this.responseListData = responseList;
        setResponseData(responseListData);
    }

    public void setResponseData(SecondSliderModel.responseData responseListData) {
        String response = "{\n" +
                "  \"status\": \"success\",\n" +
                "  \"response\": {\n" +
                "    \"msg\": \"Encontró\",\n" +
                "    \"response\": [\n" +
                "      {\n" +
                "        \"User_Id\": \"752\",\n" +
                "        \"Full_Name\": \"Valle Food \",\n" +
                "        \"Restaurant_Name\": \"Tester Rest Valle Food\",\n" +
                "        \"Email\": \"testervalle@testervalle.com\",\n" +
                "        \"Mobile\": \"3333333333\",\n" +
                "        \"Password\": \"$2y$10$jlrRxwnVrhEEjRHnkznMXuN7aV/Q6tf/zryqVN71PzDN1t3ADLmay\",\n" +
                "        \"City\": \"Chandigarh\",\n" +
                "        \"Address\": \"17C, Sector 17, Chandigarh, India\",\n" +
                "        \"Website\": \"testing.com\",\n" +
                "        \"Latitude\": \"30.7393517\",\n" +
                "        \"Longitude\": \"76.7848655\",\n" +
                "        \"Role\": \"Restaurant\",\n" +
                "        \"Login_Type\": \"Default\",\n" +
                "        \"Set_Your_Presence\": \"OFF\",\n" +
                "        \"Order_WithIN\": \"0\",\n" +
                "        \"Profile_Image\": \"resttt.jpg\",\n" +
                "        \"Cover_Image\": null,\n" +
                "        \"Overall_Rating\": \"3.0\",\n" +
                "        \"Discount_Code\": \"\",\n" +
                "        \"Discount_Percentage\": \"\",\n" +
                "        \"ForSlider\": \"0\",\n" +
                "        \"ForsliderTwo\": \"0\",\n" +
                "        \"Refer_Code\": \"VF9978\",\n" +
                "        \"Validation_Code\": \"2824\",\n" +
                "        \"Is_Active\": \"Active\",\n" +
                "        \"Device_Token\": \"fdp7toAhHwo:APA91bHi2vH7eJNpTPlmt4Dm89-yOfvc7Y_KS3HBOC6T737XfDUcH5LkAwhKWYUdgxyTFytyi4tBfryamHlyuRnrBLSk5cC1y01YIt79zJadR3KfMQAarT-eX8z7LgkQ6uqmpmKQta56\",\n" +
                "        \"createdDtm\": \"2020-02-17 00:21:02\",\n" +
                "        \"distance\": \"3.3912218513240444\",\n" +
                "        \"ItemCategory\": [\n" +
                "          {\n" +
                "            \"Item_Category\": \"ADICIONAL\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"Item_Category\": \"DESAYUNO\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        RestaurantModel responseListMain = new Gson().fromJson(response, RestaurantModel.class);
        responseList = responseListMain.response.response.get(0);
        this.responseList.User_Id = responseListData.rest_id;
        this.responseList.Discount_Code = responseListData.Discount_Code;
        this.responseList.Discount_Percentage = responseListData.Discount_Percentage;
        this.responseList.Order_WithIN = responseListData.Order_WithIN;
        this.responseList.Is_Active = responseListData.Is_Active;
        this.responseList.Overall_Rating = responseListData.Overall_Rating;
        this.responseList.Restaurant_Name = responseListData.Restaurant_Name;
        this.responseList.Cover_Image = responseListData.Cover_Image;
        this.responseList.Profile_Image = responseListData.Profile_Image;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantResponse = true;
        try {
            ((HomeTabActivity) getActivity()).updateCartData();

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> {
                        if (getActivity() == null) {
                            return;
                        }
                        if (CommonUtils.isNetworkAvailable(getActivity())) {
                            try {
                                if (restaurantResponse) {
                                    restaurantResponse = false;
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("User_Id", responseList.User_Id);

                                    makeHttpCall(ResturantFragment.this, Apis.TOP_DISCOUNTED_RESTAURANT, getRetrofitInterface().getRestaurantInfo(jsonObject.toString()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });
                }
            }, 0, 10000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CommonUtils.isNetworkAvailable(getActivity())) {
            makeHttpCall(this, Apis.ADMIN_CONTACT, getRetrofitInterface().getAdminContact());
        }
    }

    @OnClick(R.id.tvMenu)
    void OnClicktvMenu() {
        OpenMenuList(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resturant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(this, view);
        rvResturantItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        setData();

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar();
            return;
        }

        setMenuList();
    }

    private void setData() {

        if (responseList == null) {
            return;
        }

        tvRestDiscountPercentage.setText(!TextUtils.isEmpty(responseList.Discount_Percentage) ? "Descuento del " + responseList.Discount_Percentage + "%" : "");
        tvRestName.setText(responseList.Restaurant_Name);
        tvPercentageOff.setText(!TextUtils.isEmpty(responseList.Discount_Percentage) ? "Descuento " + responseList.Discount_Percentage + "%" : "");
        tvRestRating.setText(!TextUtils.isEmpty(responseList.Overall_Rating) ? "★" + responseList.Overall_Rating : "");
        tvOrderTime.setText(!TextUtils.isEmpty(responseList.Order_WithIN) ? responseList.Order_WithIN + " mins" : "30 mins");
        try {

            Glide.with(getActivity()).load(Apis.IMAGE_URL + responseList.Cover_Image).into(ivRestCoverImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSnakBar() {
        Snackbar snackbar = Snackbar
                .make(rlMainRestaurant, getResources().getString(R.string.internet_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.restry), view -> {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        setMenuList();
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

    private void setMenuList() {
        showProgressDialog(getActivity());
        makeHttpCall(this, Apis.ITEMS_LIST, getRetrofitInterface().getMenuList(responseList.User_Id));
    }

    private void OpenMenuList(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_menu);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        RecyclerView rvMenuList = dialog.findViewById(R.id.rvMenuList);
        AppCompatImageView ivClose = dialog.findViewById(R.id.ivClose);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvMenuList.setLayoutManager(verticalLayoutManager);

        if (menuItemListModel != null) {
            ResturantDishListAdapter resturantDishListAdapter = new ResturantDishListAdapter(getActivity(), menuItemListModel.response.Items, this);
            rvMenuList.setAdapter(resturantDishListAdapter);
        }

        ivClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void focusOnItem() {
        if (responseListData == null) {
            return;
        }
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());
        switch (url) {
            case Apis.ITEMS_LIST:

                if (TextUtils.isEmpty(responce)) {
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        try {

                            String string = getArguments().getString(REQUEST_TYPE);
                            if (string.equalsIgnoreCase(FILTER)) {

                                if (responseListData != null) {

                                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("Items");
                                    JSONObject object = new JSONObject();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if (responseListData.itemData.Id.equalsIgnoreCase(jsonArray.getJSONObject(i).getString("Id"))) {
                                            object = jsonArray.getJSONObject(i);
                                            jsonArray.remove(i);
                                        }
                                    }

                                    itemsDataRecom = new Gson().fromJson(object.toString(), MenuItemListModel.responseData.ItemsData.class);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        menuItemListModel = new Gson().fromJson(jsonObject.toString(), MenuItemListModel.class);
                        ResturantDishesListAdapter resturantDishListAdapter = new ResturantDishesListAdapter(getActivity(), menuItemListModel.response.Items, itemsDataRecom, this);
                        rvResturantItems.setAdapter(resturantDishListAdapter);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.ADD_TO_CART:

                if (TextUtils.isEmpty(responce)) {
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        flavour = "";
                        Toast.makeText(getActivity(), getResources().getString(R.string.item_added), Toast.LENGTH_SHORT).show();
                        ((HomeTabActivity) getActivity()).updateCartData();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.DELETE_CART:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        addToCartApiCall(itemsData);
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

                        bPresenceOnOFF = jsonObject.getJSONObject("response").getJSONArray("response").getJSONObject(0).getString("Set_Your_Presence").equalsIgnoreCase("ON");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                restaurantResponse = true;
                break;
        }
    }

    @Override
    public void onFailure(String url, Throwable throwable) {
        hideProgressDialog(getActivity());
    }

    @Override
    public void addTocart(MenuItemListModel.responseData.ItemsData itemsData) {
        //  flavourDialog(getActivity());
        if (!bPresenceOnOFF) {
            new AlertDialog.Builder(getActivity()).setTitle("Aviso").setMessage(responseList.Restaurant_Name + " no puede tomar orden en este momento debido a sus horarios de trabajo").setPositiveButton(getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss()).create().show();
            return;
        }

        this.itemsData = itemsData;
        /*{
            "Customer_Id":"88",
                "Rest_Id":"87",
                "Item_Id":"6",
                "Item_Quantity":"2"
        }*/

        try {

            CartModel cartModel = ((HomeTabActivity) getActivity()).getCartModel();
            if (cartModel != null) {
                boolean b = cartModel.response.Items.size() > 0;
                if (b) {
                    boolean b1 = cartModel.response.Items.get(0).Rest_Id.equalsIgnoreCase(responseList.User_Id);
                    if (!b1) {
                        new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.cart_alert)).setMessage(getResources().getString(R.string.cart_alert_message)).setPositiveButton(getResources().getString(R.string.clear_cart_nd_add), (dialog, which) -> {

                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
                                showProgressDialog(getActivity());
                                makeHttpCall(ResturantFragment.this, Apis.DELETE_CART, getRetrofitInterface().deleteCart(jsonObject.toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }).setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).create().show();
                    } else {
                        addToCartApiCall(itemsData);
                    }
                }
            } else {
                addToCartApiCall(itemsData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flavourDialog(MenuItemListModel.responseData.ItemsData itemsData, Context context) {

        if (flavorDialogView != null) {
            flavorDialogView.dismiss();
        }

        flavorDialogView = new Dialog(context);
        flavorDialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flavorDialogView.setContentView(R.layout.dialog_layout_flavor);
        flavorDialogView.setCancelable(false);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        flavorDialogView.getWindow().setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        flavorDialogView.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        ListView lvFlavours = flavorDialogView.findViewById(R.id.lvFlavours);
        AppCompatTextView tvContinue = flavorDialogView.findViewById(R.id.tvContinue);
        AppCompatTextView tvCancel = flavorDialogView.findViewById(R.id.tvCancel);
        lvFlavours.setAdapter(new AdapterFlavour(getActivity(), itemsData));

        tvCancel.setOnClickListener(v -> {
            flavour = "";
            flavorDialogView.dismiss();
        });

        tvContinue.setOnClickListener(v -> {
            if (TextUtils.isEmpty(flavour)) {
                Toast.makeText(getActivity(), "Elige tu sabor", Toast.LENGTH_SHORT).show();
                return;
            }
            finalCarAPiCall(itemsData, flavour);
            flavorDialogView.dismiss();
        });

        flavorDialogView.show();
    }

    private void addToCartApiCall(MenuItemListModel.responseData.ItemsData itemsData) {

        if (!TextUtils.isEmpty(itemsData.Item_Flavor)) {
            flavourDialog(itemsData, getActivity());
            return;
        }
        finalCarAPiCall(itemsData, "");
    }

    private void finalCarAPiCall(MenuItemListModel.responseData.ItemsData itemsData, String flavor) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Customer_Id", new SharedPrefModule(getActivity()).getUserId());
            jsonObject.put("Rest_Id", responseList.User_Id);
            jsonObject.put("Item_Id", itemsData.Id);
            jsonObject.put("Item_Flavor_Type", flavor);
            jsonObject.put("Item_Quantity", "1");
            showProgressDialog(getActivity());
            makeHttpCall(this, Apis.ADD_TO_CART, getRetrofitInterface().addToCart(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}