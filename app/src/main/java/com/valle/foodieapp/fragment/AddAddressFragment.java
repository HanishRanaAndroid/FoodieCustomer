package com.valle.foodieapp.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.base.BaseFragment;
import com.valle.foodieapp.listeners.SelectAddressInterface;
import com.valle.foodieapp.models.AddAddressModel;
import com.valle.foodieapp.models.AddressModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.network.NetworkResponceListener;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.gson.Gson;
import com.valle.foodieapp.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static android.content.Context.LOCATION_SERVICE;

public class AddAddressFragment extends BaseFragment implements OnMapReadyCallback, NetworkResponceListener {

    private SelectAddressInterface selectAddressInterface;
    private static final int LOCATION_PROVIDER = 999;
    private String dialogAddressType = "";

    public AddAddressFragment(SelectAddressInterface selectAddressInterface) {
        this.selectAddressInterface = selectAddressInterface;
    }

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private LocationManager mLocationManager;
    private String addressType = "";
    private String longitude = "", latitude = "";
    private String finalAdddress = "", landmardialog = "";

    @BindView(R.id.etMapLocation)
    AppCompatEditText etMapLocation;

    @BindView(R.id.tvAddress)
    AppCompatTextView tvAddress;

    @BindView(R.id.etFlatNo)
    AppCompatEditText etFlatNo;

    @BindView(R.id.etLandmark)
    AppCompatEditText etLandmark;

    @BindView(R.id.tvAsHome)
    AppCompatTextView tvAsHome;

    @BindView(R.id.tvAsWork)
    AppCompatTextView tvAsWork;

    @BindView(R.id.tvAsOther)
    AppCompatTextView tvAsOther;

    @BindView(R.id.llMyOrder)
    LinearLayoutCompat llMyOrder;

    @BindView(R.id.upperLayout)
    RelativeLayout upperLayout;

    LatLng locationFromAddress = null;

    private String ADDRESS_TYPE = "";
    private final String DIALOG = "DIALOG";
    private final String MAIN_SCREEEN = "MAIN_SCREEEN";

    @OnClick({R.id.tvAsHome, R.id.tvAsWork, R.id.tvAsOther})
    void OnClickAddressType(View view) {
        if (view == tvAsHome) {
            addressType = "Home";
            addressTypeSelected(true, false, false);
        } else if (view == tvAsWork) {
            addressType = "Work";
            addressTypeSelected(false, true, false);
        } else {
            addressType = "Other";
            addressTypeSelected(false, false, true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.tvNotAbleToFoundAddress)
    void onClicktvNotAbleToFoundAddress() {
        dialogAddressType = "";
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_address_not_found);
        dialog.setCancelable(true);
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        AppCompatEditText etAddress = dialog.findViewById(R.id.etAddress);
        AppCompatEditText etCity = dialog.findViewById(R.id.etCity);
        AppCompatEditText etLandmark = dialog.findViewById(R.id.etLandmark);
        AppCompatTextView tvAsHome = dialog.findViewById(R.id.tvAsHome);
        AppCompatTextView tvAsWork = dialog.findViewById(R.id.tvAsWork);
        AppCompatTextView tvAsOther = dialog.findViewById(R.id.tvAsOther);
        AppCompatButton btSubmitAddress = dialog.findViewById(R.id.btSubmitAddress);

        tvAsHome.setOnClickListener(v -> {
            dialogAddressType = "Home";
            addressTypeSelectedDialog(tvAsHome, tvAsWork, tvAsOther, true, false, false);
        });

        tvAsWork.setOnClickListener(v -> {
            dialogAddressType = "Work";
            addressTypeSelectedDialog(tvAsHome, tvAsWork, tvAsOther, false, true, false);
        });

        tvAsOther.setOnClickListener(v -> {
            dialogAddressType = "Other";
            addressTypeSelectedDialog(tvAsHome, tvAsWork, tvAsOther, false, false, true);
        });

        btSubmitAddress.setOnClickListener(v -> {
            String address = etAddress.getText().toString();
            String city = etCity.getText().toString();
//            landmardialog = etLandmark.getText().toString();
            landmardialog = "";

            if (TextUtils.isEmpty(address)) {
                etAddress.setError(getActivity().getResources().getString(R.string.address));
                etAddress.setFocusable(true);
                return;
            }

            if (TextUtils.isEmpty(city)) {
                etCity.setError(getActivity().getResources().getString(R.string.city));
                etCity.setFocusable(true);
                return;
            }
//
//            if (TextUtils.isEmpty(landmardialog)) {
//                etLandmark.setError(getActivity().getResources().getString(R.string.landmark));
//                etLandmark.setFocusable(true);
//                return;
//            }

            if (TextUtils.isEmpty(dialogAddressType)) {
                Toast.makeText(getActivity(), getResources().getString(R.string.plz_choose_adddress_type), Toast.LENGTH_SHORT).show();
                return;
            }

            dialog.dismiss();
            showProgressDialog(getActivity());
            finalAdddress = address;

            if (!finalAdddress.contains(city)) {
                finalAdddress = finalAdddress + "," + city;
            }

            String latitude = "";
            String longitude = "";

            ADDRESS_TYPE = DIALOG;
            String userId = new SharedPrefModule(getActivity()).getUserId();
            makeHttpCall(this, Apis.ADD_ADDRESS, getRetrofitInterface().addAddress(userId, finalAdddress, finalAdddress, landmardialog, dialogAddressType, latitude, longitude));
            /*try {
                locationFromAddress = CommonUtils.getLocationFromAddress(getActivity(), finalAdddress);

                if (locationFromAddress == null) {
                    locationFromAddress = CommonUtils.getLocationFromAddress(getActivity(), finalAdddress);
                }

                if (locationFromAddress == null) {
                    new DataLongOperationAsynchTask().execute(finalAdddress);
                } else {
                    if (locationFromAddress == null) {
                        Location lastKnownLocation = CommonUtils.getLastKnownLocation(getActivity());
                        locationFromAddress = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        if (lastKnownLocation == null) {
                            if (new GPSTracker(getActivity()).canGetLocation()) {
                                lastKnownLocation = new GPSTracker(getActivity()).getLocation();
                                locationFromAddress = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            }
                        }
                    }

                    String latitude = String.valueOf(locationFromAddress.latitude);
                    String longitude = String.valueOf(locationFromAddress.longitude);
                    ADDRESS_TYPE = DIALOG;
                    String userId = new SharedPrefModule(getActivity()).getUserId();
                    makeHttpCall(this, Apis.ADD_ADDRESS, getRetrofitInterface().addAddress(userId, finalAdddress, finalAdddress, landmardialog, dialogAddressType, latitude, longitude));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/

        });

        dialog.show();
    }

    public class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address=" + params[0].replaceAll("[^a-zA-Z0-9\\s+]", " ") + "&sensor=false&key=AIzaSyBYOIy2ebhxd_oMDS3xmAmqOyKAM3CG-JY");
                Log.d("response", "" + response);
                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
                locationFromAddress = new LatLng(lat, lng);
                if (locationFromAddress == null) {
                    Location lastKnownLocation = CommonUtils.getLastKnownLocation(getActivity());
                    locationFromAddress = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    if (lastKnownLocation == null) {
                        if (new GPSTracker(getActivity()).canGetLocation()) {
                            lastKnownLocation = new GPSTracker(getActivity()).getLocation();
                            locationFromAddress = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }
                }

                String latitude = String.valueOf(locationFromAddress.latitude);
                String longitude = String.valueOf(locationFromAddress.longitude);

                String userId = new SharedPrefModule(getActivity()).getUserId();
                ADDRESS_TYPE = DIALOG;
                makeHttpCall(AddAddressFragment.this, Apis.ADD_ADDRESS, getRetrofitInterface().addAddress(userId, finalAdddress, finalAdddress, landmardialog, dialogAddressType, latitude, longitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getLatLongByURL(String requestURL) {
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void addressTypeSelectedDialog(AppCompatTextView tvAsHome, AppCompatTextView tvAsWork, AppCompatTextView tvAsOther, boolean isHome, boolean isWork, boolean isOther) {
        tvAsHome.setBackgroundDrawable(isHome ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));
        tvAsWork.setBackgroundDrawable(isWork ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));
        tvAsOther.setBackgroundDrawable(isOther ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));

        tvAsHome.setTextColor(isHome ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
        tvAsWork.setTextColor(isWork ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
        tvAsOther.setTextColor(isOther ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
    }

    private void addressTypeSelected(boolean isHome, boolean isWork, boolean isOther) {
        tvAsHome.setBackgroundDrawable(isHome ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));
        tvAsWork.setBackgroundDrawable(isWork ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));
        tvAsOther.setBackgroundDrawable(isOther ? getResources().getDrawable(R.drawable.address_selected) : getResources().getDrawable(R.drawable.border_back));

        tvAsHome.setTextColor(isHome ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
        tvAsWork.setTextColor(isWork ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
        tvAsOther.setTextColor(isOther ? getResources().getColor(R.color.white) : getResources().getColor(R.color.foodie_color));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_PROVIDER) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etMapLocation.setText(place.getName());
                tvAddress.setText(place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                if (place != null && mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(this, view);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//        showTooltip();
        configureCameraIdle();
    }

    @OnClick(R.id.tvAddress)
    void OnClickGetPlace() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, LOCATION_PROVIDER);
    }

    private void showTooltip() {
        try {
            new SimpleTooltip.Builder(getActivity())
                    .anchorView(upperLayout)
                    .text("Elija la ubicación de su dirección desplazándose por el mapa")
                    .gravity(Gravity.BOTTOM)
                    .textColor(getResources().getColor(R.color.white))
                    .animated(true)
                    .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                    .transparentOverlay(false)
                    .build()
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tvSaveAddressAndContinue)
    void OnClicktvSaveAddressAndContinue() {

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showSnakBar(llMyOrder);
            return;
        }

        String address = tvAddress.getText().toString();
        String flatNo = etFlatNo.getText().toString();
//        String landmark = etLandmark.getText().toString();
        String landmark = "";

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.plz_enter_your_address), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(flatNo)) {
            etFlatNo.setError(getResources().getString(R.string.plz_enter_house_flat_no));
            etFlatNo.setFocusable(true);
            return;
        }

//        if (TextUtils.isEmpty(landmark)) {
//            etLandmark.setError(getResources().getString(R.string.plz_enter_landmark));
//            etLandmark.setFocusable(true);
//            return;
//        }

        if (TextUtils.isEmpty(addressType)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.plz_choose_adddress_type), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(getActivity());
        ADDRESS_TYPE = MAIN_SCREEEN;
        String userId = new SharedPrefModule(getActivity()).getUserId();
        makeHttpCall(this, Apis.ADD_ADDRESS, getRetrofitInterface().addAddress(userId, etMapLocation.getText().toString(), flatNo + "," + address, landmark, addressType, latitude, longitude));

    }

    private void configureCameraIdle() {
        onCameraIdleListener = () -> {
            LatLng latLng = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(getActivity());

            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    String locality = addressList.get(0).getAddressLine(0);
                    String country = addressList.get(0).getCountryName();
                    if (!TextUtils.isEmpty(locality) && !TextUtils.isEmpty(country)) {
                        longitude = String.valueOf(latLng.longitude);
                        latitude = String.valueOf(latLng.latitude);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.7783, 119.4179), 13));
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        try {
            Location location = CommonUtils.getLastKnownLocation(getActivity());
            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

         /*   boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));*/

            if (!success) {
                Log.e("Location", "Style parsing failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String url, String responce) {
        hideProgressDialog(getActivity());
        switch (url) {

            case Apis.ADD_ADDRESS:

                try {

                    if (TextUtils.isEmpty(responce)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responce);

                    if (jsonObject.getString(Apis.STATUS).equalsIgnoreCase(Apis.SUCCESS)) {
                        AddAddressModel addAddressModel = new Gson().fromJson(responce, AddAddressModel.class);
                        String res = "{\n" +
                                "            \"Id\": \"1\",\n" +
                                "                \"Customer_Id\": \"80\",\n" +
                                "                \"Address1\": \"House no 123\",\n" +
                                "                \"Address2\": \"Sector 15, Chandigarh\",\n" +
                                "                \"Landmark\": \"Near SBI bank\",\n" +
                                "                \"Latitude\": \"30.7565\",\n" +
                                "                \"Longitude\": \"76.4547\",\n" +
                                "                \"Is_Primary\": \"0\",\n" +
                                "                \"Type\": \"Home\"\n" +
                                "        }";

                        AddressModel.responseData.ItemsData itemsData = new Gson().fromJson(res, AddressModel.responseData.ItemsData.class);
                        if (ADDRESS_TYPE.equalsIgnoreCase(MAIN_SCREEEN)) {
                            itemsData.Is_Primary = "1";
                            itemsData.Address1 = etMapLocation.getText().toString();
                            itemsData.Address2 = etFlatNo.getText().toString() + tvAddress.getText().toString();
                            itemsData.Customer_Id = new SharedPrefModule(getActivity()).getUserId();
                            itemsData.Id = addAddressModel.response.Address_Id;
//                            itemsData.Landmark = etLandmark.getText().toString();
                            itemsData.Landmark = "";
                            itemsData.Latitude = latitude;
                            itemsData.Longitude = longitude;
                            itemsData.Type = addressType;
                        } else {
                            itemsData.Is_Primary = "1";
                            itemsData.Address1 = finalAdddress;
                            itemsData.Address2 = finalAdddress;
                            itemsData.Customer_Id = new SharedPrefModule(getActivity()).getUserId();
                            itemsData.Id = addAddressModel.response.Address_Id;
                            itemsData.Landmark = landmardialog;
                            itemsData.Latitude = "";
                            itemsData.Longitude = "";
                            itemsData.Type = dialogAddressType;
                        }
                        selectAddressInterface.OnAddressSelected(itemsData);
                        ((HomeTabActivity) getActivity()).removeFragment();
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
