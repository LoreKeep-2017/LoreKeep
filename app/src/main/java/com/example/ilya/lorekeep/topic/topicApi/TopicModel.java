package com.example.ilya.lorekeep.topic.topicApi;

import com.example.ilya.lorekeep.user.userApi.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TopicModel {

    @SerializedName("topicId")
    @Expose
    private int topicId;

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
    private String color;

    @SerializedName("changed")
    @Expose
    private boolean changed;

    public int getTopicId(){
        return topicId;
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

    public String getColor(){
        return color;
    }

    public boolean getChanged(){
        return changed;
    }

    public void setTopicId(int topicId){
        this.topicId = topicId;
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

    public void setColor(String color){
        this.color = color;
    }

    public void setChanged(boolean changed){
        this.changed = changed;
    }

}
