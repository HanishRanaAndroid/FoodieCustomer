package com.valle.foodieapp.models;

public class CreateOrderModel {
    /*{
        "status": "success",
            "response": {
        "msg": "El pedido ha sido reservado",
                "orders_Info": {
            "Id": "60",
                    "Customer_Id": "95",
                    "Delivery_Boy_Id": null,
                    "Rest_Id": "103",
                    "Order_Number": "VF281295103ON3807",
                    "Order_Status": "Order Placed",
                    "Delivery_Time": null,
                    "Grand_Total": "200",
                    "Payment_Type": "Cash",
                    "Payment_Status": "Paid",
                    "Payment_History": null,
                    "Ordered_Items": {
                "Items": [
                {
                    "Id": "28",
                        "Quantity": "1",
                        "Item_Name": "Chicken Teriyaki Sub",
                        "Item_Category": "SERPIENTES",
                        "Item_Price": "14",
                        "Item_Image": "fb0bd24cc19a8fb2bb2352a38d679cf23.jpg"
                },
                {
                    "Id": "25",
                        "Quantity": "2",
                        "Item_Name": "Hara Bhara Kebab Sub",
                        "Item_Category": "SERPIENTES",
                        "Item_Price": "12",
                        "Item_Image": "fb0bd24cc19a8fb2bb2352a38d679cf2.jpg"
                }
                ],
                "Delivery_Address": {
                    "Map_Address": "Plot No 10, IT Park,, Phase - I, Manimajra, Chandigarh, Haryana 160101, India  India",
                            "Address": "827testing address again",
                            "Delivery_Lat": "30.723665335428635",
                            "Delivery_Long": "76.8464057520032"
                },
                "Tax": "13",
                        "Delivery_Charges": "50"
            },
            "Wishlist": "No",
                    "Custom_Note": "add extra spices",
                    "Is_Rating": "0",
                    "createdDtm": "2019-12-30 04:09:13"
        }
    }
    }*/

    public ResponseData response;

    public class ResponseData {
        public orders_InfoData orders_Info;

        public class orders_InfoData {
            public String Id;
            public String Rest_Id;
        }
    }
}
