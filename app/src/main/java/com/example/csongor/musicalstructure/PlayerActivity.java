package com.example.csongor.musicalstructure;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    // Declaring constants
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TITLE="EXTRA_TITLE";
    private static final String BUNDLE_CURRENT_POSITION = "BUNDLE_CURRENT_POSITION";
    private static final String BUNDLE_IS_PLAYING = "BUNDLE_IS_PLAYING";
    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();
    private final Object mLock = new Object();
    // Declaring variables
    private SeekBar mSeekBar;
    private TextView mTimeRemaining, mTimeElapsed;
    private ImageButton mPlayPauseBtn, mBackToPlaylistBtn;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private boolean isPlaying;
    private Handler mHandler;
    private AudioAttributes mAudioAttributes;
    private AudioFocusRequest mFocusRequest;
    private long mId;
    private Uri mContentUri;
    private int mCurrentPosition, mDuration;


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Set up Views
        mTimeRemaining = findViewById(R.id.txt_time_remaining);
        mTimeElapsed = findViewById(R.id.txt_time_elapsed);
        mPlayPauseBtn = findViewById(R.id.btn_play_pause);
        mBackToPlaylistBtn = findViewById(R.id.btn_to_playlist);
        mSeekBar = findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);

        // Recieving TrackId
        Intent intent = getIntent();
        mId = intent.getLongExtra(EXTRA_ID, -1L);
        String mTitle = intent.getStringExtra(EXTRA_TITLE);

        // Converting Uri from TrackId
        mContentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mId);

        // Set up AudioManager, AudioManager Attributes, FocusRequest & MediaPlayer
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(this)
                .setAudioAttributes(mAudioAttributes)
                .setWillPauseWhenDucked(true)
                .build();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.reset();
        mMediaPlayer.setAudioAttributes(mAudioAttributes);

        try {
            mMediaPlayer.setDataSource(this, mContentUri);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();

            // Set up SeekBar Max value in order to display mTimeElapsed & mTimeRemaining values
            mDuration = mMediaPlayer.getDuration();
            mSeekBar.setMax(mDuration);
            Log.d(LOG_TAG, "MediaPlayer prepared");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int result = mAudioManager.requestAudioFocus(mFocusRequest);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(LOG_TAG, "MediaPlayer request granted");
            mMediaPlayer.setOnCompletionListener(this);
            // Start playing and set
            mMediaPlayer.start();
            isPlaying = true;
            // Set up mSeekBar Handler & Update mSeekBar in a different Thread
            mHandler = new Handler();

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "run");
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mCurrentPosition = mMediaPlayer.getCurrentPosition();
                            Log.d(LOG_TAG, "run-->mMediaPlayer.isPlaying==true");
                            mSeekBar.setProgress(mCurrentPosition);
                        }
                        mHandler.postDelayed(this, 1000);
                    }
                }
            });
        }


        // Assigning listeners for buttons
        mPlayPauseBtn.setOnClickListener(v -> {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                isPlaying=true;
                mPlayPauseBtn.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
            } else {
                mMediaPlayer.pause();
                isPlaying=false;
                mPlayPauseBtn.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "OnStart called");
    }

    /**
     * When Activity becomes invisible, stop mMediaPlayer
     * and clear up AudioManager
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop called");
        synchronized (mLock) {
            mMediaPlayer.stop();
            Log.d(LOG_TAG, "mMediaPlayer.stop called");
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            Log.d(LOG_TAG, "mMediaPlayer.release called");
            mAudioManager.abandonAudioFocusRequest(mFocusRequest);
            Log.d(LOG_TAG, "abadonAudioFocusRequest called");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Pause if music is being played & put into Bundle the state
        if (mMediaPlayer.isPlaying()) {
            outState.putBoolean(BUNDLE_IS_PLAYING, true);
        } else {
            outState.putBoolean(BUNDLE_IS_PLAYING, false);
        }
        mMediaPlayer.pause();
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
        mMediaPlayer.pause();
        if (isPlaying) mMediaPlayer.start();


    }

    /**
     * Seekbar Callback methods start
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // mCurrentPosition=progress;
        mTimeElapsed.setText(millisToMinutes(progress));
        mTimeRemaining.setText(millisToMinutes(mDuration -progress));
        if (fromUser) {
            mMediaPlayer.pause();
            Log.d(LOG_TAG, "paused");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mMediaPlayer.seekTo(progress);
        /**
         * If mPlayer Seek completed, check that was it playing music:
         * if it was paused state, don't start playing.
         */
        if (isPlaying)
            mMediaPlayer.start();
        Log.d(LOG_TAG, "playing");
    }
    // SeeBar Callback methods end


    // AudioFocusChange Callback
    @RequiresApi(api = Build.VERSION_CODES.O)
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

    // MediaPlayer Callback when finished
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "MediaPlayer onCompletion");

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
