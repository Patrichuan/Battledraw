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
            Intent intent = new Intent(MainActivity.this, CreatorBaseActivity.class);
            intent.putExtra("ROOM_NAME", roomName);
            startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, JoinerBaseActivity.class);
            intent.putExtra("ROOM_NAME", roomName);
            startActivity(intent);
        } else {
            roomNameWrapper.setError("Sorry but you lost your session. Go to home !");
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }
}