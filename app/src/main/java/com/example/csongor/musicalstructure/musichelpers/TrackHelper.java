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
                .setInitalNumberOfVotes(10)
                .setInitalRank(4.5)
                .setLength(4*60*1000L+11*1000L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Westbam")
                .setGenre(Genre.TECHNO)
                .setTitle("10 in 01")
                .setInitalNumberOfVotes(101)
                .setInitalRank(4.9)
                .setLength(3*60*1000L+37*1000L+153L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Dancing Galaxy")
                .setInitalNumberOfVotes(3)
                .setInitalRank(3.33)
                .setLength(4*60*1000L+11*1000L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dr. Motte & Westbam")
                .setGenre(Genre.TECHNO)
                .setTitle("Sunshine")
                .setInitalNumberOfVotes(34)
                .setInitalRank(4.38)
                .setLength(3*60*1000L+48*1000L+233L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Soundform")
                .setInitalNumberOfVotes(19)
                .setInitalRank(4.02)
                .setLength(8*60*1000L+12*1000L+33L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Vibrasphere")
                .setGenre(Genre.GOA)
                .setTitle("Katapult")
                .setInitalNumberOfVotes(56)
                .setInitalRank(3.98)
                .setLength(7*60*1000L+16*1000L+233L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Hooligan")
                .setGenre(Genre.TECHNO)
                .setTitle("System Ecstasy")
                .setInitalNumberOfVotes(27)
                .setInitalRank(3.64)
                .setLength(3*60*1000L+47*1000L+683L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Hooligan")
                .setGenre(Genre.RAVE)
                .setTitle("The Culture")
                .setInitalNumberOfVotes(2)
                .setInitalRank(3.5)
                .setLength(6*60*1000L+29*1000L+873L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dune")
                .setGenre(Genre.RAVE)
                .setTitle("Hardcore Vibes")
                .setInitalNumberOfVotes(38)
                .setInitalRank(4.25)
                .setLength(3*60*1000L+33*1000L+654L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dune")
                .setGenre(Genre.RAVE)
                .setTitle("Million Miles From Home")
                .setInitalNumberOfVotes(92)
                .setInitalRank(4.12)
                .setLength(4*60*1000L+1*1000L+111L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Chemical Brothers")
                .setGenre(Genre.TECHNO)
                .setTitle("Under The Influence")
                .setInitalNumberOfVotes(19)
                .setInitalRank(3.89)
                .setLength(4*60*1000L+15*1000L+115L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Dj Misjah & Dj Tim")
                .setGenre(Genre.TECHNO)
                .setTitle("Access")
                .setInitalNumberOfVotes(54)
                .setInitalRank(4.34)
                .setLength(7*60*1000L+15*1000L+605L)
                .build());

        trackList.add(new Track.TrackBuilder().getInstance()
                .setAuthor("Astral Projection")
                .setGenre(Genre.GOA)
                .setTitle("Kabbalah")
                .setInitalNumberOfVotes(53)
                .setInitalRank(4.42)
                .setLength(9*60*1000L+29*1000L+33L)
                .build());
    }
}
