package com.example.csongor.musicalstructure.musichelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/12/18.
 * This class is responsible for creating dummy Track List.
 * Implementation of {@link PlaylistCreationStrategy} for making easier call from
 * {@link TrackPlaylistFactory}
 */

class DummyTrackListStrategy implements PlaylistCreationStrategy {
    // declaring genre constants in order to avoid misspelling
    private static final String TECHNO = "Techno";
    private static final String GOA = "Goa";
    private static final String RAVE = "Rave";

    /**
     * Default constructor.
     * Since this class can be instantiated by a Factory of the package, it remains package private
     */
    DummyTrackListStrategy() {
    }

    @Override
    public List<Track> getPlaylist() {

        /**
         * Building dummy Track list. The {@link MusicTrack} structure is
         * wrapped in {@link DummyMusicTrack}
         */
        List<Track> mTrackList = new ArrayList<>();

        mTrackList.add(new DummyMusicTrack(new MusicTrack("Westbam", "Beatbox Rocker", 4 * 60 * 1000L + 11 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Westbam", "10 in 01", 3 * 60 * 1000L + 37 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Astral Projection", "Dancing Galaxy", 4 * 60 * 1000L + 12 * 1000L, GOA)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dr. Motte & Westbam", "Sunshine", 3 * 60 * 1000L + 48 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Astral Projection", "Soundform", 8 * 60 * 1000L + 12 * 1000L, GOA)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Vibrasphere", "Katapult", 7 * 60 * 1000L + 16 * 1000L, GOA)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dj Hooligan", "System Ecstasy", 3 * 60 * 1000L + 47 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dj Hooligan", "The Culture", 3 * 60 * 1000L + 29 * 1000L, RAVE)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dune", "Hardcore Vibes", 3 * 60 * 1000L + 33 * 1000L, RAVE)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dune", "Million Miles From Home", 4 * 60 * 1000L + 1000L, RAVE)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Chemical Brothers", "Under The Influence", 4 * 60 * 1000L + 15 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Dj Misjah & Dj Tim", "Access", 7 * 60 * 1000L + 15 * 1000L, TECHNO)));
        mTrackList.add(new DummyMusicTrack(new MusicTrack("Astral Projection", "Kabbalah", 9 * 60 * 1000L + 29 * 1000L, GOA)));

        /**
         * Add some time for returning the list to simulate latency
         */
        try {
            Thread.sleep(4 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mTrackList;
    }
}
