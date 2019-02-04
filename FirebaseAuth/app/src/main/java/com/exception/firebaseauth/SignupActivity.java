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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    ProgressBar progressBar;


    public static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progressBar);
    }

    public void signUp(View view) {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.pass);
        EditText confirm = findViewById(R.id.confirmPass);
        
        if (!email.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() &&
                !confirm.getText().toString().isEmpty() &&
                password.getText().toString().equals(confirm.getText().toString())){
            signUpNewEmail(email.getText().toString(), password.getText().toString());

        }else{
            Toast.makeText(this, "something worng", Toast.LENGTH_SHORT).show();
        }

    }

    private void signUpNewEmail(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.e(TAG, "signup successful: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(SignupActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
