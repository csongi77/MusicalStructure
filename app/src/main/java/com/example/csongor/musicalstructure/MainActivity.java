package com.example.csongor.musicalstructure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_LIST_BY="EXTRA_LIST_BY";
    Button mListByArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check permissions for reading files
        checkPermissions();
        mListByArtist=findViewById(R.id.btn_list_by_artist);
        mListByArtist.setOnClickListener(v->listByArtist());
    }

    private void checkPermissions() {

    }

    private void listByArtist() {
        Intent intent=new Intent(MainActivity.this,PlayListActivity.class);
        intent.putExtra(EXTRA_LIST_BY,ListingOrder.ARTIST);
        startActivity(intent);
    }
}
