package com.example.csongor.musicalstructure;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.csongor.musicalstructure.musichelpers.Track;

import java.util.List;

/**
 * Created by csongor on 3/5/18.
 */

public class PlaylistArrayAdapter extends ArrayAdapter<Track> {

    private Context mContext;
    private List<Track> mTrackList;


    public PlaylistArrayAdapter(@NonNull Context context, @NonNull List<Track> trackList) {
        super(context, 0, trackList);
        mContext = context;
        mTrackList = trackList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Track mTrack=mTrackList.get(position);
        View mRootView = convertView;
        if (mRootView==null) {
            mRootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView mArtistTv=mRootView.findViewById(R.id.text_author);
        mArtistTv.setText(mTrack.getAuthor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mArtistTv.setTextColor(getContext().getColor(R.color.colorPrimary));
        }

        TextView mTitleTv=mRootView.findViewById(R.id.btn_list_by_title);


        return mRootView;
    }
}
