package com.valle.foodieapp.models;

public abstract class DataModel {

    public abstract void mian(LoginModel loginModel);


    public void work(){
        /*Do any thing here*/
        LoginModel  loginModel=new LoginModel();
        mian(loginModel);
    }
}
