package com.valle.foodieapp.models;

import java.util.List;

public class MenuItemListModel {
 /*   {
        "status": "success",
            "response": {
        "msg": "Item Found!",
                "Items": [
        {
            "Id": "9",
                "Rest_Id": "87",
                "Item_Name": "Sandwich doble carne especial",
                "Item_Category": "SNAKS",
                "Item_Price": "12500",
                "Item_Description": "Delicioso sandwich con doble porción de jamón y queso.",
                "Item_Image": "sandwich-qbano.jpg",
                "Item_Status": "In Stock",
                "Discount_Percentage": null,
                "Created_Date": "2019-12-17 23:22:55"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData {
        public List<ItemsData> Items;

        public class ItemsData {
            public String Id;
            public String Rest_Id;
            public String Item_Name;
            public String Item_Category;
            public String Item_Price;
            public String Item_Description;
            public String Item_Image;
            public String Item_Status;
            public String Item_Flavor;
            public String Discount_Percentage;
            public String Created_Date;
        }
    }
}
