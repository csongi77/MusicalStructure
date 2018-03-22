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
     *
     * @param position the index of List<Track> element
     * @param convertView the View should be converted into list_item view
     * @param parent view container
     * @return inflated view in PlayList
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Track mTrack=mTrackList.get(position);
        View mRootView = convertView;
        /**
         * converting trackLentgh time from millis to minutes:seconds.millis
         */
        long mTrackLength = mTrack.getLength();
        String mLengthToDisplay = new StringBuilder("").
                append(mTrackLength/60000).
                append(": ").
                append(mTrackLength%60000/1000).
                append(".").
                append(mTrackLength%1000).
                toString();
        /**
         * Inflating view if it's null
         */
        if (mRootView==null) {
            mRootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        /**
         *  find and set text for appropriate Track
         */
        TextView mArtistTv=mRootView.findViewById(R.id.text_author);
        mArtistTv.setText(mTrack.getAuthor());

        TextView mTitleTv=mRootView.findViewById(R.id.text_title);
        mTitleTv.setText(mTrack.getTitle());

        TextView mGenre=mRootView.findViewById(R.id.text_genre);
        mGenre.setText(mTrack.getGenre());

        TextView mLength = mRootView.findViewById(R.id.text_length);
        mLength.setText(mLengthToDisplay);

        // return the rendered view
        return mRootView;
    }


}
