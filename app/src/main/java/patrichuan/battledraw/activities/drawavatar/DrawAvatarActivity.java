package patrichuan.battledraw.activities.drawavatar;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.R;

/**
 * Created by Pat on 23/06/2016.
 */

public class DrawAvatarActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_avatar);

        mAuth = getmAuth();
        databaseReference = getDatabaseReference();

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");
    }
}