package com.example.csongor.musicalstructure.musichelpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by csongor on 3/5/18.
 * Base track component structure. This is the abstract component class as described in
 * Gang of Four's book's Composite pattern.
 * It contains all info about the related track.
 */

class AbstractTrackWrapper implements Track{

    private Track mTrack;

    /**
     *
     * @param track the wrapped Track which will have extended behaviours and/or properties
     */
    AbstractTrackWrapper(Track track){
      mTrack=track;
    }

    /**
     * getters and setters
     */
    @Override
    public String getGenre() {
        return mTrack.getGenre();
    }

    @Override
    public String getAuthor() {
        return mTrack.getAuthor();
    }

    @Override
    public long getLength(){
        return mTrack.getLength();
    }

    @Override
    public String getTitle() {
        return mTrack.getTitle();
    }

}
