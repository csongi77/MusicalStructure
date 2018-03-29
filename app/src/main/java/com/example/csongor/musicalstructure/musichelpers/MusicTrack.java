package com.example.csongor.musicalstructure.musichelpers;

/**
 * Created by csongor on 3/9/18.
 * Because MusicTrack is base entity data class
 * which later can be persistable, structure must be immutable.
 * Striving on open-closed principle further behaviours or properties can be added via wrapper classes.
 */
public final class MusicTrack implements Track {
    // Field declarations. Since these fields are immutable they are final fields
    private final String mAuthor;
    private final String mTitle;
    private final long mLength;
    private final String mGenre;
    private volatile int mHashCode;

    /**
     * Constructor for MusicTrack. Since instantiating is allowed by Factories of the package,
     * the constructor remains package private
     *
     * @param author author of track
     * @param title  title of track
     * @param length length of track in millis
     * @param genre  genre of track
     */
    MusicTrack(String author, String title, long length, String genre) {
        this.mAuthor = author;
        this.mTitle = title;
        this.mLength = length;
        this.mGenre = genre;
    }

    // Getters, also these are implementations of Track interface
    /**
     * @return Track's Author/Artist
     */
    @Override
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * @return Title of the Track
     */
    @Override
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return length/duration of Track in millis
     */
    @Override
    public long getLength() {
        return mLength;
    }

    /**
     * @return the Genre of the Track
     */
    @Override
    public String getGenre() {
        return mGenre;
    }

    /**
     * Assuming future versions of MusicalStructure will be able for removing Tracks,
     * proper hashcode and equals methods must be overwritten.
     * Those methods are based on Joshua Bloch's Effective Java book (3rd chapter, 9th point).
     * Because all fields and the class is immutable lazy initialization is allowed for
     * speed optimization.
     */
    @Override
    public int hashCode() {
        int result = mHashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + mAuthor.hashCode();
            result = 31 * result + mTitle.hashCode();
            result = 31 * result + mGenre.hashCode();
            result = 31 * result + ((int) (mLength ^ (mLength >>> 32)));
            mHashCode = result;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MusicTrack)) return false;
        MusicTrack mTrack = (MusicTrack) obj;
        return mTrack.mLength == this.mLength &&
                mTrack.mGenre.equalsIgnoreCase(this.mGenre) &&
                mTrack.mTitle.equalsIgnoreCase(this.mTitle) &&
                mTrack.mAuthor.equalsIgnoreCase(this.mTitle);
    }

    /**
     * For debugging purposes
     * @return author and title
     */
    @Override
    public String toString() {
        return "MusicTrack-> author:" + mAuthor + ", title:" + mTitle;
    }
}
