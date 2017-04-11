package com.example.ilya.lorekeep.note.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
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

    public void setNewNote(Note note) throws SQLException{
        this.create(note);
    }
}
