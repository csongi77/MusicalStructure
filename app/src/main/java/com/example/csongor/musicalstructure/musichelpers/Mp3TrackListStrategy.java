package com.example.csongor.musicalstructure.musichelpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.example.csongor.musicalstructure.ErrorActivity;
import com.example.csongor.musicalstructure.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/12/18.
 * This class is responsible for creating Mp3 Track List from external storage
 * Implementation of {@link PlaylistCreationStrategy} for making easier call from
 * {@link TrackPlaylistFactory}
 */
class Mp3TrackListStrategy implements PlaylistCreationStrategy {

    private static final String LOG_TAG = Mp3TrackListStrategy.class.getSimpleName();

    private static final String EXTRA_ERROR = "EXTRA_ERROR";
    private Context mContext;

    /**
     * Default constructor
     *
     * @param context of calling Activity
     */
    Mp3TrackListStrategy(Context context) {
        mContext = context;
    }

    /**
     * @return new List<Track> containing mp3 files from Media directory and it's subfolders
     * Idea to use ContentResolver is from Android Developers' Guide, StackOverflow and own experience
     */
    @Override
    public List<Track> getPlaylist() {
        Log.e(LOG_TAG, "getPlayList called");
        List<Track> playlist = new ArrayList<>();

        // Getting Content Resolver for parsing audio content
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Making query for the files
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            Log.e(LOG_TAG, "cursor==null");
            // query failed, handle error. Sending Intent to ErrorActivity
            Intent intent = new Intent(mContext, ErrorActivity.class);
            intent.putExtra(EXTRA_ERROR, ErrorMessage.MP3_FILE_QUERY_ERROR);
            mContext.startActivity(intent);
        } else if (!cursor.moveToFirst()) {
            Log.e(LOG_TAG, "no file found");
            cursor.close();
            // no media on the device, handle error via Intent to ErrorActivity
            Intent intent = new Intent(mContext, ErrorActivity.class);
            intent.putExtra(EXTRA_ERROR, ErrorMessage.NO_MEDIA);
            mContext.startActivity(intent);
        } else {
            Log.d(LOG_TAG, "retrieving data from cursor");
            // parsing retrieved values into temporary variables
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                //adding temporary variable results into Mp3 track, adding it into List
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                long thisDuration = (long) cursor.getInt(durationColumn);
                Track trackToAdd = new Mp3MusicTrack(new MusicTrack(thisArtist, thisTitle, thisDuration, ""), thisId);
                playlist.add(trackToAdd);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // If there were no results, NullTrack is added instead throwing Exception
        if (playlist.isEmpty()) playlist.add(new NullTrack());
        return playlist;
    }
}
