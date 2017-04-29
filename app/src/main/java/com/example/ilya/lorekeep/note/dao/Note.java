package com.example.ilya.lorekeep.note.dao;

import android.provider.ContactsContract;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Note")
public class Note {

    @DatabaseField(generatedId = true)
    public int noteId;

    @DatabaseField(canBeNull = false)
    private int topicId;

    @DatabaseField(dataType = DataType.STRING)
    private String comment;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String content;

    @DatabaseField(dataType = DataType.STRING)
    private String url;

    @DatabaseField(dataType = DataType.DATE)
    private Date creation_date;

    @DatabaseField(dataType = DataType.DATE)
    private Date last_used;

    @DatabaseField(dataType = DataType.INTEGER)
    private int rating;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean changed;

    public Note(){}

    public void setTopicId(){
        topicId = 1;
    }

    public void setNoteComment(String comment){
        this.comment = comment;
    }

    public void setNoteContent(String content){
        this.content = content;
    }

    public void setNoteUrl(String url){
        this.url = url;
    }

    public void setNoteCreationDate(Date create_date){
        this.creation_date = create_date;
    }

    public void setNoteLastUsed(Date last_used){
        this.last_used = last_used;
    }

    public void setNoteRating(int rating){
        this.rating = rating;
    }

    public void setNoteChanged(boolean changed){
        this.changed = changed;
    }

    public String getNoteComment(){
        return comment;
    }

    public String getNoteContent(){
        return content;
    }

    public String getNoteUrl(){
        return url;
    }

    public Date getNoteCreationDate(){
        return creation_date;
    }

    public Date getNoteLastUsed(){
        return last_used;
    }

    public int getNoteRating(){
        return rating;
    }

    public boolean getNoteChanged(){
        return changed;
    }
}
