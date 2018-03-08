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
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // set up constants
    private static final String EXTRA_LIST_BY = "EXTRA_LIST_BY";
    private static final String EXTRA_REASON_TO_QUIT = "REASON_TO_QUIT";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 42;
    // declare variables
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check whether external storage is available. If not, quit application
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)&&
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)!=null) {
            sayGoodbye(ReasonToQuit.NO_MEDIA);
        }
        // check permissions for accessing external storage. If not, request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // show dialog box why we need permission
                showReasonDialog();
            } else {
                // go and request permission
                goRequestPermission();
            }
            onStart();
        } else {
            onStart();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * assigning listeners for buttons
         */
        Button mListByArtist = findViewById(R.id.btn_list_by_artist);
        mListByArtist.setOnClickListener(v -> openPlaylist(ListingOrder.ARTIST));

        Button mListByGenre = findViewById(R.id.btn_list_by_genre);
        mListByGenre.setOnClickListener(v-> openPlaylist(ListingOrder.GENRE));

        Button mListByTitle = findViewById(R.id.btn_list_by_title);
        mListByTitle.setOnClickListener(v-> openPlaylist(ListingOrder.TITLE));

        Button mListByRank = findViewById(R.id.btn_list_by_rank);
        mListByRank.setOnClickListener(v->openPlaylist(ListingOrder.RANK));

        Button mListByVotes = findViewById(R.id.btn_list_by_vote_number);
        mListByVotes.setOnClickListener(v->openPlaylist(ListingOrder.NUMBER_OF_VOTES));
    }

    /**
     * got request persmission result for PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // we've got permission for reading external storage, go on with process
            onStart();
        } else {
            // permission denied, say goodbye
            sayGoodbye(ReasonToQuit.NO_PERMISSION);
        }

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
                    sayGoodbye(ReasonToQuit.NO_PERMISSION);
                })
                .create();
        mAlertDialog.show();
    }

    /**
     * calling playlistActivity
     *
     * @param orderBy how to order tracks
     */
    private void openPlaylist(ListingOrder orderBy) {
        Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
        intent.putExtra(EXTRA_LIST_BY, orderBy);
        startActivity(intent);
    }

    /**
     * calling goodbye intent
     */
    private void sayGoodbye(ReasonToQuit reason) {
        Intent intent = new Intent(MainActivity.this, GoodbyeActivity.class);
        intent.putExtra(EXTRA_REASON_TO_QUIT, reason);
        startActivity(intent);
    }
}
