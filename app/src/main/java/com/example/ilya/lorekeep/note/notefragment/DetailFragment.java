package com.example.ilya.lorekeep.note.notefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;


public class DetailFragment extends Fragment {
    private TextView linkText;
    private TextView containText;

    public static DetailFragment newInstance(String link, String contain) {

        Bundle args = new Bundle();
        args.putString("link", link);
        args.putString("contain", contain);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_note_full_description, container, false);
        linkText = (TextView) root.findViewById(R.id.full_link);
        containText = (TextView) root.findViewById(R.id.full_contain);

        return root;
    }

    public void setNewArgs(String link, String contain) {
        linkText.setText(link);
        containText.setText(contain);
    }

    public void setPosition(int id, int position){
        LinearLayout l = (LinearLayout) getView().findViewById(id);
        l.addView(this.getView(), position);

    }
}
