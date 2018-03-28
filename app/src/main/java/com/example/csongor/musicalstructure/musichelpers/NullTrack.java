package com.example.csongor.musicalstructure.musichelpers;

/**
 * Created by csongor on 3/12/18.
 * This {@link Track} will be created if some error occurs during TrackList creation. This instance will put
 * in List<Track> instead throwing an Exception.
 * Classes using List<Track> has to examine the first list element of this list.
 * <p>
 * if (!(mList.get(0) instanceof NullTrack)) {
 * // everything is fine, do as usual
 * ...
 * }
 */
public final class NullTrack implements Track {

    /**
     * Default constructor
     */
    NullTrack() {
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public long getLength() {
        return -1L;
    }

    @Override
    public String getGenre() {
        return "";
    }
}
