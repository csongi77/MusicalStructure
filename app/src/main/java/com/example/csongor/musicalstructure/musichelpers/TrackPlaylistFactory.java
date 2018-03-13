package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by csongor on 3/5/18.
 * Helper class for generating Track list object asynchronously using Strategy and Factory patterns.
 * Since MusicTrack cannot be instantiated outside this package this class has the
 * only responsibility to create appropriate Track Lists.
 */

public class TrackPlaylistFactory extends AsyncTaskLoader<List<Track>> {

    // The PlaylistCreationStrategy encapsulates the methods creating the appropriate TrackList
    private PlaylistCreationStrategy mPlaylistStrategy;

    /**
     * Consturctor
     * @param context context of client
     * @param playlistStrategy defines which kind of TrackList is needed to load
     */
    public TrackPlaylistFactory(Context context, PlaylistStrategies playlistStrategy) {
        super(context);
        switch (playlistStrategy) {
            // Instantiating the appropriate Strategy
            case DUMMY_PLAYLIST:
                mPlaylistStrategy = new DummyTrackListStrategy();
                return;
            default:
                mPlaylistStrategy = new Mp3TrackListStrategy(getContext());
                return;
        }
    }

    /**
     * Overriding AsyncTaskLoader's methods
     */

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Loading data based on Strategy
    @Override
    public List<Track> loadInBackground() {
        return mPlaylistStrategy.getPlaylist();
    }

}




