package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/12/18.
 * This class is responsible for creating Mp3 Track List from external storage
 * Implementation of PlaylistCreationStrategy
 */

class Mp3TrackListStrategy implements PlaylistCreationStrategy {


    Mp3TrackListStrategy(Context context) {
    }

    @Override
    public List<Track> getPlaylist() {
        List<Track> trackList=new ArrayList<>();
        trackList.add(new NullTrack());
        return trackList;
    }
}
