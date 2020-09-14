package com.valle.foodieapp.models;

import java.util.List;

public class SecondSliderModel {
//    {
//        "status": "success",
//            "response": [
//        {
//            "id": "1",
//                "rest_id": "174",
//                "item_id": "84",
//                "User_Id": "174",
//                "Full_Name": "Las Delicias De Pipe",
//                "Restaurant_Name": "Las Delicias de Pipe",
//                "Email": "lasdeliciasdepipe@gmail.com",
//                "Mobile": "3176807833",
//                "Password": "$2y$10$SAvT0VbTZulzIA6AFppQuO/.eJUR50yOUNdRP02ymTquzL3fI0ODa",
//                "City": "Cali",
//                "Address": "Calle 84 #22-162, Cali, Valle del Cauca, Colombia",
//                "Website": "",
//                "Latitude": "3.436450100000001",
//                "Longitude": "-76.4693446",
//                "Role": "Restaurant",
//                "Login_Type": "Default",
//                "Set_Your_Presence": "OFF",
//                "Order_WithIN": "0",
//                "Profile_Image": "Logo_del_restaurante1.jpeg",
//                "Cover_Image": "5.jpg",
//                "Overall_Rating": "4.4",
//                "Discount_Code": "10pipe",
//                "Discount_Percentage": "10",
//                "ForSlider": "1",
//                "ForsliderTwo": "0",
//                "Refer_Code": "VF9275",
//                "Validation_Code": "5973",
//                "Is_Active": "Active",
//                "Device_Token": "dFm4cKv6Y40:APA91bHjaIww_kIl1wGtvzOXIIXSjtwVhLnNXcb7LoW4ZK6mq-DWqiTyOkjOWgedYI1ZNG91WKeAA0Ff2IdtDe6aabyeT4E6nwY1HMgjm47TgKoKtzkRUw7b-R6Rhuex69flemxv0U0d",
//                "createdDtm": "2020-01-31 08:43:37",
//                "itemData": {
//            "Id": "84",
//                    "Rest_Id": "174",
//                    "Item_Name": "Filete de pollo",
//                    "Item_Category": "ASADOS",
//                    "Item_Flavor": null,
//                    "Item_Price": "16000",
//                    "Item_Description": "Filete con porción de papa a la francesa.",
//                    "Item_Image": "Filete_de_pollo_y_cerdo3.jpg",
//                    "Item_Status": "In Stock",
//                    "Discount_Percentage": "0",
//                    "Created_Date": "2020-02-08 13:45:56"
//        }
//        }
//    ]
//    }

    public List<responseData> response;

    public class responseData {
        public String rest_id;
        public String Restaurant_Name;
        public String Overall_Rating;
        public String Profile_Image;
        public String Discount_Code;
        public String Discount_Percentage;
        public String Order_WithIN;
        public String Is_Active;
        public String Cover_Image;
        public itemDataObject itemData;

        public class itemDataObject {
            public String Id;
            public String Item_Name;
            public String Item_Category;
            public String Item_Price;
            public String Item_Image;
            public String Item_Status;
        }
    }
}
