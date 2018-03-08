package com.example.csongor.musicalstructure.musichelpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by csongor on 3/5/18.
 * base track structure. It contains all info about the related track
 */

public class Track {
    /**
     *  field declarations
     */
    private Enum<Genre> genre=Genre.UNKNOWN;
    private String author="unknown";
    private long length=0L;
    private String title="unknown";
    private double rank=0.0;
    private int numberOfVotes=0;

    /**
     * private constructor
     */
    private Track (TrackBuilder builder){
      genre=builder.genre;
      author=builder.artist;
      length=builder.length;
      title=builder.title;
      rank=builder.rank;
      numberOfVotes=builder.votes;
    }

    /**
     * getters and setters
     */
    public String getGenre() {
        return genre.toString();
    }

    public String getAuthor() {
        return author;
    }

    public long getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }

    public double getRank() {
        return rank;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    /**
     * @param rate user can rate the music from 1 to 5. (the higher the better)
     *             The rank is based on average of votes.
     *             Because it is a dummy track object, the average is calculated by "number of
     *             votes" and the "base rank".
     */
    public void setRank(int rate) {
        if(numberOfVotes>0) {
            rank = (rank * numberOfVotes / (numberOfVotes + 1)) + (rate / (numberOfVotes + 1));
        } else {
            rank=(double)rate;
        }
        numberOfVotes++;
    }

    /**
     *
     * @return the length of a track in String format (mmm:ss:SS) for displaying in list
     */
    public String getLengthString(){
        return (new SimpleDateFormat("mmm:ss.SS")).format(new Date(length));
    }

    /**
     * builder inner class for easy creation of tracks (based on GoF's Builder pattern)
     * IMPORTANT: If inital number of votes is 0 then inital rank sets to 0 regardless of
     * previous value
     */
    public static class TrackBuilder {
        String artist;
        String title;
        Enum<Genre> genre;
        long length;
        double rank;
        int votes;

        Track newTrack;
        private static TrackBuilder trackBuilderInstance = new TrackBuilder();

        public TrackBuilder getInstance(){
            return trackBuilderInstance;
        }

        public TrackBuilder setAuthor (String author){
            this.artist=author;
            return trackBuilderInstance;
        }

        public TrackBuilder setTitle (String title){
            this.title=title;
            return trackBuilderInstance;
        }

        public TrackBuilder setGenre (Enum<Genre> genre){
            this.genre=genre;
            return trackBuilderInstance;
        }

        public TrackBuilder setLength (long length){
            this.length=length;
            return trackBuilderInstance;
        }

        public TrackBuilder setInitalRank(double rank){
            this.rank=rank;
            return trackBuilderInstance;
        }

        public TrackBuilder setInitalNumberOfVotes(int votes){
            this.votes=votes;
            return trackBuilderInstance;
        }

        public Track build(){
            if(this.votes==0){
                this.rank=0.0;
            }
            return new Track(this);
        }
    }
}
