package patrichuan.battledraw.presentation.player_flows.joiner.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import patrichuan.battledraw.R;
import patrichuan.battledraw.model.Player;
import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.presentation.player_flows.joiner.fragments.DrawAvatarFragment;
import patrichuan.battledraw.util.Constants;

/**
 * Created by Pat on 12/07/2016.
 */

public class JoinerBaseActivity extends BaseActivity {

    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joiner_base);

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");
        setNewJoiner();

        DrawAvatarFragment drawAvatarFragment = new DrawAvatarFragment();
        drawAvatarFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, DrawAvatarFragment.newInstance(roomName), "DrawAvatarFragment")
                .commit();
    }

    private void setNewJoiner () {
        getDatabaseReference().child("rooms").child(roomName).child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked")
                Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
                if (players != null) {
                    FirebaseUser currentUser = getmAuth().getCurrentUser();
                    if (currentUser != null && !players.containsKey(currentUser.getUid())) {
                        Map<String, Object> childUpdateMap;

                        // Creo el player
                        Player player = new Player(currentUser.getEmail());
                        player.setInRoom(roomName);
                        Map<String, Object> playerMap = player.toMap();
                        childUpdateMap = new HashMap<>();
                        childUpdateMap.put("/players/" + currentUser.getUid(), playerMap);
                        getDatabaseReference().updateChildren(childUpdateMap);

                        // Actualizo la lista de jugadores de la room
                        players.put(currentUser.getUid(), Constants.PLAYER_TYPE_JOINER);
                        childUpdateMap = new HashMap<>();
                        childUpdateMap.put("/rooms/" + roomName + "/players/", players);
                        getDatabaseReference().updateChildren(childUpdateMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (activeFragment instanceof DrawAvatarFragment) {
            doLeaveRoom();
        }
        super.onBackPressed();
    }

    private void doLeaveRoom() {
        if (getmAuth().getCurrentUser()!=null) {
            final String playerId = getmAuth().getCurrentUser().getUid();
            getDatabaseReference().child("rooms").child(roomName).child("players").child(playerId).removeValue();
        }
    }
}