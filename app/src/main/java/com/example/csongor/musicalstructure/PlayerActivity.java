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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();

    // Declaring variables
    private SeekBar mSeekBar;
    private TextView mTimeRemaining, mTimeElapsed;
    private ImageButton mPlayPauseBtn, mBackToPlaylistBtn;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private boolean isPaused;
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

        // Converting Uri from TrackId
        mContentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mId);

        // Set up AudioManager & MediaPlayer
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
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioAttributes(mAudioAttributes);

        try {
            mMediaPlayer.setDataSource(this, mContentUri);
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
            mMediaPlayer.start();
            // Set up mSeekBar Handler & Update mSeekBar in a different Thread
            mHandler = new Handler();

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null ) {
                        mCurrentPosition = mMediaPlayer.getCurrentPosition();
                        mTimeElapsed.setText(String.valueOf(mCurrentPosition ));
                        mTimeRemaining.setText(String.valueOf(mDuration - mCurrentPosition ));
                        mSeekBar.setProgress(mCurrentPosition);
                        mHandler.postDelayed(this, 1000);
                    }
                }

            });

        }

    }

    /**
     * Seekbar Callback methods start
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // mCurrentPosition=progress;
        mTimeElapsed.setText(String.valueOf(mCurrentPosition));
        mTimeRemaining.setText(String.valueOf(mDuration - mCurrentPosition));
        if (fromUser) {
                isPaused = true;
                mMediaPlayer.pause();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        Log.d(LOG_TAG, "duration=" + mDuration);
        Log.d(LOG_TAG, "paused");
            mMediaPlayer.seekTo(progress);
            mMediaPlayer.start();
    }
    // SeeBar Callback methods end


    // AudioFocusChange Callback
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                Log.e("OnAudioFocusChange", "REQUEST GRANTED");
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
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mAudioManager.abandonAudioFocusRequest(mFocusRequest);
                onBackPressed();
                return;
        }
    }

    // MediaPlayer Callback
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "MediaPlayer onCompletion");
        mMediaPlayer = mp;
        mp.stop();
        mp.release();
        mAudioManager.abandonAudioFocusRequest(mFocusRequest);
        onBackPressed();
    }
}
