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

import java.util.List;

import com.example.csongor.musicalstructure.musichelpers.NullTrack;
import com.example.csongor.musicalstructure.musichelpers.PlaylistStrategies;
import com.example.csongor.musicalstructure.musichelpers.TrackPlaylistFactory;
import com.example.csongor.musicalstructure.musichelpers.Playable;
import com.example.csongor.musicalstructure.musichelpers.Track;

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
    private static final int LOADER_ID = 42;

    // Defining variables
    private android.support.v4.content.Loader<List<Track>> mLoader;
    private List<Track> mPlayList;
    private PlaylistArrayAdapter mAdapter;
    private ListView mListView;
    private ImageView mLoaderImage;
    private PlaylistStrategies mStrategy;

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
        Intent intent=getIntent();
        mStrategy = (PlaylistStrategies)intent.getSerializableExtra(EXTRA_PLAYLIST_CREATION_STRATEGY);

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
        if(mPlayList==null) return super.onContextItemSelected(item);

        // If everything is OK, sort playlist by Artist, Genre, Length, Title
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

    /**
     * Overriding LoaderCallbacks
     * If mLoader hasn't been created yet, a new Loader (TrackPlayListFactory)
     * will be instantiated with the required TrackList creation strategy:
     *  - DummyTracklist (for testing purposes)
     *  - Mp3Strategy (for listing mp3 files from default Musics directory
     */
    @Override
    public android.support.v4.content.Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        android.support.v4.content.Loader mLoaderToReturn=mLoader;
        if (mLoader == null) {
             mLoaderToReturn = new TrackPlaylistFactory(PlayListActivity.this, mStrategy);
        }
        return mLoaderToReturn;
    }

    /**
     * Overriding onLoadFinished callback method
     * @param loader
     * @param data - the TrackList generated by TrackPlayListFactory. All TrackCreation strategy
     *             returns List<Track> with least one Track instance. In case of error it returns
     *            List<Track> with one NullTrack instance instead of throwing Exception.
     */
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Track>> loader, List<Track> data) {
        // Assigning retrieved data to mPlayList variable
        mPlayList = data;

        // If error happened, the first element of a List is NullTrack.
        if (!(mPlayList.get(0) instanceof NullTrack)) {

            // Removing loaderImage
            mLoaderImage.setVisibility(View.GONE);

            // If Adapter hasn't been already instantiated it will be done here.
            if (mAdapter == null) {
                mAdapter = new PlaylistArrayAdapter(this, mPlayList);
                mListView.setAdapter(mAdapter);

                // Setting up listener to Items with appropriate behaviour and make ListView visible
                mListView.setOnItemClickListener((adapterView, view, i, l) -> ((Playable)adapterView.getItemAtPosition(i)).play(getApplicationContext()));
                mListView.setVisibility(View.VISIBLE);
            } else {

                // If Adapter has been already defined, it will refreshed with new data
                mAdapter.clear();
                mAdapter.addAll(mPlayList);
                mListView.setVisibility(View.VISIBLE);
            }
        } else {
            // Open Error Activity in case of NullTrack
            Intent intent = new Intent(PlayListActivity.this, ErrorActivity.class);
            intent.putExtra(EXTRA_ERROR,ErrorMessage.NO_FILE);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Track>> loader) {
        mLoaderImage.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mPlayList.clear();
    }


}
