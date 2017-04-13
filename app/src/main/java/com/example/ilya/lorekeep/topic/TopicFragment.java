package com.example.ilya.lorekeep.topic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.sql.SQLException;
import java.util.List;


public class TopicFragment extends Fragment {

    private RecyclerView mTopicRecyclerView;
    private List<Topic> mTopics;

    public static TopicFragment newInstance() {
        return new TopicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        HelperFactory.setHelper(getActivity().getApplicationContext());

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
            Log.d("on create", "query lenght: " + mTopics.size());
            HelperFactory.getHelper().getTopicDAO().setTopic();
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
            Log.d("on create", "query lenght: " + mTopics.size());
        } catch (SQLException e) {
            Log.e("on create", "fail to get query");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic, container,
                false);
        mTopicRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_topic_recycler_view);
        mTopicRecyclerView.setLayoutManager(new GridLayoutManager
                (getActivity(), 3));
        setupAdapter();
        return v;
    }


    private void setupAdapter() {
        if (isAdded()) {
            mTopicRecyclerView.setAdapter(new TopicAdapter(mTopics));
        }
    }

    private class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        private TextView mTitleTextView;
        private Button mTopicButton;
        Drawable mDrawable;

        public TopicHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.topic_item, container, false));
            mTopicButton = (Button) itemView.findViewById(R.id.list_item_topic_button);
            mTopicButton.setOnClickListener(this);
        }

        public void bindTopicItem(Topic item) {
            mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bill_up_close, null);
            mTopicButton.setText(item.toString());
            mTopicButton.setBackground(mDrawable);
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
