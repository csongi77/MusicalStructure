package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.content.Intent;

import com.example.csongor.musicalstructure.PlayerActivity;

/**
 * Created by csongor on 3/13/18.
 * This class wraps the basic {@link MusicTrack} class with Mp3 behaviour.
 * It contains the URI of track and implementing {@link Playable} interface1's play method.
 */
public class Mp3MusicTrack extends AbstractTrackWrapper implements Playable {

    // Declaring constants
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    // Declaring variables
    private long mId;

    /**
     * @param track the wrapped Track which will have extended behaviours and/or properties
     * @param id    the ID of instantiated Track
     *              Since Tracks can be instantiated by Factories in this package,
     *              constructor's scope is package private
     */
    Mp3MusicTrack(Track track, long id) {
        super(track);
        mId = id;
    }

    /**
     * Start playing mp3 via exlplicit Intent
     *
     * @param context the Context which called this method
     *                Track's Id and Title is put into Intent, which are read by {@link PlayerActivity}
     */
    @Override
    public void play(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_ID, mId);
        intent.putExtra(EXTRA_TITLE, this.getTitle());
        context.startActivity(intent);
    }
}
