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

    public Topic(){
        topicTitle = "Java";
    }

}
