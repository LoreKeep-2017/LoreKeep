package com.example.ilya.lorekeep.dbexecutor;


import android.util.Log;

import com.example.ilya.lorekeep.Ui;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteList;
import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorGetAllNotes {

    private static ExecutorGetAllNotes GET_ALL_NOTES = null;

    private final Executor executor = Executors.newCachedThreadPool();
    private Callback callback;
    private Map<Integer, List<Note>> mlNotes = NoteList.getInstance().mlNotes;
    private String TAG = "executorGetAllNotes";
    private Integer topicId;

    public ExecutorGetAllNotes(){
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public static ExecutorGetAllNotes getInstance() {

        if(GET_ALL_NOTES == null){
            GET_ALL_NOTES = new ExecutorGetAllNotes();
        }
        return GET_ALL_NOTES;
    }

    public interface Callback {
        void onLoaded();
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void load() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: getAllNotes in new thread!");
                if(mlNotes.get(topicId) == null){
                    List<Note> notes = new ArrayList<Note>();
                    mlNotes.put(topicId, notes);
                }
                if (mlNotes.get(topicId).size() == 0){
                    Log.e(TAG, "run: without cache! cache size" + mlNotes.get(topicId).size() );
                    try {
                        Topic topic = HelperFactory.getHelper().getTopicDAO().queryForId(topicId);
                        mlNotes.get(topicId).addAll(HelperFactory.getHelper().getNoteDao().getAllNotesByTopicId(topic));
                    } catch (SQLException e) {
                        Log.e(TAG, "run: ", e);
                    }
                } else {
                    Log.e(TAG, "run: with cache size: "+ mlNotes.get(topicId).size() );
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
