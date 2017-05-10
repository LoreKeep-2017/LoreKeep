package com.example.ilya.lorekeep.topic.topicApi.models;

import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TopicModel {

    @SerializedName("topicId")
    @Expose
    private int topicId;

    private int userId;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("creationDate")
    @Expose
    private Date creationDate;

    @SerializedName("lastUsed")
    @Expose
    private Date lastUsed;

    @SerializedName("color")
    @Expose
    private int color;

    @SerializedName("changed")
    @Expose
    private boolean changed;

    public int getTopicId(){
        return topicId;
    }

    public int getUserId(){
        return userId;
    }

    public UserModel getUser(){
        return user;
    }

    public String getTitle(){
        return title;
    }

    public String getImage(){
        return image;
    }

    public int getRating(){
        return rating;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public Date getLastUsed(){
        return lastUsed;
    }

    public int getColor(){
        return color;
    }

    public boolean getChanged(){
        return changed;
    }

    public void setTopicId(int topicId){
        this.topicId = topicId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public void setUser(UserModel user){
        this.user = user;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }

    public void setLastUsed(Date lastUsed){
        this.lastUsed = lastUsed;
    }

    public void setColor(int color){
        this.color = color;
    }

    public void setChanged(boolean changed){
        this.changed = changed;
    }

}
