package com.example.csongor.musicalstructure.musichelpers;

import android.content.Context;

/**
 * Created by csongor on 3/9/18.
 * This interface is for playing tracks in appropriate way.
 *  1) Dummy Tracks are showing Toast
 *  2) Mp3 Tracks are starting an implicit Intent
 *  3) For adding new media types this interface could be useful
 *  If Music Structure are growing, this interface could be implemented by a facade to
 *      hide underlying musichelper structure
 *  The aim of this interface is to decouple the playing implementation from UI objects implementation.
 */

public interface Playable {
    void play(Context context);
}
