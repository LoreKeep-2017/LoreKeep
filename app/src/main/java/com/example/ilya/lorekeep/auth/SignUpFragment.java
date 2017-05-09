package com.example.ilya.lorekeep.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ilya.lorekeep.R;


public class SignUpFragment extends Fragment {

    private static final String TITLE = "TITLE";
    private String title;

    public static SignUpFragment newInstance(String title) {
        SignUpFragment fragmentFirst = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        return root;
    }
}
