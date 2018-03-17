package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by csongor on 3/13/18.
 * This class wraps the basic Track class with Mp3 behaviour.
 * It contains the URI of track and implementing play method.
 */

public class Mp3MusicTrack extends AbstractTrackWrapper implements Playable {

    private Uri mUri;

    /**
     * @param track the wrapped Track which will have extended behaviours and/or properties
     * @param uri the URI of instantiated Track
     *            Since Tracks can be instantiated by Factories in this package,
     *            constructor's scope is package private
     */
    Mp3MusicTrack(Track track, Uri uri) {
        super(track);
        mUri = uri;
    }

    /**
     * Start playing mp3 via implicit Intent
     * @param context
     */
    @Override
    public void play(Context context) {



        Intent intent = new Intent(Intent.CATEGORY_APP_MUSIC);
        intent.setData(mUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }
}
