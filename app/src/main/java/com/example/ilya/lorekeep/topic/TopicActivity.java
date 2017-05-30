package com.example.ilya.lorekeep.topic;


import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.image_flickr.SearchFragment;
import com.example.ilya.lorekeep.topic.topicApi.TopicApi;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicAnswer;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//import static com.example.ilya.lorekeep.topic.NewTopicFragment.REQUEST_IMAGE;
//import static com.example.ilya.lorekeep.topic.NewTopicFragment.REQUEST_IMAGE_FRAGMENT;
//import static java.security.AccessController.getContext;

public class TopicActivity extends AppCompatActivity {

    public static final String TOPIC_ID = "topic_id";
    public static final String SERVER_TOPIC_ID = "server_topic_id";
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
    private TopicAdapter adapter;

    @Override
    @TargetApi(23)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        int buffer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("topic fragment", "" + buffer);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("topic fragment", "permission != check self");
            if (this.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //TODO: write asking permission
            } else {
                this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_READ_STORAGE);
            }
        }
        HelperFactory.setHelper(this.getApplicationContext());

        final SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.sharedTitle),
                Context.MODE_PRIVATE);
        userId = sharedPref.getInt(getString(R.string.userId), 1);

        sessionId = sharedPref.getString(getString(R.string.sessionId), "");

        /////////////////// Request for pulling all topic if user logged in first time //////////////

        topic = RetrofitFactory.retrofitLore().create(TopicApi.class);

        ///////////////////    Request for pulling deleted topics ///////////////////////////

//        deleteTopicsFromServer();


        ///////////////////    Request for pulling new topics ///////////////////////////

//        pullingNewTopics();

        ///////////////////    Request for created new topics ///////////////////////////

//        pushingNewTopics();

        ///////////////////    Request for updating topics ///////////////////////////

//        pushingUpdatedTopics();

        try {
            mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
        } catch (SQLException ex) {

        }
        Log.d("OnCreate", "mTopics size " + mTopics.size());
        adapter = new TopicAdapter(mTopics);
        onCreateView();
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

    public void onCreateView() {
        mTopicRecyclerView = (RecyclerView) findViewById(R.id.fragment_topic_recycler_view);

        DisplayMetrics displaymetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displaymetrics.widthPixels / displaymetrics.density;
        int columnsCount = (int) dpWidth / 130;
        mTopicRecyclerView.setLayoutManager(new GridLayoutManager
                (this, columnsCount));

        initToolbars();
    }

    private void initToolbars() {

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        ImageView mAddTopic = (ImageView) toolbarBottom.findViewById(R.id.add_topic);

        mAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicActivity.this, NewTopicActivity.class);
                startActivityForResult(intent, 200);
            }
        });

//        mSearchImage = (ImageView) toolbarBottom.findViewById(R.id.search_notes);
//        mSearchImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager manager = this.getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                Fragment searchFragment = SearchFragment.newInstance();
//                searchFragment.setTargetFragment(TopicFragment.this, REQUEST_IMAGE);
//                transaction.add(R.id.loading_text_fragment_search, searchFragment, REQUEST_IMAGE_FRAGMENT);
//                transaction.addToBackStack(null);
//                transaction.commit();
//
//
//            }
//        });
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

//                    mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
//                    setupAdapter();

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
                        if (result.get(i).getImage() != null) {
                            byte[] imageBytes = Base64.decode(result.get(i).getImage(), 0);

                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                            String imageUri = GlobalMethods.insertImage(getActivity().getContentResolver(), bitmap, "image", "imageTopic");

//                            mTopic.setTopicImage(imageUri);
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

    private void pushingUpdatedTopics() {

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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if (requestCode == 200 && resultCode == RESULT_OK) {
                mTopics.add(mTopics.size(), HelperFactory.getHelper().getTopicDAO().getLastAdded());
                adapter.notifyItemChanged(mTopics.size() - 1);
            } else if (requestCode == 300 && resultCode == RESULT_OK) {
                Log.d("OnActivityResult", "position " + intent.getIntExtra(TOPIC_ID, -1));
                int deletedPosition = intent.getIntExtra(TOPIC_ID, -1);
                if (deletedPosition != -1) {
                    Log.d("onActivityResult", "deleted position " + deletedPosition);
                    adapter.setDeletedTopic(deletedPosition);
                    adapter.notifyItemRemoved(deletedPosition);
                }
            }
        } catch (SQLException ex) {

        }
    }

    private void setupAdapter() {
        mTopicRecyclerView.setAdapter(adapter);
    }

    private class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mTopicButton;
        int topicPosition;
        int serverTopicId;
        private String title;

        public TopicHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.topic_item, container, false));
            mTopicButton = (Button) itemView.findViewById(R.id.list_item_topic_button);
//            mTopicButton.setGravity(Gravity.CENTER);
            mTopicButton.setOnClickListener(this);
            mTopicButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(TopicActivity.this, NewTopicActivity.class);
                    intent.putExtra(TOPIC_ID, topicPosition);
                    intent.putExtra(SERVER_TOPIC_ID, serverTopicId);
                    intent.putExtra(TOPIC_UPDATE, true);
                    startActivityForResult(intent, 300);
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
                    bitmap = BitmapFactory.decodeStream(TopicActivity.this.getContentResolver()
                            .openInputStream(Uri.parse(imageUri)));
                    BitmapDrawable bdrawable = new BitmapDrawable(TopicActivity.this.getResources(), bitmap);
                    mTopicButton.setBackground(bdrawable);
                } catch (FileNotFoundException e) {
                }
            }
            if (color != 0) {
                mTopicButton.setBackgroundColor(color);
            }
            title = item.getTopicTitle();
            mTopicButton.setText(title);
            serverTopicId = item.getServerTopicId();
            topicPosition = getAdapterPosition();

            Animation animation = AnimationUtils.loadAnimation(TopicActivity.this, android.R.anim.slide_in_left);
            animation.setDuration(700);
            mTopicButton.startAnimation(animation);
        }

        public void removeView(){
            ((ViewManager)mTopicButton.getParent()).removeView(mTopicButton);
        }

        public View getButton() {
            return mTopicButton;
        }

        @Override
        public void onClick(View v) {
            //TODO change topicID
            Intent intent = NoteActivity.newIntent(TopicActivity.this, serverTopicId);
            intent.putExtra("topic", title);
            startActivity(intent);
        }
    }

    private class TopicAdapter extends RecyclerView.Adapter<TopicHolder> {
        private List<Topic> mTopicItems;
        private int deletedTopic = -1;

        public TopicAdapter(List<Topic> topics) {
            mTopicItems = topics;
        }

        @Override
        public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(TopicActivity.this);
            return new TopicHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(final TopicHolder topicHolder, int position) {
            if(deletedTopic == -1) {
                Topic topic = mTopicItems.get(position);
                topicHolder.bindTopicItem(topic);

                Animation animation = AnimationUtils.loadAnimation(TopicActivity.this, android.R.anim.slide_in_left);
                animation.setDuration(700);
                topicHolder.getButton().startAnimation(animation);
            } else {
                Animation animation = AnimationUtils.loadAnimation(TopicActivity.this, android.R.anim.slide_out_right);
                animation.setDuration(700);
                topicHolder.getButton().startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        topicHolder.removeView();
                        mTopicItems.remove(deletedTopic);
                        deletedTopic = -1;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mTopicItems.size();
        }

        public void setDeletedTopic(int deletedTopic){
            this.deletedTopic = deletedTopic;
        }
    }

    @Override
    public void onDestroy() {
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

}
