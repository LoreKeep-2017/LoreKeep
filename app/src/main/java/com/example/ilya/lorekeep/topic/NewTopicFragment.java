package com.example.ilya.lorekeep.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;

public class NewTopicFragment extends Fragment {

    public static TopicFragment newInstance() {
        return new TopicFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        HelperFactory.setHelper(getActivity().getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic, container,
                false);


        return v;
    }
}
