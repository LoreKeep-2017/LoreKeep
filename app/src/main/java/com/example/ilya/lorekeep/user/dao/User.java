package com.example.ilya.lorekeep.user.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "User")
public class User {

    @DatabaseField(generatedId = true)
    private int userId;

    @DatabaseField(dataType = DataType.STRING)
    private String username;

    @DatabaseField(dataType = DataType.STRING)
    private String password;

    @DatabaseField(dataType = DataType.STRING)
    private String email;

    @DatabaseField(dataType = DataType.STRING)
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
