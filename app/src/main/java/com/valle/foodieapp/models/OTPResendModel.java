package com.valle.foodieapp.models;

public class OTPResendModel {
//    {
//        "status": "success",
//            "response": {
//        "status": "success",
//                "msg": "OTP has been sent successfully!",
//                "validationCode": 52607
//    }
//    }

    public responseData response;

    public class responseData{
        public String msg;
        public String validationCode;
    }
}
