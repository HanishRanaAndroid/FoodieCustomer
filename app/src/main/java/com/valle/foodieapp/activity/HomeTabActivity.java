package com.valle.foodieapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.valle.foodieapp.R;
import com.valle.foodieapp.base.BaseActivity;
import com.valle.foodieapp.fragment.AddAddressFragment;
import com.valle.foodieapp.fragment.CartFragment;
import com.valle.foodieapp.fragment.HomeScreenFragment;
import com.valle.foodieapp.fragment.OrdersHistoryFragment;
import com.valle.foodieapp.fragment.ResturantFragment;
import com.valle.foodieapp.fragment.SearchFragment;
import com.valle.foodieapp.fragment.SelectAddressFragment;
import com.valle.foodieapp.fragment.SettingFragment;
import com.valle.foodieapp.fragment.WishListFragment;
import com.valle.foodieapp.fragment.WompiPaymentCheckoutFragment;
import com.valle.foodieapp.listeners.LocationListener;
import com.valle.foodieapp.listeners.PayUMoneyTransactionListener;
import com.valle.foodieapp.models.AddressModel;
import com.valle.foodieapp.models.CartModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.valle.foodieapp.utils.GPSTracker;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class HomeTabActivity extends BaseActivity implements View.OnClickListener, NetworkResponceListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager;
    private AppCompatImageView ivMenu, ivSearch;
    private static final String TAG = "HomeTabActivity";
    private AppCompatTextView tvViewCart;
    private static final int LOCATION_PROVIDER = 786;
    private static final int ADDRESS_PROVIDER = 999;
    private LocationListener locationListner;

    @BindView(R.id.tvLocation)
    AppCompatTextView tvLocation;

    @BindView(R.id.llCartAlertLayout)
    LinearLayoutCompat llCartAlertLayout;

    @BindView(R.id.tvNoOfItems)
    AppCompatTextView tvNoOfItems;

    @BindView(R.id.tvAppHeader)
    AppCompatTextView tvAppHeader;

    @BindView(R.id.tvItemTotal)
    AppCompatTextView tvItemTotal;

    @BindView(R.id.llSelectLocation)
    LinearLayoutCompat llSelectLocation;

    private Timer timer;
    private Timer headertimer;

    private CartModel cartModel;

    private boolean isAddressHasPriority = false;

    private boolean listCartReponse = true;

    private PayUMoneyTransactionListener payUMoneyTransactionListener;

    @OnClick(R.id.llSelectLocation)
    void OnClickllSelectLocation() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(HomeTabActivity.this);
        startActivityForResult(intent, LOCATION_PROVIDER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        headertimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        headertimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        headertimer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    Fragment fr = fragmentManager.findFragmentById(R.id.frame_layout);
                    if (fr instanceof HomeScreenFragment || fr instanceof SearchFragment) {
                        callCartApi();
                    }
                });
            }
        }, 0, 10000);

        headertimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    handleVisiblity();
                });
            }
        }, 0, 100);
    }

    public void updateCartData() {
        listCartReponse = true;
        callCartApi();
    }

    public void updateState() {
        listCartReponse = true;
    }

    private void handleVisiblity() {
        Fragment fr = fragmentManager.findFragmentById(R.id.frame_layout);
        tvAppHeader.setVisibility(fr instanceof HomeScreenFragment ? View.GONE : View.VISIBLE);
        llSelectLocation.setVisibility(fr instanceof HomeScreenFragment ? View.VISIBLE : View.GONE);

        try {
            if (fr instanceof HomeScreenFragment || fr instanceof ResturantFragment || fr instanceof SearchFragment) {

            } else {
                llCartAlertLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCartApi() {
        try {
            if (listCartReponse) {
                listCartReponse = false;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Customer_Id", new SharedPrefModule(HomeTabActivity.this).getUserId());
                makeHttpCall(this, Apis.LIST_CART, getRetrofitInterface().getCartList(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(HomeTabActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeTabActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new androidx.appcompat.app.AlertDialog.Builder(HomeTabActivity.this)
                        .setTitle(getResources().getString(R.string.perm_ness))
                        .setMessage(getResources().getString(R.string.location_perm_req))
                        .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> ActivityCompat.requestPermissions(HomeTabActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION))
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(HomeTabActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeTabActivity.this, permission)) {
                        //denied
                        checkLocationPermission();
                    } else {
                        if (ActivityCompat.checkSelfPermission(HomeTabActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                            Location lastKnownLocation = CommonUtils.getLastKnownLocation(HomeTabActivity.this);

                            if (lastKnownLocation == null) {
                                if (new GPSTracker(HomeTabActivity.this).canGetLocation()) {
                                    lastKnownLocation = new GPSTracker(HomeTabActivity.this).getLocation();
                                }
                            }

                            String latitude = String.valueOf(lastKnownLocation.getLatitude());
                            String longitude = String.valueOf(lastKnownLocation.getLongitude());
                            new SharedPrefModule(HomeTabActivity.this).setLocationLattitude(latitude);
                            new SharedPrefModule(HomeTabActivity.this).setLocationLongitude(longitude);
                            tvLocation.setText(CommonUtils.getAddress(HomeTabActivity.this, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                            locationListner.onLocationChanged(latitude, longitude);
                        } else {
                            //set to never ask again
                            new androidx.appcompat.app.AlertDialog.Builder(HomeTabActivity.this)
                                    .setTitle("Permiso necesario")
                                    .setMessage("El permiso de ubicación es necesario")
                                    .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", HomeTabActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }
                break;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        bindView(this);
        tvViewCart = findViewById(R.id.tvViewCart);
        fragmentManager = getSupportFragmentManager();
        Places.initialize(HomeTabActivity.this,
                "AIzaSyAsYL1hrGBbl8KI_DhiULQGaoMfSkQAjA4");
        PlacesClient placesClient = Places.createClient(HomeTabActivity.this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        tvViewCart.setOnClickListener(this);
        addFragment(new HomeScreenFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.action_item1:
                    selectedFragment = new HomeScreenFragment();
                    removeAllFragmentsFromStack();
                    break;
                case R.id.action_item2:
                    selectedFragment = new SearchFragment();
                    removeAllFragmentsFromStack();
                    break;
                case R.id.action_item3:
                    selectedFragment = new OrdersHistoryFragment();
                    break;
                case R.id.action_item4:
                    selectedFragment = new WishListFragment();
                    break;

                case R.id.action_item5:
                    selectedFragment = new SettingFragment();
                    break;
            }
            replaceFragment(selectedFragment, null);
            return true;
        });

        callAddressListApi();
    }

    private void removeAllFragmentsFromStack() {
        //Here we are removing all the fragment that are shown here
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                Fragment mFragment = fragmentManager.getFragments().get(i);
                if (mFragment instanceof HomeScreenFragment || mFragment instanceof SearchFragment) {
                } else if (mFragment != null) {
                    fragmentManager.popBackStackImmediate();
                }
            }
        }
    }

    private void showTooltip() {
        try {
            if (TextUtils.isEmpty(tvLocation.getText().toString())) {
                SimpleTooltip build = new SimpleTooltip.Builder(this)
                        .anchorView(tvLocation)
                        //.text("Recuerda que primero debes realizar el registro fisico para acceder a las funcionalidades de la app. Para informacion de numeros de contacto visita vallefood.co ")
                        .gravity(Gravity.BOTTOM)
                        .dismissOnInsideTouch(false)
                        .textColor(getResources().getColor(R.color.white))
                        .animated(true)
                        .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                        .transparentOverlay(false)
                        .contentView(R.layout.warning_message_layout)
                        .build();

                build.findViewById(R.id.btnOk).setOnClickListener(v2 -> {
                    if (build.isShowing()) {
                        build.dismiss();
                    }
                });
                build.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_PROVIDER) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d("LOCATION_PROVIDER", "Place: " + place.getName() + ", " + place.getId());
                tvLocation.setText(place.getName());
                locationListner.onLocationChanged(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
                new SharedPrefModule(HomeTabActivity.this).setLocationPrefrence(place.getName());
                new SharedPrefModule(HomeTabActivity.this).setLocationCity(CommonUtils.getCity(HomeTabActivity.this, place.getLatLng().latitude, place.getLatLng().longitude));
                new SharedPrefModule(HomeTabActivity.this).setLocationLattitude(String.valueOf(place.getLatLng().latitude));
                new SharedPrefModule(HomeTabActivity.this).setLocationLongitude(String.valueOf(place.getLatLng().longitude));
            } else if (requestCode == ADDRESS_PROVIDER) {
                fragmentManager.findFragmentById(R.id.frame_layout).onActivityResult(requestCode, resultCode, data);
            } else {
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
                        payUMoneyTransactionListener.onSucessfullTransaction(payuResponse, merchantResponse);

                    } else if (resultModel != null && resultModel.getError() != null) {
                        Log.d("", "Error response : " + resultModel.getError().getTransactionResponse());
                        payUMoneyTransactionListener.onFailureOfTransaction();
                    } else {
                        Log.d("", "Both objects are null!");
                    }
                }
            }
        }
    }

    private void callAddressListApi() {
        try {
            String userId = new SharedPrefModule(HomeTabActivity.this).getUserId();
            makeHttpCall(this, Apis.LIST_ADDRESS, getRetrofitInterface().getAddressList(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragmentWithBackStack(Fragment fragment, Bundle bundle) {
        String backStateName = fragment.getClass().getName();
        if (!fragmentManager.popBackStackImmediate(backStateName, 0)) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
            checkCartScreen(fragment);
        }
    }

    private void callExitAPP() {
        new AlertDialog.Builder(HomeTabActivity.this).setTitle(getResources().getString(R.string.exit))
                .setMessage(getResources().getString(R.string.are_you_sure_to_exit_app)).setPositiveButton(
                getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss()).create().show();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        checkCartScreen(fragment);
    }

    public void removeFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_layout);
        if (fragment != null)
            fragmentManager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onBackPressed() {
        getFragmentCallBack();
    }

    public void getFragmentCallBack() {
        Fragment fr = fragmentManager.findFragmentById(R.id.frame_layout);
        if ((fr instanceof HomeScreenFragment) || (fr instanceof SearchFragment) || (fr instanceof OrdersHistoryFragment) || (fr instanceof SettingFragment)) {
            callExitAPP();
        } else if (fr instanceof AddAddressFragment || fr instanceof SelectAddressFragment) {
            removeFragment();
        } else if (fr instanceof WompiPaymentCheckoutFragment) {
            Toast.makeText(HomeTabActivity.this, getResources().getString(R.string.you_cant_go_back), Toast.LENGTH_SHORT).show();
        } else {
            fragmentManager.popBackStackImmediate();
        }
        checkCartScreen(fragmentManager.findFragmentById(R.id.frame_layout));
    }

    private void checkCartScreen(Fragment fragment) {
        try {
            if (fragment instanceof HomeScreenFragment) {
                bottomNavigationView.getMenu().findItem(R.id.action_item1).setChecked(true);
            } else if (fragment instanceof OrdersHistoryFragment) {
                bottomNavigationView.getMenu().findItem(R.id.action_item3).setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvViewCart) {
            replaceFragmentWithBackStack(new CartFragment(), null);
        }
    }

    public void setLocationListner(LocationListener locationListner) {
        this.locationListner = locationListner;
    }

    public void setPayUMoenyTransactionListner(PayUMoneyTransactionListener payUMoenyTransactionListner) {
        this.payUMoneyTransactionListener = payUMoenyTransactionListner;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSuccess(String url, String responce) {
        switch (url) {
            case Apis.LIST_CART:
                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        cartModel = new Gson().fromJson(responce, CartModel.class);

                        llCartAlertLayout.setVisibility(cartModel.response.Items.size() > 0 ? View.VISIBLE : View.GONE);
                        if (cartModel.response.Items.size() > 0) {
                            tvNoOfItems.setText(cartModel.response.Items.size() + getResources().getString(R.string.item));
                            checkInstance(cartModel);
                            int total = 0;
                            for (int i = 0; i < cartModel.response.Items.size(); i++) {
                                total = total + (Integer.parseInt(cartModel.response.Items.get(i).Item_Details.get(0).Item_Price) * Integer.parseInt(cartModel.response.Items.get(i).Item_Quantity));
                            }
                            tvItemTotal.setText("$" + total + getResources().getString(R.string.plus_taxes));
                        }
                    } else {
                        cartModel = null;
                        llCartAlertLayout.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listCartReponse = true;
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
                                isAddressHasPriority = true;
                                tvLocation.setText(addressModel.response.Items.get(i).Type + "-" + addressModel.response.Items.get(i).Address2);
                                if (!TextUtils.isEmpty(addressModel.response.Items.get(i).Latitude)) {
                                    locationListner.onLocationChanged(addressModel.response.Items.get(i).Latitude, addressModel.response.Items.get(i).Longitude);
                                    new SharedPrefModule(HomeTabActivity.this).setLocationLattitude(addressModel.response.Items.get(i).Latitude);
                                    new SharedPrefModule(HomeTabActivity.this).setLocationLongitude(addressModel.response.Items.get(i).Longitude);
                                } else {
                                    setLocation();
                                }
                                break;
                            }
                        }

                        if (!isAddressHasPriority) {
                            setLocation();
                        }

                    } else {
                        setLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showTooltip();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onFailure(String url, Throwable throwable) {
        if (Apis.LIST_ADDRESS.equalsIgnoreCase(url)) {
            setLocation();
            showTooltip();
        } else if (Apis.LIST_CART.equalsIgnoreCase(url)) {
            listCartReponse = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLocation() {
        try {
            String locationLattitude = new SharedPrefModule(HomeTabActivity.this).getLocationLattitude();
            String locationLongitude = new SharedPrefModule(HomeTabActivity.this).getLocationLongitude();
            if (!TextUtils.isEmpty(locationLattitude) && !TextUtils.isEmpty(locationLongitude)) {
                tvLocation.setText(CommonUtils.getAddress(HomeTabActivity.this, Double.valueOf(locationLattitude), Double.valueOf(locationLongitude)));
                locationListner.onLocationChanged(locationLattitude, locationLongitude);
            } else {
                if (checkLocationPermission()) {
                    Location lastKnownLocation = CommonUtils.getLastKnownLocation(HomeTabActivity.this);

                    if (lastKnownLocation == null) {
                        if (new GPSTracker(HomeTabActivity.this).canGetLocation()) {
                            lastKnownLocation = new GPSTracker(HomeTabActivity.this).getLocation();
                        }
                    }

                    String latitude = String.valueOf(lastKnownLocation.getLatitude());
                    String longitude = String.valueOf(lastKnownLocation.getLongitude());
                    new SharedPrefModule(HomeTabActivity.this).setLocationLattitude(latitude);
                    new SharedPrefModule(HomeTabActivity.this).setLocationLongitude(longitude);
                    tvLocation.setText(CommonUtils.getAddress(HomeTabActivity.this, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                    locationListner.onLocationChanged(latitude, longitude);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CartModel getCartModel() {
        return cartModel;
    }

    private void checkInstance(CartModel cartModel) {
        Fragment fr = fragmentManager.findFragmentById(R.id.frame_layout);

        if (fr instanceof HomeScreenFragment || fr instanceof SearchFragment || fr instanceof ResturantFragment || fr instanceof WishListFragment) {
            llCartAlertLayout.setVisibility(cartModel.response.Items.size() > 0 ? View.VISIBLE : View.GONE);
        } else {
            llCartAlertLayout.setVisibility(View.GONE);
        }
    }
}
