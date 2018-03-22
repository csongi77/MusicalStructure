package com.example.csongor.musicalstructure;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.csongor.musicalstructure.musichelpers.Track;

import java.util.List;

/**
 * Created by csongor on 3/5/18.
 * Adapter for PlayListActivity
 */

public class PlaylistArrayAdapter extends ArrayAdapter<Track> {

    private Context mContext;
    private List<Track> mTrackList;

    // Ctor;
    public PlaylistArrayAdapter(@NonNull Context context, @NonNull List<Track> trackList) {
        super(context, R.layout.activity_play_list, trackList);
        mContext = context;
        mTrackList = trackList;
    }

    /**
     * @param position    the index of List<Track> element
     * @param convertView the View should be converted into list_item view
     * @param parent      view container
     * @return inflated view in PlayList
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Track mTrack = mTrackList.get(position);
        // Declaring local variables
        String authorToDisplay;
        String titleToDisplay;
        int maxStringLengthAuthor = 32;
        int maxStringLengthTitle = 22;

        View mRootView = convertView;

        /**
         * Inflating view if it's null
         */
        if (mRootView==null) {
            mRootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        /**
         *  find and set text for appropriate Track fields
         *  If the lentgh of Strings are too long, they will be cut
         */
        TextView mArtistTv = mRootView.findViewById(R.id.text_author);
        if (mTrack.getAuthor().trim().length() > maxStringLengthAuthor) {
            authorToDisplay = new StringBuilder(mTrack.getAuthor().substring(0, maxStringLengthAuthor))
                    .append("...")
                    .toString();
        } else {
            authorToDisplay = mTrack.getAuthor();
        }
        mArtistTv.setText(authorToDisplay);

        TextView mTitleTv = mRootView.findViewById(R.id.text_title);
        if (mTrack.getTitle().trim().length() > maxStringLengthTitle) {
            titleToDisplay = new StringBuilder(mTrack.getTitle().substring(0, maxStringLengthTitle))
                    .append("...")
                    .toString();
        } else {
            titleToDisplay= mTrack.getTitle();
        }
        mTitleTv.setText(titleToDisplay);

        TextView mGenre = mRootView.findViewById(R.id.text_genre);
        mGenre.setText(mTrack.getGenre());

        /**
         * Converting trackLentgh time from millis to minutes:seconds.millis
         * If seconds value < 10, then append 0 number before it to get equal seconds length
         */
        long mTrackLength = mTrack.getLength();
        StringBuilder mLengthToDisplay = new StringBuilder("").
                append(mTrackLength / 60000).
                append(": ");
        if (mTrackLength%60000/1000<10) mLengthToDisplay.append(0);
                mLengthToDisplay.append(mTrackLength % 60000 / 1000).
                append(".").
                append(mTrackLength % 1000);

        TextView mLength = mRootView.findViewById(R.id.text_length);
        mLength.setText(mLengthToDisplay.toString());

        // return the rendered view
        return mRootView;
    }


}
