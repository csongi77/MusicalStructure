package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

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
        mContext=context;
    }

    @Override
    public List<Track> getPlaylist() {
        List<Track> trackList=new ArrayList<>();
        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File [] fileList = musicDirectory.listFiles();
        if (fileList.length==0){
            trackList.add(new NullTrack());
        } else {
            for(int i=0; i<fileList.length; i++){
                File toProcess=fileList[i];
                String filename = toProcess.getName();
                String extensionArray[] = filename.split("\\.");
                if(extensionArray[extensionArray.length-1].equalsIgnoreCase("mp3")){
                   trackList.add(createMp3Track(toProcess));
                }
            }
        }
        return trackList;
    }

    private Track createMp3Track(File toProcess) {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(toProcess.getPath());
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        long length = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Uri uri=Uri.parse(toProcess.getPath());
        return new Mp3MusicTrack(new MusicTrack(artist,title,length,genre),uri);
    }


}
