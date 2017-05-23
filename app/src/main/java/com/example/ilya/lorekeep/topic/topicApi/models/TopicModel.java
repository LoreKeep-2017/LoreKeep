package com.example.ilya.lorekeep.topic.topicApi.models;

import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteModel;
import com.example.ilya.lorekeep.user.dao.User;
import com.example.ilya.lorekeep.user.userApi.userModels.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopicModel {

    @SerializedName("topicId")
    @Expose
    private int topicId;

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("notes")
    @Expose
    private List<NoteModel> notes;

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("serverTopicId")
    @Expose
    private int serverTopicId;

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

    @SerializedName("imageBitmap")
    @Expose
    private byte[] imageBitmap;

    public void setNotes(List<NoteModel> notes) {
        this.notes = notes;
    }

    public List<NoteModel> getNotes() {

        return notes;
    }

    public void setServerTopicId(int serverTopicId) {
        this.serverTopicId = serverTopicId;
    }

    public int getServerTopicId() {

        return serverTopicId;
    }

    public boolean isChanged() {
        return changed;
    }

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

    public byte[] getImageBitmap(){
        return imageBitmap;
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

    public void setImageBitmap(byte[] imageBitmap){
        this.imageBitmap = imageBitmap;
    }

}
