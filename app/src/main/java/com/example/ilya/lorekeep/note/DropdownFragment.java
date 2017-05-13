package com.example.ilya.lorekeep.note;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.note.dao.Note;


public class DropdownFragment extends Fragment {

    public static final String EXTRA_NOTE =
            "extra_note";

    private TextView mTextView;
    private Button mButtonUrl;

    public static Fragment newInstance(Note note) {
        Fragment f = new DropdownFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NOTE, note);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_note, container, false);

        Note note = (Note) getArguments().getSerializable(EXTRA_NOTE);

        if(note.getNoteUrl() != null) {
            mButtonUrl = (Button) v.findViewById(R.id.button_open_browser);
            mButtonUrl.setText("Go to url: " + note.getNoteUrl());
        }

        mTextView = (TextView) v.findViewById(R.id.fragment_test);
        mTextView.setText(note.getNoteContent());


        return v;
    }
}
