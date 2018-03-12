package com.example.csongor.musicalstructure.musichelpers;

import java.util.List;

/**
 * Created by csongor on 3/12/
 * Interface for encapsulating Playlist creations.
 *
 */

interface PlaylistCreationStrategy {
     List<Track> getPlaylist();
}
