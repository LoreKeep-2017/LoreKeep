package com.example.ilya.lorekeep.dbexecutor;

import android.util.Log;

import com.example.ilya.lorekeep.Ui;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteList;
import com.example.ilya.lorekeep.note.dao.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorCreateNote {

    private String TAG = "executorCreateNote";
    private List<Note> notes = NoteList.getInstance().notes;

    private static final ExecutorCreateNote CREATE_NOTE = new ExecutorCreateNote();
    public static ExecutorCreateNote getInstance() {
        return CREATE_NOTE;
    }
    private final Executor executor = Executors.newCachedThreadPool();
    private ExecutorCreateNote.Callback callback;

    public interface Callback {
        void onCreate();
    }

    public void setCallback(ExecutorCreateNote.Callback callback) {
        this.callback = callback;
    }

    public void create(final String title, final String content, final String link) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Note newNote = new Note();
                    newNote.setNoteTitle(title);
                    newNote.setNoteDecription(content);
                    newNote.setNote(link);
                    HelperFactory.getHelper().getNoteDao().setNewNote(newNote);
                    notes.add(newNote);
                    Log.e(TAG, "run: put in cache!!!" );
                } catch (SQLException e) {
                    Log.e("in create link", e.toString());
                }

                notifyLoaded();
            }
        });
    }

    protected void notifyLoaded() {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onCreate();
                }
                Log.d(TAG, "run: notify UI");
            }
        });
    }
}
