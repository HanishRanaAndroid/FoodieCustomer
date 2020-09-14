package com.valle.foodieapp.models;

public class LoginModel {
/*    {
        "status": "success",
            "response": {
        "varification": "Active",
                "base_url": "http://weburlforclients.com/gamma/vallafood/assets/uploads/",
                "UserInfo": {
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
                    "Validation_Code": "75734",
                    "Is_Active": "InActive",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-11 09:50:46"
        }
    }
    }*/


    public responseData response;

    public class responseData {
        public String varification;
        public String base_url;
        public UserInfoData UserInfo;

        public class UserInfoData {
            public String User_Id;
            public String Full_Name;
            public String Restaurant_Name;
            public String Email;
            public String Mobile;
            public String City;
            public String Address;
            public String Website;
            public String Latitude;
            public String Longitude;
            public String Role;
            public String Login_Type;
            public String Profile_Image;
            public String Cover_Image;
            public String Refer_Code;
            public String Validation_Code;
            public String Is_Active;
            public String Device_Token;
            public String createdDtm;
        }
    }

}
