package com.example.ilya.lorekeep.note;


import com.example.ilya.lorekeep.note.dao.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteList {
    private static NoteList ourInstance = new NoteList();

    public static NoteList getInstance() {
        return ourInstance;
    }

    public Map<Integer, List<Note>>  mlNotes = new HashMap<>();

}
