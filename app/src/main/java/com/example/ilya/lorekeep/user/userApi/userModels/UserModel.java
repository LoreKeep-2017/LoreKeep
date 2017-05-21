package com.example.ilya.lorekeep.user.userApi.userModels;


import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel{

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;

    public int getUserId(){
        return userId;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getPhonenumber(){
        return phonenumber;
    }

    public String getLogin(){
        return login;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public void setLogin(String login){
        this.login = login;
    }

}

