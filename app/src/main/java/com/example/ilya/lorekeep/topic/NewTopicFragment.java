package com.example.ilya.lorekeep.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;

import static android.content.ContentValues.TAG;

public class NewTopicFragment extends Fragment {

    public static NewTopicFragment newInstance() {
        return new NewTopicFragment();
    }

    private Button mImageTopicButton;
    private EditText mTopicTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        HelperFactory.setHelper(getActivity().getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_topic, container,
                false);

        Log.d(TAG, "onCreateView: " + (Button) v.findViewById(R.id.set_topic_image));
        mImageTopicButton = (Button) v.findViewById(R.id.set_topic_image);
//        mImageTopicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        if (mImageTopicButton == null) {
            System.out.println("nulllasjfajdf;a;dfja;dsf");
        }

        mTopicTitle = (EditText) v.findViewById(R.id.set_topic_title);
//        mTopicTitle.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_topic_top);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return v;
    }



}
