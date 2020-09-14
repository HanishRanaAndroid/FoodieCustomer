package com.valle.foodieapp.network;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("{coin}-usd")
    Observable<String> getCoinData(@Path("coin") String coin);

    @GET(Apis.HOME_PAGE)
    Observable<String> getHomePageData();

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.REGISTER)
    @FormUrlEncoded
    Observable<String> register(@Field("Full_Name") String Full_Name, @Field("Mobile") String Mobile, @Field("Email") String Email, @Field("Password") String Password, @Field("Device_Token") String Device_Token, @Field("Role") String Role);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.LOGIN)
    @FormUrlEncoded
    Observable<String> login(@Field("FieldType") String FieldType, @Field("Mobile") String Mobile, @Field("Password") String Password, @Field("Role_Type") String Role_Type, @Field("Device_Token") String Device_Token);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.SEND_OTP)
    @FormUrlEncoded
    Observable<String> sendOTPtoUser(@Field("Mobile") String Mobile);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.VERIFY_ACCOUNT)
    @FormUrlEncoded
    Observable<String> verifyAccount(@Field("Mobile") String Mobile, @Field("Code") String Code, @Field("Status") String Status);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.CHNAGE_PASSWORD)
    @FormUrlEncoded
    Observable<String> changePassword(@Field("User_Id") String User_Id, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.FORGET_PASSWORD)
    @FormUrlEncoded
    Observable<String> forgetPassword(@Field("Mobile") String Mobile, @Field("Password") String Password);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @Multipart
    @POST(Apis.EDIT_PROFILE)
    Call<String> updateProfile(@Part MultipartBody.Part image, @Part("User_Id") RequestBody User_Id,
                               @Part("Full_Name") RequestBody Full_Name, @Part("Email") RequestBody Email,
                               @Part("Mobile") RequestBody Mobile);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.TOP_DISCOUNTED_RESTAURANT)
    @FormUrlEncoded
    Observable<String> getTopDiscountRestaurant(@Field("Where") String Where, @Field("orderBy") String orderBy, @Field("Type") String Type);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @GET(Apis.SECOND_SLIDER)
    Observable<String> getSecondSliderData();

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ITEMS_LIST)
    @FormUrlEncoded
    Observable<String> getMenuList(@Field("Rest_Id") String Rest_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ORDER_HISTORY)
    @FormUrlEncoded
    Observable<String> getOrderHistory(@Field("Where") String Rest_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ORDER_HISTORY)
    @FormUrlEncoded
    Observable<String> getOrderDetail(@Field("Where") String Rest_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ADD_TO_CART)
    @FormUrlEncoded
    Observable<String> addToCart(@Field("Data") String Data);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.CHAT_API)
    @FormUrlEncoded
    Observable<String> chatApi(@Field("Where") String Data);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.SUBMIT_CHAT)
    @FormUrlEncoded
    Observable<String> submitChatApi(@Field("Data") String Data);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ORDER_HISTORY)
    @FormUrlEncoded
    Observable<String> getWishList(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.UPDATE_WISHLIST)
    @FormUrlEncoded
    Observable<String> submitWishList(@Field("Where") String Where, @Field("data") String data);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.LIST_CART)
    @FormUrlEncoded
    Observable<String> getCartList(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.DELETE_CART)
    @FormUrlEncoded
    Observable<String> deleteCart(@Field("Where") String Where);


    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.UPDATE_ADDRESS)
    @FormUrlEncoded
    Observable<String> updateAddress(@Field("Address_Id") String Address_Id, @Field("Address1") String Address1, @Field("Address2") String Address2, @Field("Landmark") String mLandmark,
                                     @Field("Type") String Type, @Field("Latitude") String Latitude, @Field("Longitude") String Longitude, @Field("Is_Primary") String Is_Primary, @Field("Customer_Id") String Customer_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.LIST_ADDRESS)
    @FormUrlEncoded
    Observable<String> getAddressList(@Field("Customer_Id") String Customer_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.UPDATE_CART)
    @FormUrlEncoded
    Observable<String> updateCart(@Field("Data") String Data, @Field("Cart_Id") String Cart_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ADD_ADDRESS)
    @FormUrlEncoded
    Observable<String> addAddress(@Field("Customer_Id") String Customer_Id, @Field("Address1") String Address1, @Field("Address2") String Address2,
                                  @Field("Landmark") String Landmark, @Field("Type") String Type, @Field("Latitude") String Latitude, @Field("Longitude") String Longitude);


    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.TOP_DISCOUNTED_RESTAURANT)
    @FormUrlEncoded
    Observable<String> applyCounponCode(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.GET_DELIVERY_BOY_INFO)
    @FormUrlEncoded
    Observable<String> getDeliveryBoyInfo(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.GET_DELIVERY_BOY_INFO)
    @FormUrlEncoded
    Observable<String> getRestaurantInfo(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.CREATE_ORDER)
    @FormUrlEncoded
    Observable<String> createOrder(@Field("Customer_Id") String Customer_Id, @Field("Rest_Id") String Rest_Id, @Field("Ordered_Items") String Ordered_Items, @Field("Grand_Total") String Grand_Total, @Field("Payment_Status") String Payment_Status, @Field("Payment_Type") String Payment_Type, @Field("Payment_History") String Payment_History, @Field("Custom_Note") String Custom_Note, @Field("Order_Number") String Order_Number);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.REPEAT_ORDER)
    @FormUrlEncoded
    Observable<String> repeatOrder(@Field("Data") String Data, @Field("Wishlist_Id") String Wishlist_Id);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.SUBMIT_RATING)
    @FormUrlEncoded
    Observable<String> submitRating(@Field("Data") String Data);


    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.ASSIGN_DELIVERY_BOY)
    @FormUrlEncoded
    Observable<String> assignDelivery(@Field("Where") String Where);

    @Headers("Content-Type: application/json")
    @POST(Apis.GET_PAYMENT_LINK)
    Observable<String> getPaymentLink(@Body String body);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.GET_ORDER_PAYMENT_STATUS)
    @FormUrlEncoded
    Observable<String> getPaymentStatus(@Field("Where") String Where);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @GET(Apis.ADMIN_CONTACT)
    Observable<String> getAdminContact();

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @POST(Apis.SUBMIT_TICKET)
    @FormUrlEncoded
    Observable<String> submitQuery(@Field("User_Id") String User_Id, @Field("Type") String Type, @Field("Order_Id") String Order_Id, @Field("Message") String Message);

    @Headers("apitoken: 813937bae1a26e2d442acec31c85c460e0a12c98")
    @GET(Apis.LIST_TICKET)
    Observable<String> getQuery();
}
