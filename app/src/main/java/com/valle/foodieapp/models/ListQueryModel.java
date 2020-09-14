package com.valle.foodieapp.models;

import java.util.List;

public class ListQueryModel {
/*
    {
        "status": "success",
            "response": {
        "msg": "",
                "Items": [
        {
            "Id": "1",
                "User_Id": "103",
                "Token_Number": "",
                "Type": "gdfg",
                "Order_Id": [],
            "Message": "sdfsdfs",
                "Status": "sdfsdf",
                "Action": ""
        }
        ]
    }
    }*/


    public ResponseData response;

    public class ResponseData {

        public List<ItemsData> Items;

        public class ItemsData {
            public String Id;
            public String User_Id;
            public String Token_Number;
            public String Type;
            public String Message;
            public String Status;
        }

    }

}
