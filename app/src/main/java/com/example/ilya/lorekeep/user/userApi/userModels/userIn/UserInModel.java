package com.example.ilya.lorekeep.user.userApi.userModels.userIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInModel {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
