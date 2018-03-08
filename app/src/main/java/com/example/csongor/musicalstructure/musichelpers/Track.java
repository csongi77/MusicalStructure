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

    /**
     * private constructor
     */
    private Track (TrackBuilder builder){
      genre=builder.genre;
      author=builder.artist;
      length=builder.length;
      title=builder.title;
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


        public Track build(){
            return new Track(this);
        }
    }
}
