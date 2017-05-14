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
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.topicApi.TopicApi;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicAnswer;
import com.example.ilya.lorekeep.topic.topicApi.models.TopicModel;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TopicFragment extends Fragment {

    public static final String TOPIC_ID = "topic_id";
    private String TAG = "TopicActivity";
    private final static int MY_PERMISSION_READ_STORAGE = 1;

    private RecyclerView mTopicRecyclerView;
    private List<Topic> mTopics;

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
                //TODO: write asking permision
            } else {
                this.getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_READ_STORAGE);
            }
        }
        HelperFactory.setHelper(getActivity().getApplicationContext());

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedTitle),
                Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(getString(R.string.userId), 1);

        final TopicApi topic = RetrofitFactory.retrofitLore().create(TopicApi.class);
        final Call<List<TopicModel>> call = topic.getAllTopics(userId);
        NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<List<TopicModel>>() {
            @Override
            public void onSuccess(List<TopicModel> result, Response<List<TopicModel>> response) {
                int size = result.toArray().length;
                Log.d("onSuccess TopicFragment", "Success " + size);
                try {
                    mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
                    Log.d("on create", "query lenght: " + mTopics.size());
                } catch (SQLException e) {
                    Log.e("on create", "fail to get query");
                }
                setupAdapter();
            }

            @Override
            public void onError(Exception ex) {
                Log.d("onError", "Error " + ex.toString());
                try {
                    mTopics = HelperFactory.getHelper().getTopicDAO().getAllTopics();
                    Log.d("on create", "query lenght: " + mTopics.size());
                } catch (SQLException e) {
                    Log.e("on create", "fail to get query");
                }
            }
        });
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

        //
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(getString(R.string.sharedTitle), Context.MODE_PRIVATE);

        Log.d(TAG, "onCreateView: !!!!!!!!!!!sesid: " + sharedPreferences.getString(getString(R.string.sessionId), ""));

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

//        setupAdapter();

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
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), NewTopicActivity.class);
                    intent.putExtra(TOPIC_ID, topicId);
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
            if(color != 0){
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
