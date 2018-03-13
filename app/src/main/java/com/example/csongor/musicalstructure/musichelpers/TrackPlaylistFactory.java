package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by csongor on 3/5/18.
 * Helper class for generating Track list object asynchronously using Factory pattern.
 * Since MusicTrack cannot be instantiated outside this package this class has the
 * only responsibility to create appropriate Track Lists.
 */

public class TrackPlaylistFactory extends AsyncTaskLoader<List<Track>> {

    private PlaylistCreationStrategy mPlaylistStrategy;

    public TrackPlaylistFactory(Context context, PlaylistStrategies playlistStrategy) {
        super(context);
        switch (playlistStrategy) {
            case DUMMY_PLAYLIST:
                mPlaylistStrategy = new DummyTrackListStrategy();
                return;
            default:
                mPlaylistStrategy = new Mp3TrackListStrategy(getContext());
                return;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Track> loadInBackground() {
        return mPlaylistStrategy.getPlaylist();
    }

}




