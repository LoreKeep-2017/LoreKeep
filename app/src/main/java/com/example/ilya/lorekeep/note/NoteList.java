package com.example.ilya.lorekeep.note;


import com.example.ilya.lorekeep.note.dao.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteList {
    private static NoteList ourInstance = new NoteList();

    public static NoteList getInstance() {
        return ourInstance;
    }

    public List<Note> notes = new ArrayList<Note>();

}
