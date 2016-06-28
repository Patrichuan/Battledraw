package patrichuan.battledraw.activities.drawavatar;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;
import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.R;

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
                Bitmap bmp = drawLayout.obtainBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                // Create a reference to "test1.jpg"
                StorageReference avatarRef = storageRef.child("avatars").child("test1.jpg");
                avatarRef.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("DrawAvatarActivity", taskSnapshot.getMetadata().getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            }
        });
    }
}