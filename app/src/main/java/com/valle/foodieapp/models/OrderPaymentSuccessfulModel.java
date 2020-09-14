package com.valle.foodieapp.models;

import java.util.List;

public class OrderPaymentSuccessfulModel {
    /*{
        "status": "success",
            "response": {
        "msg": "Encontró",
                "orders_Info": [
        {
            "Id": "78",
                "Customer_Id": {
            "User_Id": "95",
                    "Full_Name": "customer ho me",
                    "Restaurant_Name": "",
                    "Email": "customerhome@gmail.com",
                    "Mobile": "9988998899",
                    "City": "",
                    "Address": "",
                    "Website": null,
                    "Latitude": "",
                    "Longitude": "",
                    "Role": "Customer",
                    "Login_Type": "Default",
                    "Set_Your_Presence": "",
                    "Profile_Image": "IMG_20191227_175905_8131731305066107486.jpg",
                    "Cover_Image": null,
                    "Overall_Rating": "0.0",
                    "Discount_Code": null,
                    "Discount_Percentage": null,
                    "Refer_Code": "VF5667",
                    "Validation_Code": "",
                    "Is_Active": "Active",
                    "Device_Token": "",
                    "createdDtm": "2019-12-20 15:13:10"
        },
            "Delivery_Boy_Id": {},
            "Rest_Id": {
            "User_Id": "103",
                    "Full_Name": "Subway",
                    "Restaurant_Name": "Subway",
                    "Email": "subway@gmail.com",
                    "Mobile": "65656154544",
                    "City": "Chandigarh",
                    "Address": "Sector 5",
                    "Website": null,
                    "Latitude": "30.7279221",
                    "Longitude": "76.839661",
                    "Role": "Restaurant",
                    "Login_Type": "Default",
                    "Set_Your_Presence": "ON",
                    "Profile_Image": "f91994fdd564bb3aa3e067b977cd6d591.jpg",
                    "Cover_Image": "3bf2fc01bf677b2ae1c2040f135df0b05.jpg",
                    "Overall_Rating": "1.0",
                    "Discount_Code": "SUB30",
                    "Discount_Percentage": "30",
                    "Refer_Code": "VF8681",
                    "Validation_Code": "",
                    "Is_Active": "Active",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-27 13:13:19"
        },
            "Order_Number": "ON951030",
                "Order_Status": "Order Placed",
                "Delivery_Time": null,
                "Grand_Total": "77",
                "Payment_Type": "Online",
                "Payment_Status": "Paid",
                "Payment_History": {
            "id": "11158-1577791792-65295",
                    "created_at": "2019-12-31T11:29:52.154Z",
                    "amount_in_cents": 175000,
                    "reference": "test_IbFM0P_1577791723869_z38z27aqq4q",
                    "customer_email": "test@gmail.com",
                    "currency": "COP",
                    "payment_method_type": "CARD",
                    "payment_method": {
                "type": "CARD",
                        "extra": {
                    "bin": "424242",
                            "name": "VISA-4242",
                            "brand": "VISA",
                            "exp_year": "25",
                            "exp_month": "05",
                            "last_four": "4242",
                            "external_identifier": "98gYY3iayR"
                },
                "installments": 1
            },
            "status": "APPROVED",
                    "status_message": null,
                    "shipping_address": null,
                    "redirect_url": "http://weburlforclients.com/gamma/vallafood/api/v1/PaymentCallback/ON951030",
                    "payment_source_id": null,
                    "payment_link_id": "test_IbFM0P",
                    "customer_data": {
                "full_name": "Tester ",
                        "phone_number": "+571234567890"
            },
            "bill_id": null,
                    "merchant": {
                "name": "Valle Food",
                        "legal_name": "Luis Enrique Martinez Tovar",
                        "contact_name": "Luis Enrique Martinez Tovar",
                        "phone_number": "+573043777993",
                        "logo_url": null,
                        "legal_id_type": "CC",
                        "email": "lemt83@yahoo.es",
                        "legal_id": "14621631"
            },
            "entries": [
            {
                "amount_in_cents": -14181,
                    "concept": "TRANSACTION_FEE_VAT"
            },
            {
                "amount_in_cents": -74637,
                    "concept": "TRANSACTION_FEE"
            },
            {
                "amount_in_cents": 175000,
                    "concept": "PAYMENT"
            }
                    ],
            "disbursement": null
        },
            "Ordered_Items": {
            "Items": [
            {
                "Id": "28",
                    "Quantity": "1",
                    "Item_Name": "Chicken Teriyaki Sub",
                    "Item_Category": "SERPIENTES",
                    "Item_Price": "14",
                    "Item_Image": "fb0bd24cc19a8fb2bb2352a38d679cf23.jpg"
            }
                    ],
            "Delivery_Address": {
                "Map_Address": "Plot No 10, IT Park,, Phase - I, Manimajra, Chandigarh, Haryana 160101, India  India",
                        "Address": "999testing locators ",
                        "Delivery_Lat": "30.72367225264152",
                        "Delivery_Long": "76.84637792408466"
            },
            "Tax": "13",
                    "Delivery_Charges": "50"
        },
            "Wishlist": "No",
                "Custom_Note": "",
                "Is_Rating": {},
            "createdDtm": "2019-12-31 04:29:56"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData {
        public List<orders_InfoData> orders_Info;

        public class orders_InfoData {
            public Payment_HistoryData Payment_History;

            public class Payment_HistoryData {
                public String status;
                public String reference;
                public String payment_method_type;
            }
        }
    }
}
