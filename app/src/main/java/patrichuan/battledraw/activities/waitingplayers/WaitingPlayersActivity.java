package patrichuan.battledraw.activities.waitingplayers;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
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

/**
 * Created by Pat on 23/06/2016.
 */

public class WaitingPlayersActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private String roomName;
    private TextView tv_roomname;
    private ImageView iv_avatar_player1, iv_avatar_player2, iv_avatar_player3, iv_avatar_player4,
            iv_avatar_player5, iv_avatar_player6, iv_avatar_player7, iv_avatar_player8;

    private TextView tv_avatar_player1, tv_avatar_player2, tv_avatar_player3, tv_avatar_player4,
            tv_avatar_player5, tv_avatar_player6, tv_avatar_player7, tv_avatar_player8;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_players);

        instanceViews();

        mAuth = getmAuth();
        databaseReference = getDatabaseReference();

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");

        tv_roomname.setText(roomName);

        databaseReference.child("rooms").child(roomName).child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                doNewPlayerHasJoined();
                final String playerValue = dataSnapshot.getValue().toString();
                final String playerKey = dataSnapshot.getKey();

                if (playerValue.equals(Constants.PLAYER_TYPE_JOINER)) {
                    databaseReference.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final int numPlayersInside = (int) dataSnapshot.getChildrenCount();
                            databaseReference.child("players").child(playerKey).child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pictureUri = dataSnapshot.getValue().toString();
                                    setAvatar(numPlayersInside-1, pictureUri);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
                // FIXME Implementar "Leave Room" option
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void instanceViews() {
        tv_roomname = (TextView) findViewById(R.id.tv_roomname);
        iv_avatar_player1 = (ImageView) findViewById(R.id.iv_avatar_player1);
        iv_avatar_player2 = (ImageView) findViewById(R.id.iv_avatar_player2);
        iv_avatar_player3 = (ImageView) findViewById(R.id.iv_avatar_player3);
        iv_avatar_player4 = (ImageView) findViewById(R.id.iv_avatar_player4);
        iv_avatar_player5 = (ImageView) findViewById(R.id.iv_avatar_player5);
        iv_avatar_player6 = (ImageView) findViewById(R.id.iv_avatar_player6);
        iv_avatar_player7 = (ImageView) findViewById(R.id.iv_avatar_player7);
        iv_avatar_player8 = (ImageView) findViewById(R.id.iv_avatar_player8);

        tv_avatar_player1 = (TextView) findViewById(R.id.tv_avatar_player1);
        tv_avatar_player2 = (TextView) findViewById(R.id.tv_avatar_player2);
        tv_avatar_player3 = (TextView) findViewById(R.id.tv_avatar_player3);
        tv_avatar_player4 = (TextView) findViewById(R.id.tv_avatar_player4);
        tv_avatar_player5 = (TextView) findViewById(R.id.tv_avatar_player5);
        tv_avatar_player6 = (TextView) findViewById(R.id.tv_avatar_player6);
        tv_avatar_player7 = (TextView) findViewById(R.id.tv_avatar_player7);
        tv_avatar_player8 = (TextView) findViewById(R.id.tv_avatar_player8);
    }

    private void setAvatar(final int numPlayers, String pictureUri) {
        switch (numPlayers) {
            case 1:
                Glide.with(this).load(pictureUri).into(iv_avatar_player1);
                tv_avatar_player1.setText("Player 1");
                doOMGthisAvatarSong();
                break;
            case 2:
                Glide.with(this).load(pictureUri).into(iv_avatar_player2);
                tv_avatar_player2.setText("Player 2");
                doOMGthisAvatarSong();
                break;
            case 3:
                Glide.with(this).load(pictureUri).into(iv_avatar_player3);
                tv_avatar_player3.setText("Player 3");
                doOMGthisAvatarSong();
                break;
            case 4:
                Glide.with(this).load(pictureUri).into(iv_avatar_player4);
                tv_avatar_player4.setText("Player 4");
                doOMGthisAvatarSong();
                break;
            case 5:
                Glide.with(this).load(pictureUri).into(iv_avatar_player5);
                tv_avatar_player5.setText("Player 5");
                doOMGthisAvatarSong();
                break;
            case 6:
                Glide.with(this).load(pictureUri).into(iv_avatar_player6);
                tv_avatar_player6.setText("Player 6");
                doOMGthisAvatarSong();
                break;
            case 7:
                Glide.with(this).load(pictureUri).into(iv_avatar_player7);
                tv_avatar_player7.setText("Player 7");
                doOMGthisAvatarSong();
                break;
            case 8:
                Glide.with(this).load(pictureUri).into(iv_avatar_player8);
                tv_avatar_player8.setText("Player 8");
                doOMGthisAvatarSong();
                break;
        }
    }

    private void doOMGthisAvatarSong () {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.omg_this_avatar);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doNewPlayerHasJoined () {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.new_player_has_joined);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}