package com.self.gallerypicker.logic;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.self.gallerypicker.model.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/18/15.
 */
public class GalleryQuery {


//    public void querySdMediaItemListAsync(Context context, QueryCallback<List<MediaItemWrapper>> queryCallback){
//        new MediaItemQueryAsyncTask(context, queryCallback).execute();
//    };

    public void querySdMediaItemListAsync(Context context, QueryCallback<List<MediaItem>> queryCallback){
        new MediaItemQueryAsyncTask(context, queryCallback).execute();
    };

    public ArrayList<MediaItem> querySdImageMedaiItemPathList(Context context) {
        ArrayList<MediaItem> imagePathList = new ArrayList<MediaItem>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        int columnIndexData = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        int columnIndexFolderName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int columnIndexId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(columnIndexData);
            long id = cursor.getLong(columnIndexId);

            imagePathList.add(new MediaItem(id, Uri.parse(imagePath), false));
        }
        return imagePathList;
    }

//    private void getVideos() {
//        final String[] videoColumns = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Media.DATA};
//        final String orderBy = MediaStore.Video.Media._ID;
//        Cursor videoCursor = activity.managedQuery(
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns,
//                null, null, orderBy);
//        if (videoCursor != null) {
//            int videoColumnIndex = videoCursor.getColumnIndex(MediaStore.Video.Media._ID);
//            int count = videoCursor.getCount();
//            for (int i = 0; i < count; i++) {
//                videoCursor.moveToPosition(i);
//                int id = videoCursor.getInt(videoColumnIndex);
//                String path = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                ImageItem imageItem = new ImageItem(id, path);
//                imageItem.setVideo(true);
//                imageItems.put(i + 1, imageItem);
//            }
//            videoCursor.close();
//        }
//    }

    private abstract class QueryAsyncTask<T> extends AsyncTask<Void, Void, T>{

        private final Context mContext;
        private QueryCallback mQueryCallback;

        public QueryAsyncTask(Context context, QueryCallback<T> queryCallback) {
            mQueryCallback = queryCallback;
            mContext = context;
        }

        @Override
        protected void onPostExecute(T strings) {
            super.onPostExecute(strings);
            if(mQueryCallback != null){
                mQueryCallback.onQueryFinished(strings);
            }
        }

        public Context getContext() {
            return mContext;
        }
    }

    private class MediaItemQueryAsyncTask extends QueryAsyncTask<List<MediaItem>>{

        public MediaItemQueryAsyncTask(Context context, QueryCallback<List<MediaItem>> queryCallback) {
            super(context, queryCallback);
        }

        @Override
        protected List<MediaItem> doInBackground(Void... voids) {
//            List<MediaItemWrapper> mediaItemWrapperList = new ArrayList<>();
            ArrayList<MediaItem> mediaItemList = querySdImageMedaiItemPathList(getContext());
            return mediaItemList;
        }
    }


    public interface QueryCallback<T> {
        void onQueryFinished(T result);
    }
}
