package com.valle.foodieapp.models;

import java.util.List;

public class ApplyCouponCodeModel {

/*    {
        "status": "success",
            "response": {
        "msg": "Encontró",
                "response": [
        {
            "User_Id": "79",
                "Full_Name": "First User",
                "Restaurant_Name": "Restaurant123",
                "Email": "Restaurant@gmail.com",
                "Mobile": "1231231231",
                "City": "Chandigarh",
                "Address": null,
                "Latitude": null,
                "Longitude": null,
                "Set_Your_Presence": "ON",
                "Profile_Image": null,
                "Cover_Image": null,
                "Discount_Code": "GF23",
                "Discount_Percentage": "23",
                "Overall_Rating": "4.2",
                "Refer_Code": "BL7213",
                "Role": "Restaurant",
                "Is_Active": "InActive"
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
            public String Discount_Code;
            public String Discount_Percentage;
        }
    }

}
