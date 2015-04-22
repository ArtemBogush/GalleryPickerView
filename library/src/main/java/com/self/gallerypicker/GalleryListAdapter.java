package com.self.gallerypicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.self.gallerypicker.logic.GalleryQuery;
import com.self.gallerypicker.logic.MediaItemWrapper;
import com.self.gallerypicker.model.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/18/15.
 */
public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.Holder> {

    private GalleryQuery mGalleryQuery;

    private OnItemClickListener mOnItemClickListener;
    private GalleryDataListener mGalleryDataListener;

    private List<MediaItemWrapper> mItemList = new ArrayList<>();

    public GalleryListAdapter() {
        mGalleryQuery = new GalleryQuery();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                loadData(recyclerView.getContext());
            }
        });
    }

    private void loadData(final Context context){
        notifyGalleryAdapterListenerLoadingStarted();
        mGalleryQuery.querySdMediaItemListAsync(context, new GalleryQuery.QueryCallback<List<MediaItem>>() {
            @Override
            public void onQueryFinished(List<MediaItem> result) {
                mItemList.clear();
                for (MediaItem mediaItem : result) {
                    mItemList.add(new MediaItemWrapper(context, mediaItem));
                }
                notifyDataSetChanged();
                notifyGalleryAdapterListenerLoadingFinished();
            }
        });
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        Log.d(GalleryListAdapter.class.getSimpleName(), "onViewRecycled(): holder.getAdapterPosition() = " + holder.getAdapterPosition());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        Holder holder = new Holder(view);
        holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);

        return holder;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MediaItemWrapper previousMediaItemWrapper = findMediaItemById(holder.id);
        if(previousMediaItemWrapper != null){
            previousMediaItemWrapper.cancelLoadingThumbnail();
        }
        MediaItemWrapper mediaItemWrapper = mItemList.get(position);
        mediaItemWrapper.setPosition(position);
        holder.thumbnail.setImageResource(R.drawable.default_placeholder);
        holder.id = mediaItemWrapper.getMediaItem().getId();
        mediaItemWrapper.loadThumbnailAsync(new HolderCallback(holder));
    }

    private MediaItemWrapper findMediaItemById(long id) {
        for(MediaItemWrapper itemWrapper: mItemList){
            if(itemWrapper.getMediaItem().getId() == id){
                return itemWrapper;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    private class HolderCallback implements MediaItemWrapper.Callback{

        private Holder mHolder;


        public HolderCallback(Holder holder) {
            mHolder = holder;
        }

        @Override
        public void onThumbnailReady(MediaItemWrapper mediaItemWrapper) {
            Log.d(GalleryListAdapter.class.getSimpleName(), "mHolder.getLayoutPosition() = " + mHolder.getLayoutPosition());
            if(mHolder.id == mediaItemWrapper.getMediaItem().getId()){
                mHolder.thumbnail.setImageBitmap(mediaItemWrapper.loadThumbnail());
            }
        }
    }

    private void notifyGalleryAdapterListenerLoadingStarted(){
        if(mGalleryDataListener != null){
            mGalleryDataListener.onDataLoadingStarted();
        }
    }

    private void notifyGalleryAdapterListenerLoadingFinished(){
        if(mGalleryDataListener != null){
            mGalleryDataListener.onDataLoadingFinished();
        }
    }

    public void setGalleryDataListener(GalleryDataListener galleryDataListener) {
        mGalleryDataListener = galleryDataListener;
    }

    public interface OnItemClickListener{
        void onItemClicked(MediaItem mediaItem);
    }

    public interface GalleryDataListener {
        void onDataLoadingStarted();
        void onDataLoadingFinished();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        ProgressBar progressBar;
        long id;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaItemWrapper mediaItemWrapper = mItemList.get(getAdapterPosition());
                    mOnItemClickListener.onItemClicked(mediaItemWrapper.getMediaItem());
                }
            });
        }
    }
}
