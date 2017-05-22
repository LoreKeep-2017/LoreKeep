package com.example.ilya.lorekeep.note.dao;

import com.example.ilya.lorekeep.topic.dao.Topic;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class NoteImpl extends BaseDaoImpl<Note, Integer> {

    public NoteImpl(ConnectionSource connectionSource, Class<Note> note) throws SQLException{
        super(connectionSource, note);
    }

    public List<Note> getAllNotes() throws SQLException{
        return this.queryForAll();
    }

    public List<Note> getAllNotesByTopicId(Topic topic) throws SQLException{
        QueryBuilder<Note, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Note.NOTES_FILED_TOPIC, topic);
        PreparedQuery<Note> preparedQuery = queryBuilder.prepare();
        List<Note> noteList =query(preparedQuery);

        return noteList;
    }

    public Note getNoteByServerTopicId(Integer serverNoteId) throws SQLException{
        return this.queryBuilder().where().eq("serverTopicId", serverNoteId).queryForFirst();
    }

    public void updateServerNoteId(int noteId, int serverNoteId){
        try{
            UpdateBuilder<Note, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("noteId", noteId);
            updateBuilder.updateColumnValue("serverNoteId", serverNoteId);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void updateNote(Note note) throws SQLException{
        this.update(note);
    }

    public void updateCreated(int noteId){
        try {
            UpdateBuilder<Note, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("noteId", noteId);
            updateBuilder.updateColumnValue("created", false);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void setNewNote(Note note) throws SQLException{
        this.create(note);
    }
}
