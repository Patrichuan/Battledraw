package patrichuan.battledraw.activities.waitingplayers;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Map;

import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.Constants;
import patrichuan.battledraw.R;
import patrichuan.battledraw.fragments.WaitingPlayersFragment;

/**
 * Created by Pat on 23/06/2016.
 */

public class NewWaitingPlayersActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newwaiting_players);

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
