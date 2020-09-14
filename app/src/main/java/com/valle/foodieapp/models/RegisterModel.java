package com.valle.foodieapp.models;

public class RegisterModel {

   /* {
        "status": "success",
            "response": {
        "sms_response": {
            "numbers": null,
                    "text": "Thank you for join ValleFood. Please verify your account. Your OTP is 75734. Please Do Not share this OTP with anyone."
        },
        "mail_response": "<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<title>404 Page Not Found</title>\r\n<style type=\"text/css\">\r\n\r\n::selection { background-color: #E13300; color: white; }\r\n::-moz-selection { background-color: #E13300; color: white; }\r\n\r\nbody {\r\n\tbackground-color: #fff;\r\n\tmargin: 40px;\r\n\tfont: 13px/20px normal Helvetica, Arial, sans-serif;\r\n\tcolor: #4F5155;\r\n}\r\n\r\na {\r\n\tcolor: #003399;\r\n\tbackground-color: transparent;\r\n\tfont-weight: normal;\r\n}\r\n\r\nh1 {\r\n\tcolor: #444;\r\n\tbackground-color: transparent;\r\n\tborder-bottom: 1px solid #D0D0D0;\r\n\tfont-size: 19px;\r\n\tfont-weight: normal;\r\n\tmargin: 0 0 14px 0;\r\n\tpadding: 14px 15px 10px 15px;\r\n}\r\n\r\ncode {\r\n\tfont-family: Consolas, Monaco, Courier New, Courier, monospace;\r\n\tfont-size: 12px;\r\n\tbackground-color: #f9f9f9;\r\n\tborder: 1px solid #D0D0D0;\r\n\tcolor: #002166;\r\n\tdisplay: block;\r\n\tmargin: 14px 0 14px 0;\r\n\tpadding: 12px 10px 12px 10px;\r\n}\r\n\r\n#container {\r\n\tmargin: 10px;\r\n\tborder: 1px solid #D0D0D0;\r\n\tbox-shadow: 0 0 8px #D0D0D0;\r\n}\r\n\r\np {\r\n\tmargin: 12px 15px 12px 15px;\r\n}\r\n</style>\r\n</head>\r\n<body>\r\n\t<div id=\"container\">\r\n\t\t<h1>404 Page Not Found</h1>\r\n\t\t<p>The page you requested was not found.</p>\t</div>\r\n</body>\r\n</html>",
                "msg": "New User created successfully.",
                "UserInfo": {
            "User_Id": "78",
                    "Full_Name": "Developer Team ",
                    "Restaurant_Name": "Developer",
                    "Email": "test@blissitsolutions.com",
                    "Mobile": "9876543210",
                    "City": "Panchkula",
                    "Address": "Sector 5",
                    "Website": null,
                    "Latitude": "30.727921",
                    "Longitude": "76.839661",
                    "Role": "Restaurant",
                    "Login_Type": "Default",
                    "Profile_Image": null,
                    "Cover_Image": null,
                    "Refer_Code": "BL6990",
                    "Validation_Code": "75734",
                    "Is_Active": "InActive",
                    "Device_Token": "Device_Token",
                    "createdDtm": "2019-12-11 09:50:46"
        }
    }
    }*/

    public responseData response;

    public class responseData {
        public String msg;
        public LoginModel.responseData.UserInfoData UserInfo;
    }
}
