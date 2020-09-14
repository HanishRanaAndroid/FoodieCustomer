package com.valle.foodieapp.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.models.DeliveryBoyInfoModel;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.models.OrderUpdateModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.utils.AnimationUtil;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderStatusFragment extends BaseFragment implements OnMapReadyCallback, NetworkResponceListener {
    private static final long DISPLAY_LENGTH = 1000;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    @BindView(R.id.llorderDetails)
    LinearLayoutCompat llOrderDetails;

    @BindView(R.id.llHelpSupport)
    LinearLayoutCompat llHelpSupport;

    @BindView(R.id.vDetail)
    View vDetail;

    @BindView(R.id.vHelp)
    View vHelp;

    @BindView(R.id.vChat)
    View vChat;

    private int count = 0;

    @BindView(R.id.tvOrderPlaced)
    AppCompatTextView tvOrderPlaced;

    @BindView(R.id.tvConfirmed)
    AppCompatTextView tvConfirmed;

    @BindView(R.id.tvOrderProcessed)
    AppCompatTextView tvOrderProcessed;

    @BindView(R.id.tvOrderPickedUp)
    AppCompatTextView tvOrderPickedUp;

    @BindView(R.id.llChat)
    LinearLayoutCompat llChat;

    private Marker deliveryBoyMarker, restMarker;

    private final String ORDER_PLACED = "Order Placed";
    private final String ORDER_CONFIRMED = "Order Confirmed";
    private final String ORDER_PROCESSED = "Order Ready";
    private final String ORDER_PICKED = "Order Picked-up";
    private final String ORDER_DELIVERED = "Order Delivered";
    private final String ORDER_REJECTED = "Order Reject";

    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    MarkerOptions icon = new MarkerOptions();

    private Timer timer;
    private OrderPlacedModel.responseData.orders_InfoData orders_infoData;

    private DeliveryBoyInfoModel deliveryBoyInfoModel;

    private String adminPhoneNumber = "";

    public OrderStatusFragment(OrderPlacedModel.responseData.orders_InfoData orders_infoData) {
        this.orders_infoData = orders_infoData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_placed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        bindView(this, view);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setOrderItems();
    }

    private void setStausOfTheFood(boolean isOrderPlaced, boolean isConfirmed, boolean isProcessed, boolean isPickedUp) {

        tvOrderPlaced.setTextColor(isOrderPlaced ? getResources().getColor(R.color.foodie_color) : getResources().getColor(R.color.light_black));
        tvConfirmed.setTextColor(isConfirmed ? getResources().getColor(R.color.foodie_color) : getResources().getColor(R.color.light_black));
        tvOrderProcessed.setTextColor(isProcessed ? getResources().getColor(R.color.foodie_color) : getResources().getColor(R.color.light_black));
        tvOrderPickedUp.setTextColor(isPickedUp ? getResources().getColor(R.color.foodie_color) : getResources().getColor(R.color.light_black));

    }

    @OnClick(R.id.tvDetails)
    void OnClicktvDetails() {
        handleDetailsVisibility(true);
    }

    @OnClick(R.id.tvHelp)
    void OnClicktvHelp() {
        handleDetailsVisibility(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void setOrderItems() {
        try {
            if (llOrderDetails.getChildCount() > 0) {
                llOrderDetails.removeAllViews();
            }
            for (int i = 0; i < orders_infoData.Ordered_Items.Items.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_item_view, null, false);
                AppCompatTextView tvOrderItems = view.findViewById(R.id.tvOrderItems);
                AppCompatTextView tvFlavor = view.findViewById(R.id.tvFlavor);
                LinearLayoutCompat llFlavor = view.findViewById(R.id.llFlavor);
                tvOrderItems.append(CommonUtils.getColoredString(getActivity(), orders_infoData.Ordered_Items.Items.get(i).Item_Name, ContextCompat.getColor(getActivity(), R.color.light_black)));
                tvOrderItems.append(CommonUtils.getColoredString(getActivity(), "  x" + orders_infoData.Ordered_Items.Items.get(i).Quantity, ContextCompat.getColor(getActivity(), R.color.green)));
                String ItemFlavor = orders_infoData.Ordered_Items.Items.get(i).Item_Flavor_Type;
                llFlavor.setVisibility(!TextUtils.isEmpty(ItemFlavor) ? View.VISIBLE : View.GONE);
                if (!TextUtils.isEmpty(ItemFlavor)) {
                    tvFlavor.setText("sabor: " + ItemFlavor);
                }
                llOrderDetails.addView(view);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrderDetails() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", orders_infoData.Id);
            makeHttpCall(this, Apis.ORDER_HISTORY, getRetrofitInterface().getOrderDetail(jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    updateOrderDetails();
                    getDeliveryBoyLiveInfo();

                });
            }
        }, 0, 10000);

        if (CommonUtils.isNetworkAvailable(getActivity())) {
            makeHttpCall(this, Apis.ADMIN_CONTACT, getRetrofitInterface().getAdminContact());
        }

    }

    private void getDeliveryBoyLiveInfo() {
        try {
            if (!TextUtils.isEmpty(orders_infoData.Delivery_Boy_Id.User_Id)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("User_Id", orders_infoData.Delivery_Boy_Id.User_Id);

                makeHttpCall(this, Apis.GET_DELIVERY_BOY_INFO, getRetrofitInterface().getDeliveryBoyInfo(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.llChatWithDeliveryBoy)
    void OnClickllChatWithDeliveryBoy() {
        if (!TextUtils.isEmpty(orders_infoData.Delivery_Boy_Id.User_Id)) {
            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new ChatFragment(orders_infoData), null);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.plz_wait_assigning_delivery_boy), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.llHelpSupport)
    void OnClickllHelpSupport() {
        try {

            Bundle bundle = new Bundle();
            bundle.putString("order_id", orders_infoData.Id);
            bundle.putString("order_number", orders_infoData.Order_Number);

            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new HelpAndSupportFragment(), bundle);

            /*if (TextUtils.isEmpty(adminPhoneNumber)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.cb_please_wait), Toast.LENGTH_SHORT).show();
            } else {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + adminPhoneNumber));
                    startActivity(intent);
                }
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.tvChat)
    void OnClicktvChat() {
        llOrderDetails.setVisibility(View.GONE);
        vDetail.setVisibility(View.GONE);
        llHelpSupport.setVisibility(View.GONE);
        vHelp.setVisibility(View.GONE);
        vChat.setVisibility(View.VISIBLE);
        llChat.setVisibility(View.VISIBLE);
    }

    private void handleDetailsVisibility(boolean isDetails) {
        llOrderDetails.setVisibility(isDetails ? View.VISIBLE : View.GONE);
        vDetail.setVisibility(isDetails ? View.VISIBLE : View.GONE);
        llHelpSupport.setVisibility(isDetails ? View.GONE : View.VISIBLE);
        vHelp.setVisibility(isDetails ? View.GONE : View.VISIBLE);
        vChat.setVisibility(View.GONE);
        llChat.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            if (orders_infoData != null) {
                if (!TextUtils.isEmpty(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat) && !TextUtils.isEmpty(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)), new LatLng(Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat), Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)))
                            .width(5)
                            .pattern(PATTERN_POLYGON_ALPHA)
                            .color(getActivity().getResources().getColor(R.color.black)));


                    MarkerOptions markerOptions2 = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_location));
                    markerOptions2.position(new LatLng(Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat), Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)));
                    markerOptions2.title(getResources().getString(R.string.delivery_location));
                    googleMap.addMarker(markerOptions2);
                }

                if (!TextUtils.isEmpty(orders_infoData.Rest_Id.Profile_Image)) {
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(Apis.IMAGE_URL + orders_infoData.Rest_Id.Profile_Image)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                    if (restMarker != null) {
                                        restMarker.remove();
                                    }

                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource, 80, 80, false);
                                    icon = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap));
                                    MarkerOptions markerOptions = icon;
                                    markerOptions.position(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)));
                                    markerOptions.title(orders_infoData.Rest_Id.Restaurant_Name);
                                    restMarker = mMap.addMarker(markerOptions);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                } else {
                    icon = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.rest_icon));
                }

                MarkerOptions markerOptions = icon;
                markerOptions.position(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)));
                markerOptions.title(orders_infoData.Rest_Id.Restaurant_Name);
                restMarker = googleMap.addMarker(markerOptions);

                MarkerOptions deliveryBoy = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_boy));
                deliveryBoy.position(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)));
                deliveryBoy.title(getResources().getString(R.string.delivery_boy));

                deliveryBoyMarker = mMap.addMarker(deliveryBoy);
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)))      // Sets the center of the map to location user
                        .zoom(14)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (!TextUtils.isEmpty(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat) && !TextUtils.isEmpty(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)));
                    builder.include(new LatLng(Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat), Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)));
                    LatLngBounds bounds = builder.build();
                    new Handler().postDelayed(() -> {
                        if (mMap != null) {
                            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                            mMap.animateCamera(cu);
                        }
                        if (getActivity() != null) {
                            getActivity().fileList();
                        }
                    }, 3000);
                }
            /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(Double.valueOf(orders_infoData.Rest_Id.Latitude), Double.valueOf(orders_infoData.Rest_Id.Longitude)));
            builder.include(new LatLng(Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Lat), Double.valueOf(orders_infoData.Ordered_Items.Delivery_Address.Delivery_Long)));
            int padding = 150;
            LatLngBounds bounds = builder.build();
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());
        switch (url) {
            case Apis.ORDER_HISTORY:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        OrderUpdateModel orderUpdateModel = new Gson().fromJson(responce, OrderUpdateModel.class);
                        orders_infoData = orderUpdateModel.response.orders_Info.get(0);
                        if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_PLACED)) {
                            setStausOfTheFood(true, false, false, false);
                        } else if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_CONFIRMED) || orders_infoData.Order_Status.equalsIgnoreCase(ORDER_PROCESSED)) {
                            /*setStausOfTheFood(false, true, false, false);
                        } else if (count == 2) {*/
                            setStausOfTheFood(false, false, true, false);
                        } else if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_PICKED)) {
                            setStausOfTheFood(false, false, false, true);
                        } else if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_DELIVERED)) {
                            timer.cancel();
                            ((HomeTabActivity) getActivity()).replaceFragmentWithBackStack(new RateOrderFragment(orders_infoData), null);
                        } else if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_REJECTED)) {
                            getActivity().onBackPressed();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.GET_DELIVERY_BOY_INFO:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        deliveryBoyInfoModel = new Gson().fromJson(responce, DeliveryBoyInfoModel.class);
                        if (deliveryBoyInfoModel.response.response.size() > 0) {
                            String latitude = deliveryBoyInfoModel.response.response.get(0).Latitude;
                            String longitude = deliveryBoyInfoModel.response.response.get(0).Longitude;

                            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(latitude)) {
                                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                AnimationUtil.animateMarkerTo(deliveryBoyMarker, latLng);

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to location user
                                        .zoom(14)                   // Sets the zoom
                                        .bearing(90)                // Sets the orientation of the camera to east
                                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                if (mMap != null) {
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Apis.ADMIN_CONTACT:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);
                    /*{
                        "status": "success",
                            "response": {
                        "msg": "",
                                "Contact_Number": {
                            "Contact_Number": "9999999999"
                        }
                    }
                    }*/

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {

                        adminPhoneNumber = jsonObject.getJSONObject("response").getJSONObject("Contact_Number").getString("Contact_Number");

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
