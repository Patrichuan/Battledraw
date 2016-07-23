package patrichuan.battledraw.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import patrichuan.battledraw.BattleDrawApp;
import patrichuan.battledraw.util.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity {

// TODO
/*
    - Delete room if creator leave:
        - change players inroom field
        - delete room
    - Rearrange players order if player leave
    - Refactor
 */

    private Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
    private BattleDrawApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (BattleDrawApp) getApplication();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > Constants.PASSWORD_MIN_LENGH && password.length()<=Constants.PASSWORD_MAX_LENGH;
    }

    public boolean validateRoomName(String roomName) {
        return roomName.length() > Constants.ROOMNAME_MIN_LENGH && roomName.length()<=Constants.ROOMNAME_MAX_LENGH;
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public FirebaseAuth getmAuth () {
        return app.getmAuth();
    }

    public DatabaseReference getDatabaseReference () {
        return app.getDatabaseReference();
    }

    public StorageReference getStorageReference () {
        return app.getStorageReference();
    }
}