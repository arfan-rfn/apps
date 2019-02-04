package com.exception.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private final static String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupFirebaseAuth();
    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void logIn(View view) {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        if (!email.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty()){
            Log.d(TAG, "clicked to Log in ");
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "unsuccessful log in", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            Toast.makeText(this, "email or password  is empty", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupFirebaseAuth(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "user log in as: " + user.getUid());
                }else {
                    Log.d(TAG, "user logged out");
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }
}
