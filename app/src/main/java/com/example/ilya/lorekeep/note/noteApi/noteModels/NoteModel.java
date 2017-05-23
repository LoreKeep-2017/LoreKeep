package com.example.ilya.lorekeep.note.noteApi.noteModels;


import android.os.Parcelable;

import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class NoteModel{

    @SerializedName("noteId")
    @Expose
    private int noteId;

    @SerializedName("topicId")
    @Expose
    private int topicId;

    @SerializedName("serverTopicId")
    @Expose
    private int serverTopicId;


    @SerializedName("serverNoteId")
    @Expose
    private int serverNoteId;

    @SerializedName("topic")
    @Expose
    private TopicModel topic;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("url")
    @Expose
    private String url;

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

    @SerializedName("changed")
    @Expose
    private boolean changed;

    public void setServerNoteId(int serverNotecId) {
        this.serverNoteId = serverNotecId;
    }

    public int getServerNoteId() {

        return serverNoteId;
    }

    public void setServerTopicId(int serverTopicId) {
        this.serverTopicId = serverTopicId;
    }

    public int getServerTopicId() {

        return serverTopicId;
    }

    public TopicModel getTopic() {
        return topic;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public void setTopic(TopicModel topic) {
        this.topic = topic;
    }

    public boolean isChanged() {

        return changed;
    }

    public int getNoteId(){
        return noteId;
    }

    public int getTopicId(){
        return topicId;
    }

    public String getComment(){
        return comment;
    }

    public String getContent(){
        return content;
    }

    public String getUrl(){
        return url;
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

    public boolean getChanged(){
        return changed;
    }

    public void setNoteId(int noteId){
        this.noteId = noteId;
    }

    public void setTopic(int topicId){
        this.topicId = topicId;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setUrl(String url){
        this.url = url;
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

    public void setChanged(boolean changed){
        this.changed = changed;
    }

}
