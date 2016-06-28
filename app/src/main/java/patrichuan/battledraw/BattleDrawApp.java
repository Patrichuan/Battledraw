package patrichuan.battledraw;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import patrichuan.battledraw.activities.splash.SplashActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Pat on 12/06/2016.
 */

public class BattleDrawApp extends Application {

    private static final String TAG = "BattleDrawApp";

    private static Context mContext;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    int backButtonCount;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        backButtonCount = 0;
        InitializeFirebase();

        // Default Font: Roboto-Regular.ttf
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    // FIREBASE RELATED METHODS-----------------------------------------------------------------------------
    private void InitializeFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(getAuthListener());
    }

    public FirebaseAuth.AuthStateListener getAuthListener () {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    logOutIntent();
                } else {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in");
                }
            }
        };
        return mAuthListener;
    }

    public FirebaseAuth getmAuth () {
        return mAuth;
    }

    public DatabaseReference getDatabaseReference () {
        return databaseReference;
    }

    public StorageReference getStorageReference () {
        return storageRef;
    }
    // OTHER RELATED METHODS--------------------------------------------------------------------------------

    // Log Out intent
    public void logOutIntent () {
        Intent i = new Intent(mContext, SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    // Exit App if use back button twice
    public void ExitIfTwiceBack (CoordinatorLayout Coordinator) {
        if(backButtonCount >= 1) {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            Snackbar snackbar = Snackbar.make(Coordinator, "Pulsa de nuevo 'back' para salir de la aplicación", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor((ContextCompat.getColor(Coordinator.getContext(), R.color.colorPrimary)));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.f);
            snackbar.show();
            backButtonCount++;
        }
    }
}