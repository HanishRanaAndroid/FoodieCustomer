package com.valle.foodieapp.models;

import java.util.List;

public class OrderUpdateModel {

    /*{
        "status": "success",
            "response": {
        "msg": "Found!",
                "orders_Info": [
        {
            "Id": "6",
                "Customer_Id": {
            "User_Id": "1",
                    "Full_Name": "Admin",
                    "Restaurant_Name": null,
                    "Email": "admin@admin.com",
                    "Mobile": "1231231231",
                    "City": null,
                    "Address": null,
                    "Website": null,
                    "Latitude": null,
                    "Longitude": null,
                    "Role": "Admin",
                    "Login_Type": "Default",
                    "Set_Your_Presence": "",
                    "Profile_Image": null,
                    "Cover_Image": null,
                    "Overall_Rating": null,
                    "Refer_Code": null,
                    "Validation_Code": null,
                    "Is_Active": "Active",
                    "Device_Token": null,
                    "createdDtm": "2019-09-23 13:42:03"
        },
            "Delivery_Boy_Id": {},
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
                    "Overall_Rating": "3",
                    "Refer_Code": "BL7610",
                    "Validation_Code": "34666",
                    "Is_Active": "Active",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-09 13:39:17"
        },
            "Order_Number": "ON1312177VF8920",
                "Order_Status": "Order Delivered",
                "Delivery_Time": "15",
                "Grand_Total": "",
                "Payment_Type": "",
                "Payment_Status": "",
                "Payment_History": null,
                "Ordered_Items": {
            "Items": [
            {
                "Id": "5",
                    "Rest_Id": "77",
                    "Item_Name": "VEG MANCHURIAN GRAVY",
                    "Item_Category": "Chinese",
                    "Item_Price": "1200",
                    "Item_Description": "Lorem Ipsam",
                    "Item_Image": "",
                    "Created_Date": "2019-12-12 03:45:07"
            }
                    ],
            "Tax": "10",
                    "Delivery_Charges": "20",
                    "Promo_Code": "AV20",
                    "Discount_Amount": "52",
                    "Grand_Total": "200"
        },
            "Wishlist": "No",
                "createdDtm": "2019-12-14 05:14:33"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData{
        public List<OrderPlacedModel.responseData.orders_InfoData> orders_Info;
    }
}
