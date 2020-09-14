package com.valle.foodieapp.models;

import java.util.List;

public class AddressModel {
    /*{
        "status": "success",
            "response": {
        "msg": "Dirección de entrega encontrada",
                "Items": [
       "msg" {
            "Id": "1",
                "Customer_Id": "80",
                "Address1": "House no 123",
                "Address2": "Sector 15, Chandigarh",
                "Landmark": "Near SBI bank",
                "Latitude": "30.7565",
                "Longitude": "76.4547",
                "Is_Primary": "0",
                "Type": "Home"
        }
        ]
    }
    }*/


    public responseData response;

    public class responseData {
        public List<ItemsData> Items;

        public class ItemsData {
            public String Id;
            public String Customer_Id;
            public String Address1;
            public String Address2;
            public String Landmark;
            public String Latitude;
            public String Longitude;
            public String Is_Primary;
            public String Type;

        }
    }

}
