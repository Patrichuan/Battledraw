package patrichuan.battledraw.presentation.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.presentation.player_flows.creator.activities.CreatorBaseActivity;
import patrichuan.battledraw.presentation.player_flows.joiner.activities.JoinerBaseActivity;
import patrichuan.battledraw.util.Constants;
import patrichuan.battledraw.model.Player;
import patrichuan.battledraw.R;
import patrichuan.battledraw.model.Room;
import patrichuan.battledraw.presentation.activities.splash.SplashActivity;

/**
 * Created by Pat on 13/06/2016.
 */

public class MainActivity extends BaseActivity {

    private TextInputLayout roomNameWrapper;
    private Button btnLogOut, btnJoinRoom, btnCreateRoom;
    private String roomName = "";
    private Map<String, Object> childUpdateMap;
    private Map<String, String> players = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                getmAuth().signOut();
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
                    // Check if room already exists or not
                    getDatabaseReference().child("rooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                roomNameWrapper.setErrorEnabled(false);
                                doJoinRoom();
                                Intent intent = new Intent(MainActivity.this, JoinerBaseActivity.class);
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
                    getDatabaseReference().child("rooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                roomNameWrapper.setErrorEnabled(false);
                                doCreateRoom();
                                Intent intent = new Intent(MainActivity.this, CreatorBaseActivity.class);
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
        final FirebaseUser currentUser = getmAuth().getCurrentUser();
        if (currentUser!=null) {
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
        } else {
            roomNameWrapper.setError("Sorry but you lost your session. Go to home !");
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void doJoinRoom () {
        final FirebaseUser currentUser = getmAuth().getCurrentUser();
        if (currentUser!=null) {
            getDatabaseReference().child("rooms").child(roomName).child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
                    if( players == null ) {
                        System.out.println("No players");
                    }
                    else {
                        if (!players.containsKey(currentUser.getUid())) {
                            Map<String, Object> childUpdateMap;

                            // Creo el player
                            Player player = new Player(currentUser.getEmail());
                            player.setInRoom(roomName);
                            Map<String, Object> playerMap = player.toMap();
                            childUpdateMap = new HashMap<>();
                            childUpdateMap.put("/players/"+currentUser.getUid(), playerMap);
                            getDatabaseReference().updateChildren(childUpdateMap);

                            // Actualizo la lista de jugadores de la room
                            players.put(currentUser.getUid(), Constants.PLAYER_TYPE_JOINER);
                            childUpdateMap = new HashMap<>();
                            childUpdateMap.put("/rooms/"+roomName+"/players/", players);
                            getDatabaseReference().updateChildren(childUpdateMap);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            roomNameWrapper.setError("Sorry but you lost your session. Go to home !");
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }
}