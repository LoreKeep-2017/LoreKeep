package com.example.ilya.lorekeep.topic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.global_methods.GlobalMethods;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.image_flickr.SearchFragment;
import com.example.ilya.lorekeep.topic.topicApi.TopicApi;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicAnswer;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.ilya.lorekeep.topic.NewTopicFragment.REQUEST_IMAGE;
import static com.example.ilya.lorekeep.topic.NewTopicFragment.REQUEST_IMAGE_FRAGMENT;

public class TopicFragment extends Fragment {

    public static final String TOPIC_ID = "topic_id";
    public static final String TOPIC_CREATE = "topic_create";
    public static final String TOPIC_UPDATE = "topic_update";
    private String TAG = "TopicActivity";
    private final static int MY_PERMISSION_READ_STORAGE = 1;

    private ImageView mSearchImage;
    private TopicApi topic;
    private RecyclerView mTopicRecyclerView;
    private List<Topic> mTopics;
    private int userId;
    private String sessionId;

    public static TopicFragment newInstance() {
        return new TopicFragment();
    }

    @Override
    @TargetApi(23)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        int buffer = ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("topic fragment", "" + buffer);
        if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("topic fragment", "permission != check self");
            if (this.getActivity().shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //TODO: write asking permission
            } else {
                this.getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_READ_STORAGE);
            }
        }
        HelperFactory.setHelper(getActivity().getApplicationContext());

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedTitle),
                Context.MODE_PRIVATE);
        userId = sharedPref.getInt(getString(R.string.userId), 1);

        sessionId = sharedPref.getString(getString(R.string.sessionId), "");

        /////////////////// Request for pulling all topic if user logged in first time //////////////

        topic = RetrofitFactory.retrofitLore().create(TopicApi.class);

        ///////////////////    Request for pulling deleted topics ///////////////////////////

        deleteTopicsFromServer();


        ///////////////////    Request for pulling new topics ///////////////////////////

        pullingNewTopics();

        ///////////////////    Request for created new topics ///////////////////////////

//        pushingNewTopics();

        ///////////////////    Request for updating topics ///////////////////////////

//        pushingUpdatedTopics();

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
        } catch (SQLException ex) {

        }
        setupAdapter();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("topic fragment", "in case read_storage");
                } else {

                }
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

        initToolbars(v);

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

        mSearchImage = (ImageView) toolbarBottom.findViewById(R.id.search_notes);
        mSearchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    Fragment searchFragment = SearchFragment.newInstance();
                    searchFragment.setTargetFragment(TopicFragment.this, REQUEST_IMAGE);
                    transaction.add(R.id.loading_text_fragment_search, searchFragment, REQUEST_IMAGE_FRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();


            }
        });
    }

    private void deleteTopicsFromServer() {
        final Call<List<Integer>> callSynDelTopic = topic.getChagesDelTopic("sessionId=" + sessionId);
        NetworkThread.getInstance().execute(callSynDelTopic, new NetworkThread.ExecuteCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> result, Response<List<Integer>> response) {
                try {
                    Log.d("on update deleteSer", "result size " + result.size());
                    for (int i = 0; i < result.size(); ++i) {
                        HelperFactory.getHelper().getTopicDAO().deleteTopicById(result.get(i));
                    }

                    mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
                    setupAdapter();

                } catch (SQLException e) {
                    Log.e("on failed deleteSer", "fail to get query");
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.d("onError deleteSer", "Error " + ex.toString());
            }
        });
    }

    private void pullingNewTopics() {

        final Call<List<TopicModel>> callSyn = topic.getChanges("sessionId=" + sessionId);
        NetworkThread.getInstance().execute(callSyn, new NetworkThread.ExecuteCallback<List<TopicModel>>() {
            @Override
            public void onSuccess(List<TopicModel> result, Response<List<TopicModel>> response) {
                try {
                    Log.d("on update pullingSer", "result size " + result.size());
                    for (int i = 0; i < result.size(); ++i) {
                        Topic mTopic = new Topic();
                        mTopic.setTopicUserId(userId);
                        mTopic.setTopicTitle(result.get(i).getTitle());
                        mTopic.setServerTopicId(result.get(i).getTopicId());
                        mTopic.setTopicChanged(false);
                        mTopic.setTopicCreated(false);
                        mTopic.setTopicDeleted(false);
                        if(result.get(i).getImage() != null) {
                            byte[] imageBytes = Base64.decode(result.get(i).getImage(), 0);

                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            String imageUri = GlobalMethods.insertImage(getActivity().getContentResolver(), bitmap, "image", "imageTopic");

                            mTopic.setTopicImage(imageUri);
                        }

                        mTopic.setTopicCreationDate(new Date());
                        Topic isTopic = HelperFactory.getHelper().getTopicDAO().getTopicByServerTopicId(mTopic.getServerTopicId());
                        if (isTopic == null) {
                            HelperFactory.getHelper().getTopicDAO().setTopic(mTopic);
                        } else {
                            isTopic.setTopicTitle(mTopic.getTopicTitle());
                            isTopic.setTopicColor(mTopic.getTopicColor());
                            HelperFactory.getHelper().getTopicDAO().updateTopic(isTopic);
                        }
                    }
                    mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
                    setupAdapter();

                } catch (SQLException e) {
                    Log.e("on create", "fail to get query");
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.d("onError pullingSer", "Error " + ex.toString());
            }
        });
    }

    private void pushingNewTopics() {

        int createdCount;
        try {
            createdCount = HelperFactory.getHelper().getTopicDAO().getCreatedCount();
        } catch (SQLException ex) {
            createdCount = -1;
        }
        Log.d("Topic fragment", "created count = " + createdCount);
        if (createdCount > 0) {
            List<Topic> createdTopics;
            try {
                createdTopics = HelperFactory.getHelper().getTopicDAO().getCreatedTopics();
                Log.d("Topic fragment", "created ot send" + createdTopics.size());
                for (int i = 0; i < createdCount; ++i) {
                    final TopicModel newTopic = new TopicModel();
                    newTopic.setUserId(userId);
                    newTopic.setTopicId(createdTopics.get(i).getTopicId());
                    newTopic.setTitle(createdTopics.get(i).getTopicTitle());
                    newTopic.setCreationDate(createdTopics.get(i).getTopicCreationDate());
                    newTopic.setColor(createdTopics.get(i).getTopicColor());

                    final Call<TopicAnswer> call = topic.createNewTopic(newTopic, "sessionId=" + sessionId);
                    NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<TopicAnswer>() {
                        @Override
                        public void onSuccess(TopicAnswer result, Response<TopicAnswer> response) {
                            try {
                                Log.d("on create", "Created id" + newTopic.getTopicId());
                                HelperFactory.getHelper().getTopicDAO().updateCreated(newTopic.getTopicId());
                                Log.d("on create", "created count " + HelperFactory.getHelper().getTopicDAO().getCreatedCount());
                            } catch (SQLException e) {
                                Log.e("on create", "fail to get query");
                            }
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("onError", "Error " + ex.toString());
                        }
                    });
                }
            } catch (SQLException ex) {
                //TODO write exception
            }
        }

    }

    private void pushingUpdatedTopics(){

        int updateCount;
        try {
            updateCount = HelperFactory.getHelper().getTopicDAO().getChangedCount();
        } catch (SQLException ex) {
            updateCount = -1;
        }
        Log.d("Topic fragment", "update count = " + updateCount);
        if (updateCount > 0) {
            List<Topic> createdTopics;
            try {
                createdTopics = HelperFactory.getHelper().getTopicDAO().getChangedTopics();
                Log.d("Topic fragment", "created ot send" + createdTopics.size());
                for (int i = 0; i < updateCount; ++i) {
                    final TopicModel newTopic = new TopicModel();
                    newTopic.setUserId(userId);
                    newTopic.setTopicId(createdTopics.get(i).getTopicId());
                    newTopic.setTitle(createdTopics.get(i).getTopicTitle());
                    newTopic.setCreationDate(createdTopics.get(i).getTopicCreationDate());
                    newTopic.setColor(createdTopics.get(i).getTopicColor());

                    final Call<TopicAnswer> call = topic.updateTopic(newTopic, "sessionId=" + sessionId);
                    NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<TopicAnswer>() {
                        @Override
                        public void onSuccess(TopicAnswer result, Response<TopicAnswer> response) {
                            try {
                                Log.d("on create", "Created id" + newTopic.getTopicId());
                                HelperFactory.getHelper().getTopicDAO().updateChanged(newTopic.getTopicId());
                                Log.d("on create", "created count " + HelperFactory.getHelper().getTopicDAO().getCreatedCount());
                            } catch (SQLException e) {
                                Log.e("on create", "fail to get query");
                            }
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("onError", "Error " + ex.toString());
                        }
                    });
                }
            } catch (SQLException ex) {
                //TODO write exception
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
        } catch (Exception e) {
            Log.d("Error", e.toString());
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
        private String title;

        public TopicHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.topic_item, container, false));
            mTopicButton = (Button) itemView.findViewById(R.id.list_item_topic_button);
            mTopicButton.setOnClickListener(this);
            mTopicButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(getActivity(), NewTopicActivity.class);
                    intent.putExtra(TOPIC_ID, topicId);
                    intent.putExtra(TOPIC_UPDATE, true);
                    startActivity(intent);
                    return true;
                }
            });
        }

        public void bindTopicItem(Topic item) {
            String imageUri = item.getTopicImage();
            Integer color = item.getTopicColor();
            if (imageUri != null) {
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                            .openInputStream(Uri.parse(imageUri)));
                    BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                    mTopicButton.setBackground(bdrawable);
                } catch (FileNotFoundException e) {
                }
            }
            if (color != 0) {
                mTopicButton.setBackgroundColor(color);
            }
            title = item.getTopicTitle();
            mTopicButton.setText(title);
            topicId = item.getTopicId();
        }

        @Override
        public void onClick(View v) {
            Intent intent = NoteActivity.newIntent(getContext(), topicId);
            intent.putExtra("topic", title);
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
