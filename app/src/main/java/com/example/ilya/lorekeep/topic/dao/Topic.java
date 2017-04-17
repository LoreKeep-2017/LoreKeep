package com.example.ilya.lorekeep.topic.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Topic")
public class Topic {

    @DatabaseField(generatedId = true)
    private int mId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String topicTitle;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] mImage;

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        this.mImage = image;
    }

    public Topic(){
        topicTitle = null;
        mImage = null;
    }

    public int getId() {
        return mId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setId(int id) {

        mId = id;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }
}
