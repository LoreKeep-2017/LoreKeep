package com.example.ilya.lorekeep.user.userApi.userModels.userUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserUpAnswerModel {

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("message")
    @Expose
    private Message message;

    public String getLevel(){
        return level;
    }

    public int getMessage(){
        return message.getId();
    }

    private class Message{

        @SerializedName("id")
        @Expose
        private int id;

        public int getId(){
            return id;
        }


    }
}

