package com.exception.numbergussing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random = new Random();
    int randomNum = random.nextInt(20)+1;

    public void makeToast(String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();

    }

    public void GuessButton(View view){

        EditText guessNum = findViewById(R.id.guessNumber);

        if (!guessNum.getText().toString().equals("")) {

            Integer customerGuess = Integer.parseInt(guessNum.getText().toString());

            if (randomNum > customerGuess) {
                makeToast("try bigger");
            } else if (randomNum < customerGuess) {
                makeToast("try small");
            } else {
                makeToast("You got it man!!!");
                randomNum = random.nextInt(20) + 1;
            }
        }

        Log.i("the number", String.valueOf(randomNum));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
