package com.example.csongor.musicalstructure;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.csongor.musicalstructure.musichelpers.Track;
import com.example.csongor.musicalstructure.musichelpers.TrackHelper;

import java.io.Serializable;
import java.util.List;

import static com.example.csongor.musicalstructure.R.layout.activity_play_list;
@RequiresApi(api = Build.VERSION_CODES.N)
public class PlayListActivity extends AppCompatActivity {

    private static final String EXTRA_LIST_BY = "EXTRA_LIST_BY";
    private List<Track> mPlayList;
    private PlaylistArrayAdapter mAdapter;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_play_list);
        // Setting up toolbar
         android.support.v7.widget.Toolbar mToolbar = findViewById(R.id.toolbar_list_options);
         setSupportActionBar(mToolbar);


        mListView = findViewById(R.id.list_container);

        // Retrieving dummy playlist
        TrackHelper mTrackHelper = new TrackHelper();
        mPlayList = mTrackHelper.getTrackList();

        // Retrieving default list order
        Serializable mListingOrder = getIntent().getSerializableExtra(EXTRA_LIST_BY);
        switch ((ListingOrder) mListingOrder) {
            case ARTIST:
                mPlayList.sort((t1, t2) -> {return t1.getAuthor().compareToIgnoreCase(t2.getAuthor());});
                break;
            case GENRE:
                mPlayList.sort((t1,t2)->{return t1.getGenre().toString().compareToIgnoreCase(t2.getGenre().toString());});
                break;
            case RANK:
                mPlayList.sort((t1,t2)->{return (int)((t2.getRank()-t1.getRank())*100);});
                break;
            case TITLE:
                mPlayList.sort((t1,t2)->{return t1.getTitle().compareToIgnoreCase(t2.getTitle());});
                break;
            case NUMBER_OF_VOTES:
                mPlayList.sort((t1,t2)->{return t2.getNumberOfVotes()-t1.getNumberOfVotes();});
            default:
                break;
        }
        mAdapter = new PlaylistArrayAdapter(this, mPlayList);
        mListView.setAdapter(mAdapter);

    }

    /**
     * Setting up menu options for sorting tracks
     * @param menu the menu resource file
     * @return inflated menu0
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_tracks,menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Callback for sorting tracks. This also calls PlaylistArrayAdapter's
     * notifyDataSetChanged method in order to refresh playlist.
     * @param item the selected menuItem
     *
     */
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionMenuItemView mMenuItem=findViewById(R.id.menu_select);
        mMenuItem.setTitle(item.getTitle());
        switch (item.getItemId()){
            case R.id.menu_artist:
                mPlayList.sort((t1, t2) -> {return t1.getAuthor().compareToIgnoreCase(t2.getAuthor());});
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_genre:
                mPlayList.sort((t1,t2)->{return t1.getGenre().toString().compareToIgnoreCase(t2.getGenre().toString());});
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_rank:
                mPlayList.sort((t1,t2)->{return (int)((t2.getRank()-t1.getRank())*100);});
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_track:
                mPlayList.sort((t1,t2)->{return t1.getTitle().compareToIgnoreCase(t2.getTitle());});
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_votes:
                mPlayList.sort((t1,t2)->{return t2.getNumberOfVotes()-t1.getNumberOfVotes();});
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
