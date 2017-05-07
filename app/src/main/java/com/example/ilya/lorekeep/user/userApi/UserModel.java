package com.example.ilya.lorekeep.user.userApi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("username")
    @Expose
    private String username;

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

    public String getUsername(){
        return username;
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

    public void setUserId(int userId){
        this.userId = userId;
    }

    public void setUsername(String username){
        this.username = username;
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


}

