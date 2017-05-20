package com.example.ilya.lorekeep.topic.topicApi;


import com.example.ilya.lorekeep.user.userApi.userModels.UserAnswerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicAnswerDelModel {

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("message")
    @Expose
    private String message;

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {

        return level;
    }

    public String getMessage() {
        return message;
    }
}
