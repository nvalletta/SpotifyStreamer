package nanodegree.spotifystreamer.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import nanodegree.spotifystreamer.R;


public final class ArtistActivity extends SpotifyActivity implements TextWatcher {


    private static final String PREVIOUS_SEARCH_KEY = "PREVIOUS_SEARCH";
    private String previousSearch;
    private EditText searchBox;
    private final Handler searchHandler = new Handler();


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

            performArtistSearch(query);
            previousSearch = query;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        searchBox = (EditText)findViewById(R.id.searchEditText);
        searchBox.addTextChangedListener(this);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(PREVIOUS_SEARCH_KEY, previousSearch);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (null != savedInstanceState && !savedInstanceState.getString(PREVIOUS_SEARCH_KEY,"").isEmpty()) {
            previousSearch = savedInstanceState.getString(PREVIOUS_SEARCH_KEY, "");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(previousSearch)) {
            return;
        }
        searchHandler.removeCallbacks(searchRunnable);
        searchHandler.postDelayed(searchRunnable, 500);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }


    @Override
    public void afterTextChanged(Editable s) { }


}
