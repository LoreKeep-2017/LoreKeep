package com.example.ilya.lorekeep.note.noteApi.noteModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoteAnswer {

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
