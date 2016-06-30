package patrichuan.battledraw.activities.drawavatar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;
import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.Constants;
import patrichuan.battledraw.Player;
import patrichuan.battledraw.R;
import patrichuan.battledraw.activities.main.MainActivity;
import patrichuan.battledraw.activities.splash.SplashActivity;

/**
 * Created by Pat on 23/06/2016.
 */

public class DrawAvatarActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String roomName;
    private DrawableView drawLayout;
    private Button btnIAmDone;

    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_avatar);

        storageRef = getStorageReference();
        databaseReference = getDatabaseReference();

        mAuth = getmAuth();

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");

        drawLayout = (DrawableView) findViewById(R.id.drawLayout);
        if (drawLayout!=null) {
            drawLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    DrawableViewConfig config = new DrawableViewConfig();
                    config.setStrokeColor(getResources().getColor(android.R.color.black));
                    config.setStrokeWidth(20.0f);
                    config.setMinZoom(1.0f);
                    config.setMaxZoom(3.0f);
                    config.setCanvasHeight(drawLayout.getHeight());
                    config.setCanvasWidth(drawLayout.getWidth());
                    drawLayout.setConfig(config);
                }
            });
        }

        btnIAmDone = (Button) findViewById(R.id.btnIAmDone);
        btnIAmDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doJoinRoom();
            }
        });
    }

    private void doJoinRoom () {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            final StorageReference avatarRef = storageRef.child("avatars").child(currentUser.getUid());
            Bitmap bmp = drawLayout.obtainBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] data = baos.toByteArray();

            avatarRef.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageMetadata metadata = taskSnapshot.getMetadata();
                            if (metadata != null && metadata.getDownloadUrl() != null) {
                                final String avatarUri = metadata.getDownloadUrl().toString();
                                final String player_uid = currentUser.getUid();
                                databaseReference.child("players").child(player_uid).child("picture").setValue(avatarUri);
                                databaseReference.child("rooms").child(roomName).child("players").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                player.setPicture(avatarUri);
                                                Map<String, Object> playerMap = player.toMap();
                                                childUpdateMap = new HashMap<>();
                                                childUpdateMap.put("/players/"+currentUser.getUid(), playerMap);
                                                databaseReference.updateChildren(childUpdateMap);

                                                // Actualizo la lista de jugadores de la room
                                                players.put(currentUser.getUid(), Constants.PLAYER_TYPE_JOINER);
                                                childUpdateMap = new HashMap<>();
                                                childUpdateMap.put("/rooms/"+roomName+"/players/", players);
                                                databaseReference.updateChildren(childUpdateMap);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {
            finish();
        }
    }
}