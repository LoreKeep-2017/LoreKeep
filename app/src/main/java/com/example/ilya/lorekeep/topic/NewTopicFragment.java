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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class NewTopicFragment extends Fragment {

    public static NewTopicFragment newInstance() {
        return new NewTopicFragment();
    }

    private Button mImageTopicButton;
    private EditText mTopicTitle;
    private TextView mCreateTopic;
    private ImageView mRemoveTopic;
    private Integer topicId;
    private Topic mTopic = new Topic();
    private Topic editTopic = new Topic();

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


        topicId = getActivity().getIntent().getIntExtra("topic_id", -1);

        if(topicId != -1) {
            try {
                editTopic = HelperFactory.getHelper().getTopicDAO().queryForId(topicId);
            } catch (SQLException e) {
                Log.d(TAG, "onClick: " + e.toString());
            }
        }


        mImageTopicButton = (Button) v.findViewById(R.id.set_topic_image);
        String topicImagePath = editTopic.getTopicImage();
        if(topicImagePath != null){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.parse(topicImagePath)));
                BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                mImageTopicButton.setBackground(bdrawable);
            } catch(FileNotFoundException e){
                // TODO : write catch
            }
        }

        mImageTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        mTopicTitle = (EditText) v.findViewById(R.id.set_topic_title);
        mTopicTitle.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mTopic.setTopicTitle(c.toString());
            }

            @Override
            public void afterTextChanged(Editable c) {

            }
        });

        Toolbar bottomToolbar = (Toolbar) v.findViewById(R.id.toolbar_topic_bottom);
        mRemoveTopic = (ImageView) bottomToolbar.findViewById(R.id.button_remove_topic);

        mRemoveTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HelperFactory.getHelper().getTopicDAO().deleteTopicById(topicId);
                }catch(SQLException e){
                    Log.d(TAG, "onClick: " + e.toString());
                }
                getActivity().finish();
            }
        });

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_topic_top);
        mCreateTopic = (TextView) toolbar.findViewById(R.id.toolbar_topic_create);

        mCreateTopic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Log.d("create topic", mTopic.toString());
                    HelperFactory.getHelper().getTopicDAO().setTopic(mTopic);
                }catch(SQLException e){
                    Log.d(TAG, "onClick: " + e.toString());
                }
                getActivity().finish();
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Log.d("New Topic Fragment", targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                mImageTopicButton.setBackground(bdrawable);
            } catch(FileNotFoundException e){}
            mImageTopicButton.setText("");
            mTopic.setTopicImage(targetUri.toString());
        }
    }

}
