package com.valle.foodieapp.models;

public class VerifyModel {
/*    {
        "status": "success",
            "response": {
        "msg": "Your account has been verified.",
                "GetUser": {
            "User_Id": "78",
                    "Full_Name": "Developer Team ",
                    "Restaurant_Name": "Developer",
                    "Email": "test@blissitsolutions.com",
                    "Mobile": "9876543210",
                    "City": "Panchkula",
                    "Address": "Sector 5",
                    "Website": null,
                    "Latitude": "30.727921",
                    "Longitude": "76.839661",
                    "Role": "Restaurant",
                    "Login_Type": "Default",
                    "Profile_Image": null,
                    "Cover_Image": null,
                    "Refer_Code": "BL6990",
                    "Validation_Code": "",
                    "Is_Active": "Active",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-11 09:50:46"
        }
    }
    }*/

    public responseData response;

    public class responseData {
        public String msg;
        public GetUserData GetUser;

        public class GetUserData {
            public String User_Id;
            public String Full_Name;
            public String Restaurant_Name;
            public String Email;
            public String Mobile;
            public String City;
            public String Address;
        }
    }

}
