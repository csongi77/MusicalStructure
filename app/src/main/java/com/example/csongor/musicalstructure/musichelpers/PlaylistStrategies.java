package com.example.csongor.musicalstructure.musichelpers;

/**
 * Created by csongor on 3/12/18.
 * Enum for possible Playlist creation strategies.
 * On creating TrackPlaylistFactory client has to chose one of these
 * to determine how playlist will be created
 */
public enum PlaylistStrategies {
    DUMMY_PLAYLIST,
    MP3_PLAYLIST_FROM_FILE
}
