package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.example.csongor.musicalstructure.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csongor on 3/12/18.
 * This class is responsible for creating Mp3 Track List from external storage
 * Implementation of PlaylistCreationStrategy
 */

class Mp3TrackListStrategy implements PlaylistCreationStrategy {

    private Context mContext;

    Mp3TrackListStrategy(Context context) {
        mContext = context;
    }

    /**
     * @return new List<Track> containing mp3 files from Music directory and it's subfolders
     */
    @Override
    public List<Track> getPlaylist() {
        List<Track> trackList;
        File musicDirectory = Environment.getExternalStorageDirectory();
        trackList = processFile(musicDirectory);
        if (trackList.isEmpty()) {
            trackList.add(new NullTrack());
        }
        return trackList;
    }

    // Parse mp3 files from directory recursively
    private List<Track> processFile(File file) {
        File[] fileList = file.listFiles();
        List<Track> trackList = new ArrayList<>();
        if (fileList.length == 0)
            return trackList;
        for (int i = 0; i < fileList.length; i++) {
            File toProcess = fileList[i];
            if (!toProcess.isDirectory()) {
                String filename = toProcess.getName();
                String extensionArray[] = filename.split("\\.");
                if (extensionArray[extensionArray.length - 1].equalsIgnoreCase("mp3")) {
                    // If the file is mp3, new Mp3Track is added to tracklist
                    trackList.add(createMp3Track(toProcess));
                }
            } else {
                // If the file is directory, it will parsed calling this method recursively
                trackList.addAll(processFile(fileList[i]));
            }
        }
        return trackList;
    }

    //Creating Mp3Track from file with checking values
    private Track createMp3Track(File toProcess) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(toProcess.getPath());
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artist == null || artist.equalsIgnoreCase(""))
            artist = toProcess.getName().toString();
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (title == null || title.equalsIgnoreCase(""))
            title = mContext.getString(R.string.mp3_title_unknown);
        String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        if (genre == null || genre.startsWith("(") || genre.equalsIgnoreCase("") || Character.isDigit(genre.charAt(0)))
            genre = mContext.getString(R.string.mp3_title_unknown);
        long length = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        //Uri uri = Uri.parse(toProcess.getPath());
        Uri uri= FileProvider.getUriForFile(mContext,"com.example.csongor.musicalstructure.fileprovider",toProcess);
        mContext.grantUriPermission("*",uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return new Mp3MusicTrack(new MusicTrack(artist, title, length, genre), uri);
    }

}
