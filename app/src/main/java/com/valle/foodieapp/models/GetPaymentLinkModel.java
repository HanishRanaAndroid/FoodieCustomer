package com.valle.foodieapp.models;

public class GetPaymentLinkModel {

    /*{
        "status": "OK",
            "data": {
        "id": "mNKc3Z",
                "name": "Cap",
                "amount_in_cents": 100,
                "currency": "COP",
                "single_use": false,
                "description": "Black, men's size",
                "sku": "WBXCH1",
                "expires_at": "2018-10-12 23:25:43 UTC",
                "collect_shipping": false,
                "redirect_url": null,
                "image_url": "https://bit.ly/2MBcBGH",
                "merchant_public_key": "pub_test_a28XA0JxOrPZeWP7a92na2",
                "created_at": "2018-07-01 23:49:45 UTC",
                "updated_at": "2018-07-01 23:49:45 UTC"
    }
    }*/

    public DataObject data;

    public class DataObject {
        public String id;
        public String name;
        public String amount_in_cents;
        public String redirect_url;
        public String merchant_public_key;
        public String created_at;
        public String updated_at;
    }
}

