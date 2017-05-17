package com.example.ilya.lorekeep.topic.dao;

import android.provider.ContactsContract;
import android.renderscript.Element;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "Topic")
public class Topic {

    @DatabaseField(generatedId = true)
    private int topicId;

    @DatabaseField(dataType = DataType.INTEGER)
    private int serverTopicId;

    @DatabaseField(dataType = DataType.INTEGER)
    private int userId;

    @DatabaseField(dataType = DataType.STRING)
    private String title;

    @DatabaseField(dataType = DataType.STRING)
    private String image;

    @DatabaseField(dataType = DataType.INTEGER)
    private int color;

    @DatabaseField(dataType = DataType.DATE)
    private Date creationDate;

    @DatabaseField(dataType = DataType.DATE)
    private Date lastUsed;

    @DatabaseField(dataType = DataType.INTEGER)
    private int rating;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean changed;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean created;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean deleted;


    public Topic() {}

    public void setTopicUserId(int userId) {
        this.userId = userId;
    }

    public void setServerTopicId(int serverTopicId){
        this.serverTopicId = serverTopicId;
    }

    public void setTopicTitle(String title) {
        this.title = title;
    }

    public void setTopicImage(String image) {
        this.image = image;
    }

    public void setTopicColor(int color) {
        this.color = color;
    }

    public void setTopicCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setTopicLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setTopicRating(int rating) {
        this.rating = rating;
    }

    public void setTopicChanged(boolean changed) {
        this.changed = changed;
    }

    public void setTopicCreated(boolean created){
        this.created = created;
    }

    public void setTopicDeleted(boolean deleted){
        this.deleted = deleted;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getServerTopicId() {
        return serverTopicId;
    }

    public int getTopicUserId() {
        return userId;
    }

    public String getTopicTitle() {
        return title;
    }

    public String getTopicImage() {
        return image;
    }

    public int getTopicColor() {
        return color;
    }

    public Date getTopicCreationDate() {
        return creationDate;
    }

    public Date getTopicLastUsed() {
        return lastUsed;
    }

    public int getTopicRating() {
        return rating;
    }

    public boolean getTopicChanged() {
        return changed;
    }

    public boolean getTopicCreated(){
        return created;
    }

    public boolean getTopicDeleted(){
        return deleted;
    }
}
