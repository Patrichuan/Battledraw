package patrichuan.battledraw.presentation.activities.initial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import patrichuan.battledraw.presentation.BaseActivity;
import patrichuan.battledraw.R;
import patrichuan.battledraw.presentation.activities.main.MainActivity;

/**
 * Created by Pat on 13/06/2016.
 */

public class InitialActivity extends BaseActivity {

    private static final String TAG = "Initial";

    private String email, password;
    private Button btnSignIn, btnSignUp;
    private TextInputLayout emailWrapper, passwordWrapper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        instanceViews();
        setListeners();

        mAuth = getmAuth();
    }

    private void instanceViews () {
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }

    private void setListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                email = emailWrapper.getEditText().getText().toString();
                password = passwordWrapper.getEditText().getText().toString();
                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    emailWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    doSignIn();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                email = emailWrapper.getEditText().getText().toString();
                password = passwordWrapper.getEditText().getText().toString();
                if (!validateEmail(email)) {
                    emailWrapper.setError("Not a valid email address!");
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    emailWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    doSignUp();
                }
            }
        });
    }

    private void doSignIn() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(InitialActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InitialActivity.this, user.getEmail() + " authentication sucesfull!.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void doSignUp () {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(InitialActivity.this, email + " SignUp failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InitialActivity.this, email + " SignUp succesfull.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}