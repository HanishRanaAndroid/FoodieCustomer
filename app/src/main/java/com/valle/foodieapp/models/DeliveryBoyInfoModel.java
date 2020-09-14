package com.valle.foodieapp.models;

import java.util.List;

public class DeliveryBoyInfoModel {
   /* {
        "status": "success",
            "response": {
        "msg": "Encontró",
                "response": [
        {
            "User_Id": "89",
                "Full_Name": "Delivery Boy",
                "Restaurant_Name": "",
                "Email": "DeliveryBoy@gmail.com",
                "Mobile": "3211233214",
                "City": "Chandigarh",
                "Address": "Sector 5",
                "Latitude": "30.722041",
                "Longitude": "76.851761",
                "Set_Your_Presence": "OFF",
                "Profile_Image": "IMG_20191214_162009_1919978022791123804.jpg",
                "Cover_Image": "",
                "Discount_Code": "",
                "Discount_Percentage": null,
                "Overall_Rating": "5",
                "Refer_Code": "BL7181",
                "Role": "Delivery_Boy",
                "Is_Active": "Active"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData {
        public List<responseList> response;

        public class responseList {
            public String User_Id;
            public String Full_Name;
            public String Restaurant_Name;
            public String Email;
            public String Mobile;
            public String City;
            public String Address;
            public String Latitude;
            public String Longitude;
            public String Set_Your_Presence;
            public String Profile_Image;
            public String Cover_Image;
            public String Discount_Code;
            public String Discount_Percentage;
            public String Overall_Rating;
            public String Role;
            public String Is_Active;
        }
    }
}
