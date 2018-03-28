package com.example.csongor.musicalstructure;

/**
 * Created by csongor on 3/7/18.
 * This enum holds possible errors.
 */
public enum ErrorMessage {
    // If external storage is unavailable of not readable:
    NO_MEDIA,

    // If Read_External_Storage permission is not granted:
    NO_PERMISSION,

    // If default Music directory contains no mp3 files:
    NO_FILE,

    // If Mp3TrackListStrategy's Cursor==null:
    MP3_FILE_QUERY_ERROR
}
