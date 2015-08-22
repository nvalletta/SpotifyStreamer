package nanodegree.spotifystreamer.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import nanodegree.spotifystreamer.R;
import nanodegree.spotifystreamer.fragments.MusicPlayerDialogFragment;


public class MusicFragmentActivity extends SpotifyActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_music_player);

        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            launchMusicPlayerDialogFragment(getSupportFragmentManager());
        }
    }


    public static void launchMusicPlayerDialogFragment(FragmentManager fm) {
        MusicPlayerDialogFragment musicPlayerDialogFragment = new MusicPlayerDialogFragment();

        if (!deviceHasSmallScreen) {
            musicPlayerDialogFragment.show(fm, "player");
        } else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.add(R.id.music_player_activity_root, musicPlayerDialogFragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


}
