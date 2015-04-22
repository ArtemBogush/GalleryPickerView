package com.self.gallerypicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.self.gallerypicker.model.MediaItem;

/**
 * Created by user on 4/18/15.
 */
public class GalleryPickerView extends RecyclerView {

    private static final String APP_NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private static final int DEFAULT_VALUE_GRID_SPAN = 3;
    private static final int DEFAULT_VALUE_GRID_ORIENTATION = GridLayoutManager.VERTICAL;
    private static final boolean DEFAULT_VALUE_IS_REVERSED_LAYOUT = false;

    private GridLayoutManager mLayoutManager;
    private GalleryListAdapter mGalleryListAdapter;

    public GalleryPickerView(Context context) {
        super(context);
        init(null);
    }

    public GalleryPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GalleryPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        int gridSpan = DEFAULT_VALUE_GRID_SPAN;
        int orientation = DEFAULT_VALUE_GRID_ORIENTATION;
        boolean isReversedLayout = DEFAULT_VALUE_IS_REVERSED_LAYOUT;
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GalleryPickerView, 0, 0);
            try {
                gridSpan = typedArray.getInt(R.styleable.GalleryPickerView_grid_span, DEFAULT_VALUE_GRID_SPAN);
                orientation = typedArray.getInt(R.styleable.GalleryPickerView_android_orientation, DEFAULT_VALUE_GRID_SPAN);
                isReversedLayout = typedArray.getBoolean(R.styleable.GalleryPickerView_reversed, DEFAULT_VALUE_IS_REVERSED_LAYOUT);
            } finally {
                typedArray.recycle();
            }
        }

        mLayoutManager = new GridLayoutManager(getContext(), gridSpan, orientation, isReversedLayout);
        setLayoutManager(mLayoutManager);
        mGalleryListAdapter = new GalleryListAdapter();
        setAdapter(mGalleryListAdapter);
    }


    public void setGalleryDataListener(GalleryListAdapter.GalleryDataListener galleryDataListener) {
        mGalleryListAdapter.setGalleryDataListener(galleryDataListener);
    }

    public void setOnItemClickListener(GalleryListAdapter.OnItemClickListener onItemClickListener) {
        mGalleryListAdapter.setOnItemClickListener(onItemClickListener);
    }
}
