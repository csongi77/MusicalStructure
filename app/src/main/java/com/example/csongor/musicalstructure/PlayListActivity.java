package com.example.csongor.musicalstructure;

import android.content.Intent;
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

import com.example.csongor.musicalstructure.musichelpers.NullTrack;
import com.example.csongor.musicalstructure.musichelpers.Playable;
import com.example.csongor.musicalstructure.musichelpers.PlaylistStrategies;
import com.example.csongor.musicalstructure.musichelpers.Track;
import com.example.csongor.musicalstructure.musichelpers.TrackPlaylistFactory;

import java.util.List;

/**
 * This Activity has the responsibility for starting to load TrackList, displaying them and
 * put them into requested order (by Artist, Title, Genre, Length)
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class PlayListActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Track>> {

    // Defining Constants
    private static final String LOG_TAG = PlayListActivity.class.getSimpleName();
    private static final String EXTRA_PLAYLIST_CREATION_STRATEGY = "EXTRA_PLAYLIST_CREATION_STRATEGY";
    private static final String EXTRA_ERROR = "EXTRA_ERROR";
    private static final String BUNDLE_PLAYLIST_ORDER = "BUNDLE_PLAYLIST_ORDER";
    private static final int LOADER_ID = 42;

    // Defining variables
    private android.support.v4.content.Loader<List<Track>> mLoader;
    private List<Track> mPlayList;
    private PlaylistArrayAdapter mAdapter;
    private ListView mListView;
    private ImageView mLoaderImage;
    private PlaylistStrategies mStrategy;
    private ListingOrder mListingOrder;

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

        // Getting playlist creation strategy via Intent
        Intent intent = getIntent();
        mStrategy = (PlaylistStrategies) intent.getSerializableExtra(EXTRA_PLAYLIST_CREATION_STRATEGY);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart called");
        /**
         * Creating Loader in order to retrieve TrackList
         */
        Log.d(LOG_TAG, "getLoaderManager executed");
        mLoader = getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        // Setting up default listing order
        mListingOrder = ListingOrder.DEFAULT;
    }

    /**
     * Release resources onStop event
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onStop called, destroyLoader runs");
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    /**
     * @param outState Saving actual Track Listing order for appropriate listing on restoring view
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_PLAYLIST_ORDER, mListingOrder);
        super.onSaveInstanceState(outState);
    }

    /**
     * Retrieving playlist order to redraw view when config changes
     * Not the nicest solution because calling sortPlayListBy() method will use more CPU time but
     * I couldn't put List<Track>'s reference into Bundle (because List is not Serializable)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getSerializable(BUNDLE_PLAYLIST_ORDER) != null)
            mListingOrder = (ListingOrder) savedInstanceState.getSerializable((BUNDLE_PLAYLIST_ORDER));
        if (mPlayList != null)
            sortPlayListBy(mListingOrder);
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
        // Check whether is mPlaylist is not null to avoid NullPointerException
        if (mPlayList == null) return super.onContextItemSelected(item);
        Log.e(LOG_TAG, "onOptionsItemSelected called");

        /**
         *         If everything is OK, sort playlist by Artist, Genre, Length, Title
         *         Also set mListingOrder to put in Bundle for onSave- and onRestoreInstanceState in order
         *         to get right Listing when users gets back from PlayerActivity by pressing back button
         */
        switch (item.getItemId()) {
            case R.id.menu_artist:
                sortPlayListBy(ListingOrder.ARTIST);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_genre:
                sortPlayListBy(ListingOrder.GENRE);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_length:
                // Hope no single track is longer than 49 days... (length<maxInt)
                sortPlayListBy(ListingOrder.LENGTH);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_track:
                sortPlayListBy(ListingOrder.TITLE);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Overriding LoaderCallbacks
     * If mLoader hasn't been created yet, a new Loader (TrackPlayListFactory)
     * will be instantiated with the required TrackList creation strategy:
     * - DummyTracklist (for testing purposes)
     * - Mp3Strategy (for listing mp3 files from default Musics directory
     */
    @Override
    public android.support.v4.content.Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCReateLoader called");
        android.support.v4.content.Loader mLoaderToReturn = mLoader;
        if (mLoader == null) {
            mLoaderToReturn = new TrackPlaylistFactory(PlayListActivity.this, mStrategy);
        }
        return mLoaderToReturn;
    }

    /**
     * Overriding onLoadFinished callback method
     *
     * @param loader
     * @param data   - the TrackList generated by TrackPlayListFactory. All TrackCreation strategy
     *               returns List<Track> with least one Track instance. In case of error it returns
     *               List<Track> with one NullTrack instance instead of throwing Exception.
     */
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Track>> loader, List<Track> data) {
        // Assigning retrieved data to mPlayList variable
        Log.d(LOG_TAG, "onLoadFinished called");
        if (mPlayList == null)
            mPlayList = data;

        // If error happened, the first element of a List is NullTrack.
        if (!(mPlayList.get(0) instanceof NullTrack)) {

            // Removing loaderImage
            mLoaderImage.setVisibility(View.GONE);

            // If Adapter hasn't been already instantiated it will be done here.
            if (mAdapter == null) {
                Log.d(LOG_TAG, "mAdapter==null");
                mAdapter = new PlaylistArrayAdapter(this, mPlayList);
                mListView.setAdapter(mAdapter);

                // Setting up listener to Items with appropriate behaviour and make ListView visible
                mListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    ((Playable) adapterView.getItemAtPosition(i)).play(getApplicationContext());
                });
                mListView.setVisibility(View.VISIBLE);
            } else {
                Log.d(LOG_TAG, "mAdapter not null");
                mListView.setVisibility(View.VISIBLE);
            }
        } else {
            // Open Error Activity in case of NullTrack
            Intent intent = new Intent(PlayListActivity.this, ErrorActivity.class);
            intent.putExtra(EXTRA_ERROR, ErrorMessage.NO_FILE);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Track>> loader) {
        Log.d(LOG_TAG, "onLoaderReset called");
        mLoaderImage.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mPlayList.clear();
        mAdapter=null;
        mPlayList=null;
        mLoader=null;
    }

    /**
     * @param listingOrder - this is the helper method for sorting Playlist
     */
    private void sortPlayListBy(ListingOrder listingOrder) {
        // Setting up mListingOrder for savingInstanceState call
        mListingOrder=listingOrder;
        switch (mListingOrder) {
            case TITLE:
                mPlayList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
                return;
            case ARTIST:
                mPlayList.sort((t1, t2) -> t1.getAuthor().compareToIgnoreCase(t2.getAuthor()));
                return;
            case LENGTH:
                mPlayList.sort((t1, t2) -> (int) ((t2.getLength() - t1.getLength())));
                return;
            case GENRE:
                mPlayList.sort((t1, t2) -> t1.getGenre().compareToIgnoreCase(t2.getGenre()));
        }
    }
}
