package com.self.gallerypicker.logic;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.self.gallerypicker.model.MediaItem;

import java.lang.ref.WeakReference;

/**
 * Created by user on 4/21/15.
 */
public class MediaItemWrapper {

    private MediaItem mMediaItem;
    private WeakReference<Bitmap> mThumbnail = new WeakReference<Bitmap>(null);
    private ContentResolver mContentResolver;
    private int mPosition;
    private ThumbnailLoaderTask mThumbnailLoaderTask;


    public MediaItemWrapper(Context context, MediaItem mediaItem) {
        mMediaItem = mediaItem;
        mContentResolver = context.getContentResolver();
    }


    public MediaItem getMediaItem() {
        return mMediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        mMediaItem = mediaItem;
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void loadThumbnailAsync(Callback callback){
        mThumbnailLoaderTask = new ThumbnailLoaderTask(this, callback);
        mThumbnailLoaderTask.execute();
    }

    public void cancelLoadingThumbnail(){
        if(mThumbnailLoaderTask != null){
            mThumbnailLoaderTask.cancel(true);
        }
    }

    public Bitmap getThumbnail(){
        return mThumbnail.get();
    }

    public Bitmap loadThumbnail(){
        Bitmap thumbnail = mThumbnail.get();
        if(thumbnail == null){
            if(mMediaItem.isVideo()) {
                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, mMediaItem.getId(), MediaStore.Video.Thumbnails.MINI_KIND, null);
            } else {
                thumbnail = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, mMediaItem.getId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
            }
            mThumbnail = new WeakReference<Bitmap>(thumbnail);
        }
        return thumbnail;
    }

    private class ThumbnailLoaderTask extends AsyncTask<Void, Void, Void>{

        private Callback mCallback;
        private MediaItemWrapper mMediaItemWrapper;

        public ThumbnailLoaderTask(MediaItemWrapper mediaItemWrapper, Callback callback) {
            mCallback = callback;
            mMediaItemWrapper = mediaItemWrapper;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMediaItemWrapper.loadThumbnail();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallback != null){
                mCallback.onThumbnailReady(mMediaItemWrapper);
            }
        }
    }

    public interface Callback {
        void onThumbnailReady(MediaItemWrapper mediaItemWrapper);
    }
}
