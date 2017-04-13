package com.example.ilya.lorekeep.dbexecutor;

import android.util.Log;

import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.dao.Note;

import java.sql.SQLException;

public class executorCreateNote implements Runnable {

    private String title;
    private String content;
    private String link;
    private String TAG = "executorCreateNote";

    public executorCreateNote(String title, String content, String link){
        this.title = title;
        this.content = content;
        this.link = link;
    }
    @Override
    public void run() {
        //this sleep only to show gui not blocking
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Note newNote = new Note();
            newNote.setNoteTitle(this.title);
            newNote.setNoteDecription(this.content);
            newNote.setNote(this.link);
            HelperFactory.getHelper().getNoteDao().setNewNote(newNote);
        } catch (SQLException e) {
            Log.e("in create link", e.toString());
        }

    }
}
