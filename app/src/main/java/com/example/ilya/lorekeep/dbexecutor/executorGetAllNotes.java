package com.example.ilya.lorekeep.dbexecutor;


import android.support.v4.util.LruCache;
import android.util.Log;

import com.example.ilya.lorekeep.Ui;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.dao.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class executorGetAllNotes {

    private static final executorGetAllNotes GET_ALL_NOTES = new executorGetAllNotes();

    public static executorGetAllNotes getInstance() {
        return GET_ALL_NOTES;
    }

    public interface Callback {
        void onLoaded(List<Note> notelist);
    }

    private final Executor executor = Executors.newCachedThreadPool();
    private LruCache<Integer, Note> cache = new LruCache<>(32);
    private Callback callback;
    private List<Note> noteList;
    private String TAG = "executorGetAllNotes";

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void load() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: getAllNotes in new thread!");
                try {
                    noteList = HelperFactory.getHelper().getNoteDao().getAllNotes();
                } catch (SQLException e) {
                    Log.e(TAG, "run: ", e);
                }

                notifyLoaded(noteList);
            }
        });
    }

    private void notifyLoaded(final List<Note> notelist) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (notelist != null) {
                    for(Note note: notelist){
                        cache.put(note.mId, note);
                    }
                }
                if (callback != null) {
                    callback.onLoaded(notelist);
                }
                Log.d(TAG, "run: notify UI");
            }
        });
    }
}
