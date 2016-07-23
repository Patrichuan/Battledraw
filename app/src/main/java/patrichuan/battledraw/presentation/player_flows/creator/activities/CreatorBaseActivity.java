package patrichuan.battledraw.presentation.player_flows.creator.activities;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patrichuan.battledraw.model.Player;
import patrichuan.battledraw.model.Room;
import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.R;
import patrichuan.battledraw.presentation.player_flows.creator.fragments.WaitingPlayersFragment;
import patrichuan.battledraw.util.Constants;

/**
 * Created by Pat on 23/06/2016.
 */

public class CreatorBaseActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private Map<String, String> players = new HashMap<>();

    private String roomName;
    private Map<String, Object> childUpdateMap;
    private ArrayList<String> joiners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_base);

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");
        setNewCreator ();

        joiners = new ArrayList<>();

        WaitingPlayersFragment waitingPlayersFragment = new WaitingPlayersFragment();
        waitingPlayersFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, WaitingPlayersFragment.newInstance(extras.getString("ROOM_NAME")))
                .commit();
    }

    private void setNewCreator () {
        FirebaseUser currentUser = getmAuth().getCurrentUser();
        if (currentUser != null) {
            players.put(getmAuth().getCurrentUser().getUid(), Constants.PLAYER_TYPE_CREATOR);

            // Creo la room
            Room room = new Room();
            room.setPlayers(players);
            Map<String, Object> roomMap = room.toMap();
            childUpdateMap = new HashMap<>();
            childUpdateMap.put("/rooms/"+roomName, roomMap);
            getDatabaseReference().updateChildren(childUpdateMap);

            // Creo el player
            Player player = new Player(getmAuth().getCurrentUser().getEmail());
            player.setInRoom(roomName);
            Map<String, Object> playerMap = player.toMap();
            childUpdateMap = new HashMap<>();
            childUpdateMap.put("/players/"+getmAuth().getCurrentUser().getUid(), playerMap);
            getDatabaseReference().updateChildren(childUpdateMap);
        }
    }







    public void addJoiner (String uid) {
        joiners.add(uid);
    }

    public void removeJoiner (String uid) {
        for (int i=0; i<joiners.size(); i++) {
            if (joiners.get(i).equals(uid)) {
                joiners.remove(i);
                break;
            }
        }
    }

    public int getJoinerPos (String uid) {
        return joiners.indexOf(uid)+1;
    }

    public int getNumJoiners () {
        return joiners.size();
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
