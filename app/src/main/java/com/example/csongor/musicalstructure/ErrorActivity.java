package com.example.csongor.musicalstructure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * All Activity errors lands in this Activity.
 * The Intent opening this activity holds error information in Serializable Extra ({@link ErrorMessage} enum).
 * Based on this info the title and description will inform users what shall they do to avoid this error.
 */
public class ErrorActivity extends AppCompatActivity {

    // declaring constants
    private static final String EXTRA_ERROR = "EXTRA_ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        // Assigning values to local variables
        TextView mErrorTitle = findViewById(R.id.txt_error_title);
        TextView mErrorDescription = findViewById(R.id.txt_error_description);
        TextView mButtonOk = findViewById(R.id.btn_error_right);

        // Setting up clickListener for mButtonOk. Pressing it, client will go back in MainActivity
        mButtonOk.setOnClickListener((View v) -> onBackPressed());

        // Getting error reason from Intent
        Intent intent = getIntent();
        ErrorMessage mErrorMessage = (ErrorMessage) intent.getSerializableExtra(EXTRA_ERROR);

        // Displaying error message based on Intent error message
        switch (mErrorMessage) {
            case NO_MEDIA:
                mErrorTitle.setText(R.string.txt_error_title_no_media);
                mErrorDescription.setText(R.string.txt_error_description_no_media);
                return;
            case NO_FILE:
                mErrorTitle.setText(R.string.txt_error_title_no_files_found);
                mErrorDescription.setText(R.string.txt_error_description_no_files_found);
                return;
            case MP3_FILE_QUERY_ERROR:
                mErrorTitle.setText(R.string.txt_error_title_query_failed);
                mErrorDescription.setText(R.string.txt_error_description_query_failed);
                return;
            default:
                mErrorTitle.setText(R.string.txt_error_title_no_permission_granted);
                mErrorDescription.setText(R.string.txt_error_description_no_permission_granted);
        }
    }
}
