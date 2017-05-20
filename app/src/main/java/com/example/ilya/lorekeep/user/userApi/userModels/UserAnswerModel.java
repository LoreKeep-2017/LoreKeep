package com.example.ilya.lorekeep.user.userApi.userModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAnswerModel {

    @SerializedName("level")
    @Expose
    private String level;

    @SerializedName("message")
    @Expose
    private Message messageClass;

//    @SerializedName("message")
//    @Expose
//    private String message;

    public String getLevel(){
        return level;
    }

//    public String getMessage(){
//        return message;
//    }


    public Message getMessageClass() {
        return messageClass;
    }

    public String getLogin(){
        return messageClass.getLogin();
    }

    public String getSessionId(){
        return messageClass.getSessionId();
    }

    public int getUserId(){
        return messageClass.getUserId();
    }

    public int getId(){
        return messageClass.getId();
    }

    private class Message{

        @SerializedName("login")
        @Expose
        private String login;

        @SerializedName("sessionId")
        @Expose
        private String sessionId;

        @SerializedName("userId")
        @Expose
        private int userId;

        @SerializedName("id")
        @Expose
        private int id;

        public String getLogin(){
            return login;
        }

        public String getSessionId(){
            return sessionId;
        }

        public int getUserId(){
            return userId;
        }

        public int getId(){
            return id;
        }

    }
}
