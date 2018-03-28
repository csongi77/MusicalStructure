package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;
import android.widget.Toast;

import com.example.csongor.musicalstructure.R;

/**
 * Created by csongor on 3/9/18.
 * This class is a dummy track class. It was created for app testing purposes.
 * It implements {@link Playable} interface in order to make easyer call from
 * {@link com.example.csongor.musicalstructure.PlayListActivity}
 */
public class DummyMusicTrack extends AbstractTrackWrapper implements Playable {

    /**
     * @param track the wrapped Track which will have extended behaviours and/or properties
     */
    DummyMusicTrack(Track track) {
        super(track);
    }

    /**
     * Handling Playable's play method. Since this is a DummyMusicTrack without real audio file
     * this method shows only a Toast message.
     *
     * @param context the Context of calling Activity
     */
    @Override
    public void play(Context context) {
        String mDisplayParam;
        if (super.getTitle().isEmpty() || super.getTitle().equalsIgnoreCase("unknown")) {
            mDisplayParam = super.getAuthor();
            String toDisplay = context.getString(R.string.playing_dummy_music_track_by_author);
            Toast.makeText(context, String.format(toDisplay, mDisplayParam), Toast.LENGTH_LONG).show();
        } else {
            mDisplayParam = super.getTitle();
            String toDisplay = context.getString(R.string.playing_dummy_music_track);
            Toast.makeText(context, String.format(toDisplay, mDisplayParam), Toast.LENGTH_SHORT).show();
        }
    }
}
