package com.valle.foodieapp.models;

import java.util.List;

public class RestaurantModel {

    /*{
        "status": "success",
            "response": {
        "msg": "Found!",
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
                "Order_WithIN": "0",
                "Profile_Image": null,
                "Cover_Image": null,
                "Discount_Code": "GF23",
                "Discount_Percentage": "23",
                "Overall_Rating": "4.2",
                "Refer_Code": "BL7213",
                "Role": "Restaurant",
                "Is_Active": "InActive"
                "ItemCategory": [
                    {
                        "Item_Category": "SERPIENTES"
                    }
                ]
            },
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
            public String Order_WithIN;
            public String Profile_Image;
            public String Cover_Image;
            public String Discount_Code;
            public String Discount_Percentage;
            public String Overall_Rating;
            public String Refer_Code;
            public String Role;
            public String Is_Active;
            public List<ItemCategoryData> ItemCategory;

            public class ItemCategoryData {
                public String Item_Category;
            }
        }
    }
}