package patrichuan.battledraw.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.Constants;
import patrichuan.battledraw.dao.Player;
import patrichuan.battledraw.R;
import patrichuan.battledraw.dao.Room;
import patrichuan.battledraw.activities.drawavatar.DrawAvatarActivity;
import patrichuan.battledraw.activities.splash.SplashActivity;
import patrichuan.battledraw.activities.waitingplayers.WaitingPlayersActivity;

/**
 * Created by Pat on 13/06/2016.
 */

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private TextInputLayout roomNameWrapper;
    private Button btnLogOut, btnJoinRoom, btnCreateRoom;
    private String roomName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = getmAuth();
        databaseReference = getDatabaseReference();

        instanceViews();
        setListeners();
    }

    private void instanceViews () {
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        roomNameWrapper = (TextInputLayout) findViewById(R.id.roomNameWrapper);
        btnJoinRoom = (Button) findViewById(R.id.btnJoinRoom);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);
    }

    private void setListeners() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (roomNameWrapper.getEditText()!=null) {
                    roomName = roomNameWrapper.getEditText().getText().toString();
                }

                if (!validateRoomName(roomName)) {
                    roomNameWrapper.setError("Not a valid room name!");
                } else {
                    databaseReference.child("rooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                roomNameWrapper.setErrorEnabled(false);
                                Intent intent = new Intent(MainActivity.this, DrawAvatarActivity.class);
                                intent.putExtra("ROOM_NAME", roomName);
                                startActivity(intent);
                            } else {
                                roomNameWrapper.setError("Sorry but room '" + roomName + "' doesnt exist !");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (roomNameWrapper.getEditText()!=null) {
                    roomName = roomNameWrapper.getEditText().getText().toString();
                }

                if (!validateRoomName(roomName)) {
                    roomNameWrapper.setError("Not a valid room name!");
                } else {
                    databaseReference.child("rooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                roomNameWrapper.setErrorEnabled(false);
                                doCreateRoom();
                                Intent intent = new Intent(MainActivity.this, WaitingPlayersActivity.class);
                                intent.putExtra("ROOM_NAME", roomName);
                                startActivity(intent);
                            } else {
                                roomNameWrapper.setError("Sorry but room '" + roomName + "' already exists !");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }



    private void doCreateRoom() {
        Map<String, Object> childUpdateMap;

        // Creo la lista de jugadores de la room con el creador como unico jugador
        Map<String, String> players = new HashMap<>();
        if (mAuth.getCurrentUser()!=null) {
            players.put(mAuth.getCurrentUser().getUid(), Constants.PLAYER_TYPE_CREATOR);
        } else {
            roomNameWrapper.setError("Sorry but you lost your session. Go to home !");
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
        }

        // Creo la room
        Room room = new Room();
        room.setPlayers(players);
        Map<String, Object> roomMap = room.toMap();
        childUpdateMap = new HashMap<>();
        childUpdateMap.put("/rooms/"+roomName, roomMap);
        databaseReference.updateChildren(childUpdateMap);

        // Creo el player
        Player player = new Player(mAuth.getCurrentUser().getEmail());
        player.setInRoom(roomName);
        Map<String, Object> playerMap = player.toMap();
        childUpdateMap = new HashMap<>();
        childUpdateMap.put("/players/"+mAuth.getCurrentUser().getUid(), playerMap);
        databaseReference.updateChildren(childUpdateMap);
    }
}