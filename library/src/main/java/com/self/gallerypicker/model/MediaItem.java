package com.self.gallerypicker.model;

import android.net.Uri;

/**
 * Created by user on 4/21/15.
 */
public class MediaItem {

    private long mId;
    private Uri mUri;
    private boolean mIsVideo;


    public MediaItem() {
    }

    public MediaItem(long id, Uri uri, boolean isVideo) {
        mId = id;
        mUri = uri;
        mIsVideo = isVideo;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public boolean isVideo() {
        return mIsVideo;
    }

    public void setIsVideo(boolean isVideo) {
        mIsVideo = isVideo;
    }

    @Override
    public String toString() {
        return mUri.toString();
    }
}
