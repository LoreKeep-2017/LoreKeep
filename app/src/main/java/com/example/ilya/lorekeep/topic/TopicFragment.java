package com.example.ilya.lorekeep.topic;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.io.FileNotFoundException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.jar.Manifest;


public class TopicFragment extends Fragment {

    public static final String TOPIC_ID = "topic_id";
    private String TAG = "TopicActivity";
    private final static int MY_PERMISSION_READ_STORAGE = 1;

    private RecyclerView mTopicRecyclerView;
    private List<Topic> mTopics;

    public static TopicFragment newInstance()
    {
        return new TopicFragment();
    }

    @Override
    @TargetApi(23)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        HelperFactory.setHelper(getActivity().getApplicationContext());

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
            Log.d("on create", "query lenght: " + mTopics.size());
        } catch (SQLException e) {
            Log.e("on create", "fail to get query");
        }
    }


    @TargetApi(23)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic, container,
                false);
        mTopicRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_topic_recycler_view);
        mTopicRecyclerView.setLayoutManager(new GridLayoutManager
                (getActivity(), 3));
//        int buffer = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        Log.d("topic fragment", "" + buffer);
//        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.d("topic fragment", "permission != check self");
//                getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                        MY_PERMISSION_READ_STORAGE);
//
//        }
        initToolbars(v);
//        setupAdapter();

        return v;
    }

    private void initToolbars(View v) {

        Toolbar toolbarBottom = (Toolbar) v.findViewById(R.id.toolbar_bottom);
        ImageView mAddTopic = (ImageView) toolbarBottom.findViewById(R.id.add_topic);

        mAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewTopicActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
            Log.d("on create", "query lenght: " + mTopics.size());
        } catch (SQLException e) {
            Log.e("on create", "fail to get query");
        }

        setupAdapter();

        Log.d(TAG, "onResume: create new thread and execute!");
    }

    private void setupAdapter() {
        if (isAdded()) {
            mTopicRecyclerView.setAdapter(new TopicAdapter(mTopics));
        }
    }

    private class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mTopicButton;
        Integer topicId;

        public TopicHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.topic_item, container, false));
            mTopicButton = (Button) itemView.findViewById(R.id.list_item_topic_button);
            mTopicButton.setOnClickListener(this);
            mTopicButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), NewTopicActivity.class);
                    intent.putExtra(TOPIC_ID, topicId);
                    startActivity(intent);
                    return true;
                }
            });
        }

        @TargetApi(23)
        public void bindTopicItem(Topic item) {

//            String imageUri = item.getTopicImage();
////            getContext().grantUriPermission("Gallery",Uri.parse(imageUri),Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            if(imageUri != null) {
//                Bitmap bitmap;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
//                            .openInputStream(Uri.parse(imageUri)));
//                    BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
//                    mTopicButton.setBackground(bdrawable);
//                } catch (FileNotFoundException e) {
//                }
//            }
//            if(item.getImage() != null){
//                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
//                BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
//                mTopicButton.setBackground(bdrawable);
//            }
////            mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bill_up_close, null);
            mTopicButton.setText(item.getTopicTitle());
////            mTopicButton.setBackground(mDrawable);
            topicId = item.getTopicId();
        }



        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), NoteActivity.class);
            startActivity(intent);
        }
    }

    private class TopicAdapter extends RecyclerView.Adapter<TopicHolder> {
        private List<Topic> mTopicItems;

        public TopicAdapter(List<Topic> topics) {
            mTopicItems = topics;
        }

        @Override
        public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TopicHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(TopicHolder topicHolder, int position) {
            Topic topic = mTopicItems.get(position);
            topicHolder.bindTopicItem(topic);
        }

        @Override
        public int getItemCount() {
            return mTopicItems.size();
        }
    }

    @Override
    public void onDestroy() {
        HelperFactory.releaseHelper();
        super.onDestroy();
    }


}
