package patrichuan.battledraw.presentation.player_flows.creator.activities;

import android.media.MediaPlayer;
import android.os.Bundle;

import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.R;
import patrichuan.battledraw.presentation.player_flows.creator.fragments.WaitingPlayersFragment;

/**
 * Created by Pat on 23/06/2016.
 */

public class CreatorBaseActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_base);

        Bundle extras = getIntent().getExtras();

        WaitingPlayersFragment waitingPlayersFragment = new WaitingPlayersFragment();
        waitingPlayersFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, WaitingPlayersFragment.newInstance(extras.getString("ROOM_NAME")))
                .commit();
    }

    public void doOMGthisAvatarSong () {
        mediaPlayer = MediaPlayer.create(this, R.raw.omg_this_avatar);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    public void doNewPlayerHasJoined () {
        mediaPlayer = MediaPlayer.create(this, R.raw.new_player_has_joined);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }
}
