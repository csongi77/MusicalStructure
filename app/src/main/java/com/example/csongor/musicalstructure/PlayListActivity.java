package com.example.csongor.musicalstructure;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.csongor.musicalstructure.musichelpers.Track;
import com.example.csongor.musicalstructure.musichelpers.TrackHelper;

import java.io.Serializable;
import java.util.List;

import static com.example.csongor.musicalstructure.R.layout.activity_play_list;

public class PlayListActivity extends AppCompatActivity {

    private static final String EXTRA_LIST_BY = "EXTRA_LIST_BY";
    private List<Track> mPlayList;
    private PlaylistArrayAdapter mAdapter;
    private ListView mListView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_play_list);
        mListView = findViewById(R.id.list_container);
        TrackHelper mTrackHelper = new TrackHelper();
        mPlayList = mTrackHelper.getTrackList();
        Serializable mListingOrder = getIntent().getSerializableExtra(EXTRA_LIST_BY);
        switch ((ListingOrder) mListingOrder) {
            case ARTIST:
                mPlayList.sort((t1, t2) -> {
                    return t1.getAuthor().compareToIgnoreCase(t2.getAuthor());

                });
                break;
            default:
                break;
        }
        mAdapter = new PlaylistArrayAdapter(this, mPlayList);
        mListView.setAdapter(mAdapter);


    }
}
