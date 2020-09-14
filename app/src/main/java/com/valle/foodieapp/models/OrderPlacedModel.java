package com.valle.foodieapp.models;

import java.util.List;

public class OrderPlacedModel {
    /*{
        "status": "success",
            "response": {
        "msg": "Found!",
                "orders_Info": [
        {
            "Id": "7",
                "Customer_Id": {
            "User_Id": "1",
                    "Full_Name": "Admin",
                    "Restaurant_Name": "",
                    "Email": "admin@admin.com",
                    "Mobile": "1231231231",
                    "City": "",
                    "Address": "",
                    "Website": null,
                    "Latitude": "",
                    "Longitude": "",
                    "Role": "Admin",
                    "Login_Type": "Default",
                    "Set_Your_Presence": "ON",
                    "Profile_Image": "",
                    "Cover_Image": null,
                    "Refer_Code": null,
                    "Validation_Code": "",
                    "Is_Active": "Active",
                    "Device_Token": "",
                    "createdDtm": "2019-09-23 13:42:03"
        },
            "Delivery_Boy_Id": null,
                "Rest_Id": {
            "User_Id": "77",
                    "Full_Name": "Developer 1",
                    "Restaurant_Name": "DeveloperRest",
                    "Email": "mohitgoel@blissitsolutions.com",
                    "Mobile": "1231231232",
                    "City": "Panchkula",
                    "Address": "Sector 51",
                    "Website": null,
                    "Latitude": "30.727921",
                    "Longitude": "76.839661",
                    "Role": "Restaurant",
                    "Login_Type": "Default",
                    "Set_Your_Presence": "ON",
                    "Profile_Image": "blog_bg13.jpg",
                    "Cover_Image": "banner13.jpg",
                    "Refer_Code": "BL7610",
                    "Validation_Code": "34666",
                    "Is_Active": "Active",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-09 13:39:17"
        },
            "Order_Number": "ON1412177VF3735",
                "Order_Status": "Order Placed",
                "Delivery_Time": "",
                "Grand_Total": "200",
                "Payment_Type": "Cash",
                "Payment_Status": "Paid",
                "Payment_History": null,
                "Ordered_Items": {
            "Items": [
            {

                            "Id": "5",
                            "Quantity": "2",
                            "Item_Name": "VEG MANCHURIAN GRAVY",
                            "Item_Category": "Chinese",
                            "Item_Price": "1200",
                            "Item_Image": ""
            }
                    ],
            "Delivery_Address": {
                "Address": "Sector 22, chandigarh",
                        "Map_Address": "Sector 22, chandigarh",
                        "Delivery_Lat": "33.345435",
                        "Delivery_Long": "75.345435"
            },
            "Tax": "10",
                    "Delivery_Charges": "20",
                    "Promo_Code": "AV20",
                    "Discount_Amount": "52",
                    "Grand_Total": "200"
        },
            "Wishlist": "No",
             "Is_Rating": {
          "Rating": "3",
          "Comment": "Nice Thali"
        },
                "createdDtm": "0000-00-00 00:00:00"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData {

        public List<orders_InfoData> orders_Info;

        public class orders_InfoData {
            public String Id;
            public String Order_Number;
            public String Order_Status;
            public String Delivery_Time;
            public String Payment_Type;
            public String Payment_Status;
            public String Grand_Total;
            public String Wishlist;
            public IsRatingData Is_Rating;
            public String createdDtm;

            public LoginModel.responseData.UserInfoData Customer_Id;
            public LoginModel.responseData.UserInfoData Rest_Id;
            public LoginModel.responseData.UserInfoData Delivery_Boy_Id;
            public Ordered_ItemsData Ordered_Items;

            public class IsRatingData {
                public String Rating;
                public String Comment;
            }

            public class Ordered_ItemsData {
                public List<ItemsData> Items;
                public Delivery_AddressData Delivery_Address;

                public class ItemsData {
                    public String Id;
                    public String Quantity;
                    public String Item_Name;
                    public String Item_Category;
                    public String Item_Price;
                    public String Item_Image;
                    public String Item_Flavor_Type;
                }

                public class Delivery_AddressData {
                    public String Address;
                    public String Map_Address;
                    public String Delivery_Lat;
                    public String Delivery_Long;
                }

                public String Tax;
                public String Delivery_Charges;
                public String Promo_Code;
                public String Discount_Amount;
                public String Grand_Total;
            }


        }
    }
}
