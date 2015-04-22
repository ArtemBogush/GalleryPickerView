package com.self.gallerypickerapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.self.gallerypicker.GalleryListAdapter;
import com.self.gallerypicker.GalleryPickerView;
import com.self.gallerypicker.model.MediaItem;


public class MainActivity extends ActionBarActivity {

    private GalleryPickerView mGalleryPickerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();

    }

    private void initData() {
        mGalleryPickerView.setOnItemClickListener(new GalleryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(MediaItem mediaItem) {
                Toast.makeText(MainActivity.this, "mediaItem tapped: " + mediaItem, Toast.LENGTH_SHORT).show();
            }
        });
        mGalleryPickerView.setGalleryDataListener(new GalleryListAdapter.GalleryDataListener() {
            @Override
            public void onDataLoadingStarted() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDataLoadingFinished() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {
        mGalleryPickerView = (GalleryPickerView) findViewById(R.id.gallery_picker_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
