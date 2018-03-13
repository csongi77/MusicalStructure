package com.example.csongor.musicalstructure;

import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ErrorActivity extends AppCompatActivity {

    private static final String EXTRA_ERROR = "EXTRA_ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Intent intent=getIntent();
        ErrorMessage mErrorMessage=(ErrorMessage)intent.getSerializableExtra(EXTRA_ERROR);
        switch (mErrorMessage){
            case NO_MEDIA:

        }
    }
}
