package com.example.csongor.musicalstructure;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.csongor.musicalstructure.musichelpers.PlaylistStrategies;

import java.io.IOException;

/**
 * This Activity is the UI for playing {@link com.example.csongor.musicalstructure.musichelpers.Mp3MusicTrack}
 * retrieved from storage
 */
public class PlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    // Declaring constants
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_ERROR = "EXTRA_ERROR";
    private static final String EXTRA_PLAYLIST_ORDER = "EXTRA_PLAYLIST_ORDER";
    private static final String EXTRA_PLAYLIST_CREATION_STRATEGY = "EXTRA_PLAYLIST_CREATION_STRATEGY";
    private static final String BUNDLE_CURRENT_POSITION = "BUNDLE_CURRENT_POSITION";
    private static final String BUNDLE_IS_PLAYING = "BUNDLE_IS_PLAYING";
    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();
    // Declaring variables
    private SeekBar mSeekBar;
    private TextView mTimeRemaining;
    private TextView mTimeElapsed;
    private ImageButton mPlayPauseBtn;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private boolean isPlaying;
    private Handler mHandler;
    private AudioAttributes mAudioAttributes;
    private AudioFocusRequest mFocusRequest;
    private int mCurrentPosition, mDuration;
    private Runnable mSeekBarUpdater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Set up Views
        mTimeRemaining = findViewById(R.id.txt_time_remaining);
        mTimeElapsed = findViewById(R.id.txt_time_elapsed);
        mPlayPauseBtn = findViewById(R.id.btn_play_pause);
        ImageButton mBackToPlaylistBtn = findViewById(R.id.btn_to_playlist);
        TextView mSongTitle = findViewById(R.id.txt_player_title);
        mSeekBar = findViewById(R.id.seekbar);

        // Receiving TrackId and Title from intent
        Intent intent = getIntent();
        long mId = intent.getLongExtra(EXTRA_ID, -1L);
        String mTitle = intent.getStringExtra(EXTRA_TITLE);

        // Set up Track Title
        mSongTitle.setText(mTitle);

        // Converting Uri from TrackId
        Uri mContentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mId);

        // Set up AudioManager, AudioManager Attributes, FocusRequest & MediaPlayer
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAudioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
        }

        // Building AudioFocusRequest properties
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .setAudioAttributes(mAudioAttributes)
                    .setWillPauseWhenDucked(true)
                    .build();
        }

        // Creating new MediaPlayer if it not exists yet
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        // Resetting and preparing MediaPlayer for playing mp3
        mMediaPlayer.reset();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaPlayer.setAudioAttributes(mAudioAttributes);
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        try {
            mMediaPlayer.setDataSource(this, mContentUri);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();

            // Set up SeekBar Max value in order to display mTimeElapsed & mTimeRemaining values
            mDuration = mMediaPlayer.getDuration();
            mSeekBar.setMax(mDuration);
            Log.d(LOG_TAG, "MediaPlayer prepared");
        } catch (IOException e) {
            Log.e(LOG_TAG,"Something went wrong opening mp3");
            Intent intentError=new Intent(PlayerActivity.this,ErrorActivity.class);
            intentError.putExtra(EXTRA_ERROR,ErrorMessage.MP3_FILE_QUERY_ERROR);
            startActivity(intentError);
            e.printStackTrace();
        }

        // Assigning listener for SeekBar
        mSeekBar.setOnSeekBarChangeListener(this);

        // Assigning listeners for Pause/Play button
        mPlayPauseBtn.setOnClickListener(v -> {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                isPlaying = true;
                mPlayPauseBtn.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
            } else {
                mMediaPlayer.pause();
                isPlaying = false;
                mPlayPauseBtn.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
            }
        });

        // Assigning listener for Playlist button
        // It uses explicit intent as Reviewer needed
        mBackToPlaylistBtn.setOnClickListener(v -> {
            Intent toPlaylist = new Intent(PlayerActivity.this, PlayListActivity.class);
            toPlaylist.putExtra(EXTRA_PLAYLIST_CREATION_STRATEGY, PlaylistStrategies.MP3_PLAYLIST_FROM_FILE);
            startActivity(toPlaylist);
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "OnStart called");
        // Requesting AudioFocus depending on Android version
        int result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            result = mAudioManager.requestAudioFocus(mFocusRequest);
        } else {
            result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(LOG_TAG, "MediaPlayer request granted");
            mMediaPlayer.setOnCompletionListener(this);
            // Start playing and set isPlaying to true. This attribute is required on rotating screen
            mMediaPlayer.start();
            isPlaying = true;
            /**
             * Preparing Handler object and a Runnable in order to
             * update SeekBar status
             */
            if (mHandler == null) {
                mHandler = new Handler();
            }
            if (mSeekBarUpdater == null) {
                mSeekBarUpdater = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, "run");
                        if (mMediaPlayer != null && mHandler != null) {
                            if (mMediaPlayer.isPlaying()) {
                                // Refreshing SeekBar position
                                mCurrentPosition = mMediaPlayer.getCurrentPosition();
                                Log.d(LOG_TAG, "run-->mMediaPlayer.isPlaying==true");
                                mSeekBar.setProgress(mCurrentPosition);
                            }
                            // Refreshing
                            mHandler.postDelayed(this, 1000);
                        }
                    }
                };
            }
            PlayerActivity.this.runOnUiThread(mSeekBarUpdater);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    // free up some UI dependent resources
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop called");
        /**
         * Clearing up RunOnUiThread by setting mHandler to null
         * in order to free up resources
         */
        mHandler = null;
    }

    /**
     * When Activity is destroyed, stop mMediaPlayer
     * and clear up AudioManager
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy called");
        mMediaPlayer.stop();
        Log.d(LOG_TAG, "mMediaPlayer.stop called");
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        Log.d(LOG_TAG, "mMediaPlayer.release called");
        // Abandon AudioFocus depending on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(mFocusRequest);
        } else {
            mAudioManager.abandonAudioFocus(this);
        }
        Log.d(LOG_TAG, "abadonAudioFocusRequest called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Pause if music is being played & put into Bundle the state
        if (isPlaying) {
            outState.putBoolean(BUNDLE_IS_PLAYING, true);
        } else {
            outState.putBoolean(BUNDLE_IS_PLAYING, false);
        }
        // update & save current position
        mCurrentPosition = mMediaPlayer.getCurrentPosition();
        outState.putInt(BUNDLE_CURRENT_POSITION, mCurrentPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        /**
         * retrieve mPlayer status.
         * If playing==true, then continue playing else make sure it's paused
         */
        mCurrentPosition = savedInstanceState.getInt(BUNDLE_CURRENT_POSITION);
        isPlaying = savedInstanceState.getBoolean(BUNDLE_IS_PLAYING);
        mMediaPlayer.seekTo(mCurrentPosition);
        mSeekBar.setProgress(mCurrentPosition);
        if (!isPlaying) {
            /**
             * If the player was paused and the orientation has been changed, on restoreInstanceState
             * the icon must be set properly
             */
            mMediaPlayer.pause();
            mPlayPauseBtn.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
        }
    }

    // Seekbar Callback methods start

    /**
     * In this callback we visually update the SeekBar progress and
     * time status (booth elapsed and remaining)
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTimeElapsed.setText(millisToMinutes(progress));
        mTimeRemaining.setText(millisToMinutes(mDuration - progress));
        if (fromUser) {
            mMediaPlayer.pause();
            Log.d(LOG_TAG, "paused");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //required for SeekBar implementation, in this case it does nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mMediaPlayer.seekTo(progress);
        /**
         * If mPlayer Seek completed, check whether it playing music:
         * if it was paused state, don't start playing.
         */
        if (isPlaying)
            mMediaPlayer.start();
        Log.d(LOG_TAG, "playing");
    }
    // SeekBar Callback methods end


    // AudioFocusChange Callback
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                Log.d("OnAudioFocusChange", "REQUEST GRANTED");
                mMediaPlayer.start();
                return;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d(LOG_TAG, "AudioFocus loss transient");
                mMediaPlayer.pause();
                return;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.d(LOG_TAG, "AudioFocus loss transient can duck");
                mMediaPlayer.pause();
                return;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(LOG_TAG, "AudioFocus loss");
                mMediaPlayer.pause();
        }
    }

    // MediaPlayer Callback when finished, returning to playlist
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "MediaPlayer onCompletion");
        onBackPressed();
    }


    /**
     * Converting time from millis to mm:ss format
     *
     * @param millis time unit in milliseconds
     * @return String time in mm:ss format
     */
    private String millisToMinutes(int millis) {
        StringBuilder toReturn = new StringBuilder()
                .append(millis / (1000 * 60))
                .append(":");
        if (millis % 60000 / 1000 < 10) toReturn.append(0);
        toReturn.append(millis % 60000 / 1000);
        return toReturn.toString();
    }
}
