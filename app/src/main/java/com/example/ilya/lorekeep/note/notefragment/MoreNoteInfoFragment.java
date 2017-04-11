package com.example.ilya.lorekeep.note.notefragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MoreNoteInfoFragment extends Fragment {

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    public static MoreNoteInfoFragment newInstance(int position) {

        Bundle args = new Bundle();

        MoreNoteInfoFragment fragment = new MoreNoteInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setBackgroundColor(Color.GRAY);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(getContext());
        title.setLayoutParams(params);

        TextView content = new TextView(getContext());
        content.setLayoutParams(params);

        params.setMargins(20,20,20,20);
        layout.setLayoutParams(params);

        title.setText("OLOLOLOL Title OLololol0");
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        content.setText("OLOLOLLOLO Content OLOLOLOLOL");
        content.setGravity(Gravity.CENTER);
        content.setTextColor(Color.WHITE);
        layout.addView(content);

        return layout;

    }
}
