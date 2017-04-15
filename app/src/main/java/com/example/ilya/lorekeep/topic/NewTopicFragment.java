package com.example.ilya.lorekeep.topic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.TextView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class NewTopicFragment extends Fragment {

    public static NewTopicFragment newInstance() {
        return new NewTopicFragment();
    }

    private Button mImageTopicButton;
    private EditText mTopicTitle;
    private TextView mCreateTopic;

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

        mImageTopicButton = (Button) v.findViewById(R.id.set_topic_image);
        mImageTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });


        mTopicTitle = (EditText) v.findViewById(R.id.set_topic_title);
        mTopicTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_topic_top);
        mCreateTopic = (TextView) toolbar.findViewById(R.id.toolbar_topic_create);

        mCreateTopic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(),bitmap);
                mImageTopicButton.setBackground(bdrawable);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
