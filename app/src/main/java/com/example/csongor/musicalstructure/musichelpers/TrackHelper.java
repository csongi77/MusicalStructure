package com.example.csongor.musicalstructure.musichelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/5/18.
 * Helper class for generating Track list object
 */

public class TrackHelper {
    private List<Track> trackList;

    // private constructor for creating trackList only once
    public TrackHelper(){
        trackList=new ArrayList<Track>();
        createTrackList();
    }



    public List<Track> getTrackList(){
        return trackList;
    }

    private void createTrackList() {

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Westbam")
                .setGenre(Genre.TECHNO)
                .setTitle("Beatbox Rocker")
                .setLength(4*60*1000L+11*1000L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Westbam")
                .setGenre(Genre.TECHNO)
                .setTitle("10 in 01")
                .setLength(3*60*1000L+37*1000L+153L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Dancing Galaxy")
                .setLength(4*60*1000L+11*1000L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dr. Motte & Westbam")
                .setGenre(Genre.TECHNO)
                .setTitle("Sunshine")
                .setLength(3*60*1000L+48*1000L+233L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Soundform")
                .setLength(8*60*1000L+12*1000L+33L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Vibrasphere")
                .setGenre(Genre.GOA)
                .setTitle("Katapult")
                .setLength(7*60*1000L+16*1000L+233L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Hooligan")
                .setGenre(Genre.TECHNO)
                .setTitle("System Ecstasy")
                .setLength(3*60*1000L+47*1000L+683L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Hooligan")
                .setGenre(Genre.RAVE)
                .setTitle("The Culture")
                .setLength(6*60*1000L+29*1000L+873L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dune")
                .setGenre(Genre.RAVE)
                .setTitle("Hardcore Vibes")
                .setLength(3*60*1000L+33*1000L+654L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dune")
                .setGenre(Genre.RAVE)
                .setTitle("Million Miles From Home")
                .setLength(4*60*1000L+1*1000L+111L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Chemical Brothers")
                .setGenre(Genre.TECHNO)
                .setTitle("Under The Influence")
                .setLength(4*60*1000L+15*1000L+115L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Misjah & Dj Tim")
                .setGenre(Genre.TECHNO)
                .setTitle("Access")
                .setLength(7*60*1000L+15*1000L+605L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Kabbalah")
                .setLength(9*60*1000L+29*1000L+33L)
                .build());
    }
}
