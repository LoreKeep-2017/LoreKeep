package com.example.ilya.lorekeep.topic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.ilya.lorekeep.topic.image.CapturePhotoUtils;
import com.example.ilya.lorekeep.topic.image.FlickrFetchr;
import com.example.ilya.lorekeep.topic.image.FlickrItem;
import com.example.ilya.lorekeep.topic.image.ThumbnailDownloader;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class NewTopicFragment extends Fragment {

    static final int PICK_COLOR_REQUEST = 1;

    public static NewTopicFragment newInstance() {
        return new NewTopicFragment();
    }

    private Button mImageTopicButton;
    private RecyclerView mPhotoRecyclerView;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private List<FlickrItem> mItems = new ArrayList<>();
    private EditText mTopicTitle;
    private EditText mSearchImage;
    private TextView mCreateTopic;

    private ImageView mRemoveTopic;
    private ImageView mColorPicker;

    private Integer topicId;
    private Integer color;
    private Topic mTopic = new Topic();
    private Topic editTopic = new Topic();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        HelperFactory.setHelper(getActivity().getApplicationContext());

//        updateItems();
        Handler responseHandler = new Handler();

        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);

        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder,
                                                      Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(),
                                bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_topic, container,
                false);


        topicId = getActivity().getIntent().getIntExtra("topic_id", -1);

        if (topicId != -1) {
            try {
                editTopic = HelperFactory.getHelper().getTopicDAO().queryForId(topicId);
            } catch (SQLException e) {
                Log.d(TAG, "onClick: " + e.toString());
            }
        }


        mImageTopicButton = (Button) v.findViewById(R.id.set_topic_image);
        String topicImagePath = editTopic.getTopicImage();
        if (topicImagePath != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.parse(topicImagePath)));
                BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                mImageTopicButton.setBackground(bdrawable);
            } catch (FileNotFoundException e) {
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
        mTopicTitle.addTextChangedListener(new TextWatcher() {
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
                } catch (SQLException e) {
                    Log.d(TAG, "onClick: " + e.toString());
                }
                getActivity().finish();
            }
        });

        mColorPicker = (ImageView) bottomToolbar.findViewById(R.id.button_color_picker);
        mColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ColorPicker.class);
                startActivityForResult(intent, PICK_COLOR_REQUEST);
            }
        });

        mSearchImage = (EditText) bottomToolbar.findViewById(R.id.flickr_search_image);
        mSearchImage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                Log.d(TAG, "QueryTextSubmit: " + c.toString());
                updateItems(c.toString());
            }

            @Override
            public void afterTextChanged(Editable c) {

            }
        });


        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager
                (getActivity(), 3));

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_topic_top);
        mCreateTopic = (TextView) toolbar.findViewById(R.id.toolbar_topic_create);

        mCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("create topic", mTopic.toString());
                    HelperFactory.getHelper().getTopicDAO().setTopic(mTopic);
                } catch (SQLException e) {
                    Log.d(TAG, "onClick: " + e.toString());
                }
                getActivity().finish();
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupAdapter();

        return v;
    }

    private void updateItems(String query) {
        new FetchItemsTask(query).execute();
    }


    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_COLOR_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                color = ColorPicker.getColor(data);
                mImageTopicButton.setBackgroundColor(color);
                mTopic.setTopicColor(color);
            }
        }

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Log.d("New Topic Fragment", targetUri.toString());
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                    mImageTopicButton.setBackground(bdrawable);
                } catch (FileNotFoundException e) {
                }
                mImageTopicButton.setText("");
                mTopic.setTopicImage(targetUri.toString());
            }
        }
    }


    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImageView;
        private Drawable mDrawable;

        public PhotoHolder(View ImageView) {
            super(ImageView);
            mItemImageView = (ImageView) itemView
                    .findViewById(R.id.fragment_photo_gallery_image_view);
            mItemImageView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
            mDrawable = drawable;
        }

        @Override
        public void onClick(View v) {

            Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
            BitmapDrawable bdrawable = new BitmapDrawable(getContext().getResources(), bitmap);
            mImageTopicButton.setBackground(bdrawable);
            mImageTopicButton.setText("");

            String targetUri = CapturePhotoUtils.insertImage(getActivity().getContentResolver(), bitmap, "hell", "hell");
            mTopic.setTopicImage(targetUri);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<FlickrItem> mGalleryItems;

        public PhotoAdapter(List<FlickrItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup,
                                              int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.flickr_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            FlickrItem galleryItem = mGalleryItems.get(position);
            Drawable placeholder = getResources().getDrawable
                    (R.drawable.bill_up_close);
            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder,
                    galleryItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<FlickrItem>> {

        private String mQuery = null;

        public FetchItemsTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<FlickrItem> doInBackground(Void... params) {

            if (mQuery == null) {
//                return new FlickrFetchr().fetchRecentPhotos();
                return null;
            } else {
                return new FlickrFetchr().searchPhotos(mQuery);
            }
        }

        @Override
        protected void onPostExecute(List<FlickrItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }
}
