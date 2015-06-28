package nanodegree.spotifystreamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.services.ArtistRetrievalService;


public final class ArtistActivity extends Activity implements TextWatcher {


    private EditText searchBox;
    private Handler searchHandler = new Handler();

    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == searchBox || null == getApplicationContext()) {
                return;
            }
            String query = searchBox.getText().toString();
            if (query.isEmpty()) {
                return;
            }
            Intent searchIntent = new Intent(ArtistActivity.this, ArtistRetrievalService.class);
            searchIntent.putExtra("query", query);
            ArtistActivity.this.startService(searchIntent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        searchBox = (EditText)findViewById(R.id.searchEditText);
        searchBox.addTextChangedListener(this);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchHandler.removeCallbacks(searchRunnable);
        searchHandler.postDelayed(searchRunnable, 1000);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }


    @Override
    public void afterTextChanged(Editable s) { }


}
