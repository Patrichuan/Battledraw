package patrichuan.battledraw.activities.drawavatar;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_avatar);

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


        mAuth = getmAuth();
        databaseReference = getDatabaseReference();

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("ROOM_NAME");
    }
}