package com.example.ilya.lorekeep.topic.topicApi.models;

import java.util.Date;

public class TopicCreateModel {

    private int userId;
    private String title;
    private Date creationDate;
    private String color;

    public TopicCreateModel(int userId, String title, Date creationDate, String color){
        this.userId = userId;
        this.title = title;
        this.creationDate = creationDate;
        this.color = color;
    }

}
