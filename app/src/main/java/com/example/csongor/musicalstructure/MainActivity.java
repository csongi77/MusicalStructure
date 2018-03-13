package com.example.csongor.musicalstructure;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.csongor.musicalstructure.musichelpers.PlaylistStrategies;

public class MainActivity extends AppCompatActivity {
    // set up constants
    private static final String EXTRA_PLAYLIST_CREATION_STRATEGY = "EXTRA_PLAYLIST_CREATION_STRATEGY";
    private static final String EXTRA_ERROR = "EXTRA_ERROR";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 42;
    // declare variables
    private AlertDialog mAlertDialog;
    private LinearLayout mDummyButton;
    private LinearLayout mMp3Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Assigning values to variables
        mDummyButton = findViewById(R.id.btn_dummy_track);
        mMp3Button = findViewById(R.id.btn_mp3_track);

        // Assigning listeners to buttons
        mDummyButton.setOnClickListener(v->openPlaylist(PlaylistStrategies.DUMMY_PLAYLIST));
        mMp3Button.setOnClickListener(v->performCheck());

    }


    /**
     * Got request persmission result for PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
     * If permission is granted, go and load Mp3 playlist
     * If permission denied, open Error Activity to show detailed description why we need permission.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // we've got permission for reading external storage, go on with process
            openPlaylist(PlaylistStrategies.MP3_PLAYLIST_FROM_FILE);
        } else {
            // permission denied, open Error Activity
            openErrorActivity(ErrorMessage.NO_PERMISSION);
        }

    }

    /**
     * Checking storage availability and persmissions for reading Music directory
     */
    private void performCheck() {
        // Check whether external storage is available. If not, open Error Activity
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)&&
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)!=null) {
            openErrorActivity(ErrorMessage.NO_MEDIA);
        }
        // check permissions for accessing external storage. If not, request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // show why we need permission
                showReasonDialog();
                super.onStart();
            } else {
                // go and request permission
                goRequestPermission();
            }
        } else {
            openPlaylist(PlaylistStrategies.MP3_PLAYLIST_FROM_FILE);
        }
        super.onStart();
    }

    /**
     * requesting permission for reading external storage
     */
    private void goRequestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
    }

    /**
     * show why we need permission for reading storage
     */
    private void showReasonDialog() {
        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(this);
        mAlertDialog = mAlertDialogBuilder.setTitle(R.string.dialog_permission_request_title)
                .setMessage(R.string.dialog_permission_request_message)
                // on positive answer request permission again
                .setPositiveButton(R.string.dialog_positive_button, (v, i) -> {
                    goRequestPermission();
                    mAlertDialog.hide();
                })
                // on negative answer stop application
                .setNegativeButton(R.string.dialog_negative_button, (v, i) -> {
                    mAlertDialog.hide();
                    openErrorActivity(ErrorMessage.NO_PERMISSION);
                })
                .create();
        mAlertDialog.show();
    }

    /**
     * Calling playlistActivity via Intent
     *
     * @param strategy how to create tracklist
     */
    private void openPlaylist(PlaylistStrategies strategy) {
        Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
        intent.putExtra(EXTRA_PLAYLIST_CREATION_STRATEGY, strategy);
        startActivity(intent);
    }

    /**
     * calling Error Activity via Intent
     */
    private void openErrorActivity(ErrorMessage reason) {
        Intent intent = new Intent(MainActivity.this, ErrorActivity.class);
        intent.putExtra(EXTRA_ERROR, reason);
        startActivity(intent);
    }
}
