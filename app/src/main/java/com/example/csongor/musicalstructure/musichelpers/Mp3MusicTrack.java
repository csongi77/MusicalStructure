package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.content.Intent;

import com.example.csongor.musicalstructure.PlayerActivity;

/**
 * Created by csongor on 3/13/18.
 * This class wraps the basic Track class with Mp3 behaviour.
 * It contains the URI of track and implementing play method.
 */

public class Mp3MusicTrack extends AbstractTrackWrapper implements Playable {

    private long mId;
    private static final String EXTRA_ID="EXTRA_ID";

    /**
     * @param track the wrapped Track which will have extended behaviours and/or properties
     * @param id the ID of instantiated Track
     *            Since Tracks can be instantiated by Factories in this package,
     *            constructor's scope is package private
     */
    Mp3MusicTrack(Track track, long id) {
        super(track);
        mId = id;
    }

    /**
     * Start playing mp3 via implicit Intent
     * @param context
     */
    @Override
    public void play(Context context) {


    Intent intent = new Intent(context, PlayerActivity.class);
    intent.putExtra(EXTRA_ID, mId);
    context.startActivity(intent);

    }
}
