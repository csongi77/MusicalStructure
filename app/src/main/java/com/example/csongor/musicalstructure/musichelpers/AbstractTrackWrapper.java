package com.example.csongor.musicalstructure.musichelpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by csongor on 3/5/18.
 * Base track component structure. This is the abstract component class as described in
 * Gang of Four's book's Composite pattern.
 * It contains all info about the related track.
 */
class AbstractTrackWrapper implements Track {

    private Track mTrack;

    /**
     * @param track the wrapped Track which will have extended behaviours and/or properties
     */
    AbstractTrackWrapper(Track track) {
        mTrack = track;
    }


    // getters and setters of the wrapped track
    /**
     * @return the Genre of the wrapped Track
     */
    @Override
    public String getGenre() {
        return mTrack.getGenre();
    }

    /**
     * @return wrapped Track's Author/Artist
     */
    @Override
    public String getAuthor() {
        return mTrack.getAuthor();
    }

    /**
     * @return length/duration of the wrapped Track in millis
     */
    @Override
    public long getLength() {
        return mTrack.getLength();
    }

    /**
     * @return Title of the Track
     */
    @Override
    public String getTitle() {
        return mTrack.getTitle();
    }

}
