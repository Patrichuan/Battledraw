package patrichuan.battledraw.activities.splash;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import patrichuan.battledraw.BaseActivity;
import patrichuan.battledraw.R;
import patrichuan.battledraw.activities.initial.InitialActivity;
import patrichuan.battledraw.activities.main.MainActivity;

/**
 * Created by Pat on 13/06/2016.
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = "Splash";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = getmAuth();
        checkCurrentUser();
    }

    private void checkCurrentUser() {
        Intent intent;
        if (mAuth.getCurrentUser() != null) {
            // already signed in
            intent = new Intent(this, MainActivity.class);
        } else {
            // not signed in
            intent = new Intent(this, InitialActivity.class);
        }
        startActivity(intent);
    }
}