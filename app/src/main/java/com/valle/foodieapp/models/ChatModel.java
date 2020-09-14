package com.valle.foodieapp.models;

import java.util.List;

public class ChatModel {

    /*{
        "status": "success",
            "response": {
        "msg": "Found",
                "response": [
        {
            "Id": "6",
                "Customer_Id": "80",
                "Delivery_Boy_Id": "89",
                "Customer_Message": "Hello",
                "Delivery_Boy_Message": null,
                "Created_Date": "2019-12-19 04:53:19"
        },
        {
            "Id": "7",
                "Customer_Id": "80",
                "Delivery_Boy_Id": "89",
                "Customer_Message": null,
                "Delivery_Boy_Message": "Hello",
                "Created_Date": "2019-12-19 04:53:27"
        }
        ]
    }
    }*/

    public responseData response;

    public class responseData {
        public List<MessagesList> response;

        public class MessagesList {
            public String Id;
            public String Customer_Id;
            public String Delivery_Boy_Id;
            public String Customer_Message;
            public String Delivery_Boy_Message;
            public String Created_Date;
        }
    }
}
