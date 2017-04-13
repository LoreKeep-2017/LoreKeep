package com.example.ilya.lorekeep.note.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Note")
public class Note {

    @DatabaseField(generatedId = true)
    public int mId;

    @DatabaseField(canBeNull = false)
    private int topicId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String noteTitle;

    @DatabaseField(dataType = DataType.STRING)
    private String noteDescription;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String note;

    public Note(){}

    public void setTopicId(){
        topicId = 1;
    }

    public void setNoteTitle(String title){
        this.noteTitle = title;
    }

    public void setNoteDecription(String decription){
        this.noteDescription = decription;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String getNoteTitle(){
        return this.noteTitle;
    }

    public String getNoteDescription(){
        return this.noteDescription;
    }

    public String getNote(){
        return this.note;
    }
}
