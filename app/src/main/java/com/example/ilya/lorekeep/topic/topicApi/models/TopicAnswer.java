package com.example.ilya.lorekeep.topic.topicApi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicAnswer {

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("message")
    @Expose
    private int message;

    public String getLevel(){
        return level;
    }

    public int getMessage(){
        return message;
    }

}


