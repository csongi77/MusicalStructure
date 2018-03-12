package com.example.csongor.musicalstructure;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import com.example.csongor.musicalstructure.musichelpers.NullTrack;
import com.example.csongor.musicalstructure.musichelpers.PlaylistStrategies;
import com.example.csongor.musicalstructure.musichelpers.TrackPlaylistFactory;
import com.example.csongor.musicalstructure.musichelpers.Playable;
import com.example.csongor.musicalstructure.musichelpers.Track;


@RequiresApi(api = Build.VERSION_CODES.N)
public class PlayListActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Track>> {

    private static final String LOG_TAG = PlayListActivity.class.getSimpleName();
    private static final String EXTRA_LIST_BY = "EXTRA_LIST_BY";
    private static final int LOADER_ID = 0;
    private android.support.v4.content.Loader<List<Track>> mLoader;
    private List<Track> mPlayList;
    private PlaylistArrayAdapter mAdapter;
    private ListView mListView;
    private ImageView mLoaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        // Setting up toolbar
        android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.toolbar_list_options);
        setSupportActionBar(mToolbar);
        // Assigning views for variables
        mLoaderImage = findViewById(R.id.loader_image);
        mListView = findViewById(R.id.list_container);


        /**
         * Just for fun, creating an animated loader :)
         * knowledge from Stackoverflow:
         * https://stackoverflow.com/questions/1634252/how-to-make-a-smooth-image-rotation-in-android
         * image is self-made by Gimp
         */
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        mLoaderImage.startAnimation(rotate);

        /**
         * Creating Loader in order to retrieve TrackList
         */
        mLoader=getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        Log.e(LOG_TAG, "getLoaderManager executed");
        mLoader.forceLoad();


    }

    /**
     * Setting up menu options for sorting tracks
     *
     * @param menu the menu resource file
     * @return inflated menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_tracks, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Callback for sorting tracks. This also calls PlaylistArrayAdapter's
     * notifyDataSetChanged method in order to refresh playlist.
     *
     * @param item the selected menuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_artist:
                mPlayList.sort((t1, t2) -> t1.getAuthor().compareToIgnoreCase(t2.getAuthor()));
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_genre:
                mPlayList.sort((t1, t2) -> t1.getGenre().compareToIgnoreCase(t2.getGenre()));
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_length:
                // Hope no single track is longer than 49 days... (length<maxInt)
                mPlayList.sort((t1, t2) -> (int) ((t2.getLength() - t1.getLength())));
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_track:
                mPlayList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public android.support.v4.content.Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG,"onCreate loader called");
        android.support.v4.content.Loader mLoaderToReturn=mLoader;
        if (mLoader == null) {
            Log.e(LOG_TAG,"onCreateLoader, loader==null, loader created");
             mLoaderToReturn = new TrackPlaylistFactory(this, PlaylistStrategies.DUMMY_PLAYLIST);
        }
        return mLoaderToReturn;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Track>> loader, List<Track> data) {
        mPlayList = data;
        if (!(mPlayList.get(0) instanceof NullTrack)) {
            mLoaderImage.setVisibility(View.GONE);
            if (mAdapter == null) {
                mAdapter = new PlaylistArrayAdapter(this, mPlayList);
                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener((adapterView, view, i, l) -> ((Playable)adapterView.getItemAtPosition(i)).play(getApplicationContext()));
                mListView.setVisibility(View.VISIBLE);
            } else {
                mAdapter.clear();
                mAdapter.addAll(mPlayList);
                mListView.setVisibility(View.VISIBLE);
            }
        } else {
            mLoaderImage.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Track>> loader) {
        mLoaderImage.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mPlayList.clear();
    }


}
