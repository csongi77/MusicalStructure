package com.example.csongor.musicalstructure.musichelpers;

/**
 * Created by csongor on 3/8/18.
 * This is the interface of Track data sturcture. It can be used for creating
 * dummy tracks, either from file, or Stream.
 * Since all of Media files has these properties, implementing
 * this interface can be also suitable for other media, example VideoMusicClips, etc.
 */
public interface Track {
    String getAuthor();
    String getTitle();
    long getLength();
    String getGenre();
}
