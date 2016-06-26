package patrichuan.battledraw.activities.waitingplayers;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
                final String playerValue = dataSnapshot.getValue().toString();

                if (playerValue.equals(Constants.PLAYER_TYPE_JOINER)) {
                    databaseReference.child("rooms").child(roomName).child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> players = (Map<String, String>) dataSnapshot.getValue();
                            if( players == null ) {
                                System.out.println("No players");
                            } else{
                                // Player N-1 (se resta 1 debido a que hay un creator player)
                                setAvatar(players.size()-1);
                            }
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
    }

    private void setAvatar(int n) {
        switch (n) {
            case 1:
                iv_avatar_player1.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 2:
                iv_avatar_player2.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 3:
                iv_avatar_player3.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 4:
                iv_avatar_player4.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 5:
                iv_avatar_player5.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 6:
                iv_avatar_player6.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 7:
                iv_avatar_player7.setBackgroundResource(R.color.colorPrimaryDark);
                break;
            case 8:
                iv_avatar_player8.setBackgroundResource(R.color.colorPrimaryDark);
                break;
        }
    }
}