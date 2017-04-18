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

public class ExecutorGetAllNotes {

    private static final ExecutorGetAllNotes GET_ALL_NOTES = new ExecutorGetAllNotes();

    public static ExecutorGetAllNotes getInstance() {
        return GET_ALL_NOTES;
    }

    public interface Callback {
        void onLoaded();
    }


    private final Executor executor = Executors.newCachedThreadPool();
    private Callback callback;
    private List<Note> notes = NoteList.getInstance().notes;
    private String TAG = "executorGetAllNotes";

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void load() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: getAllNotes in new thread!");
                if (notes.size() == 0){
                    Log.e(TAG, "run: without cache! cache size" + notes.size() );
                    try {
                        notes.addAll(HelperFactory.getHelper().getNoteDao().getAllNotes());
                    } catch (SQLException e) {
                        Log.e(TAG, "run: ", e);
                    }
                } else {
                    Log.e(TAG, "run: with cache size: "+ notes.size() );
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
                    callback.onLoaded();
                }
                Log.d(TAG, "run: notify UI");
            }
        });
    }
}
