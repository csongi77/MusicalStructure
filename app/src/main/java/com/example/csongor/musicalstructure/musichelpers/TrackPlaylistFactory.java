package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/5/18.
 * Helper class for generating Track list object asynchronously using Factory pattern.
 * Since MusicTrack cannot be instantiated outside this package this class has the
 * only responsibility to create appropriate Track Lists.
 */

public class TrackPlaylistFactory extends AsyncTaskLoader<List<Track>> {
    private static final String LOG_TAG = TrackPlaylistFactory.class.getSimpleName();

    private PlaylistCreationStrategy mPlaylistStrategy;
    private List<Track> mTrackList;

    public TrackPlaylistFactory(Context context, PlaylistStrategies playlistStrategy) {
        super(context);
        switch (playlistStrategy) {
            case DUMMY_PLAYLIST:
                mPlaylistStrategy = new DummyTrackListCreator();
                return;
            default:
                mPlaylistStrategy = new Mp3TrackListCreator(getContext());
                return;
        }
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "onStartLoading called");
        forceLoad();
    }

    @Override
    public List<Track> loadInBackground() {
        return mPlaylistStrategy.getPlaylist();
    }

}




