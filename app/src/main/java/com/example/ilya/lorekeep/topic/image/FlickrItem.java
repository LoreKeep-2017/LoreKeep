package com.example.ilya.lorekeep.topic.image;


public class FlickrItem {
    private String mCaption;
    private String mId;
    private String mUrl;
    @Override
    public String toString() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getCaption() {

        return mCaption;
    }

    public String getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }
}
