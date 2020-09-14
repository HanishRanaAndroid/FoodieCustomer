package com.valle.foodieapp.models;

import java.util.List;

public class CartModel {

  /*  {
        "status": "success",
            "response": {
        "msg": "Artículos del carrito encontrados",
                "Items": [
        {
            "Id": "7",
                "Customer_Id": "80",
                "Rest_Id": "87",
                "Item_Id": "9",
                "Item_Name": "",
                "Item_Image": "",
                "Item_Quantity": "2",
                "Created_Date": "2019-12-18 03:39:16",
                "Item_Details": [
            {
                "Id": "9",
                    "Rest_Id": "87",
                    "Item_Name": "Sandwich doble carne especial",
                    "Item_Category": "SERPIENTES",
                    "Item_Price": "12500",
                    "Item_Description": "Delicioso sandwich con doble porción de jamón y queso.",
                    "Item_Image": "sandwich-qbano.jpg",
                    "Item_Status": "In Stock",
                    "Discount_Percentage": null,
                    "Created_Date": "2019-12-18 05:38:25"
            }
                ]
        }
        ],
        "Other_Charges": {
            "Service Tax": "13",
                    "Delivery Charges": "50"
        }
    }
    }*/

    public responseData response;

    public class responseData {
        public String msg;
        public List<ItemsList> Items;
        public Other_ChargesData Other_Charges;

        public class Other_ChargesData{
            public String Service_Tax;
            public String Delivery_Charges;
        }

        public class ItemsList {
            public String Id;
            public String Rest_Id;
            public String Item_Id;
            public String Item_Name;
            public String Item_Image;
            public String Item_Quantity;
            public String Item_Flavor_Type;
            public String Created_Date;
            public List<Item_DetailsData> Item_Details;

            public class Item_DetailsData {
                public String Id;
                public String Rest_Id;
                public String Item_Name;
                public String Item_Category;
                public String Item_Price;
                public String Item_Description;
                public String Item_Image;
                public String Item_Status;
                public String Discount_Percentage;
                public String Created_Date;
            }
        }
    }
}
