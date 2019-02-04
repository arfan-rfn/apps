package com.exception.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random = new Random();
    Button playAgain;

    Button option1;
    Button option2;
    Button option3;
    Button option4;

    boolean gameIsActive;

    CountDownTimer countDown;
    TextView time;
    RelativeLayout startPage;
    RelativeLayout gamePage;
    GridLayout optionLayout;
    TextView question;
    TextView scoreBoard;
    TextView answer;
    int total;
    int score;
    int x, y;

    public void playAgain(View view){
        answer.setText("");
        total = 0;
        score = 0;
        setGameBoard();
        timer();
        scoreBoard.setText(String.valueOf(score) + "/" + String.valueOf(total));
        gameIsActive = true;
    }

    public void go(View view){
        startPage.setVisibility(View.INVISIBLE);
        gamePage.setVisibility(View.VISIBLE);
        timer();
        gameIsActive = true;
        total = 0;
        score = 0;
        setGameBoard();
    }

    public void options(View view){
        if (gameIsActive) {
            if (view.getTag().toString().equals(String.valueOf(x + y))) {
                score++;
                total++;
                answer.setText("Correct!");
            } else {
                total++;
                answer.setText("Incorrect");
            }
            scoreBoard.setText(String.valueOf(score) + "/" + String.valueOf(total));
            setGameBoard();
        }

    }

    public void setGameBoard(){

        x = random.nextInt(25)+1;
        y = random.nextInt(25)+1;
        int optionNum = random.nextInt(4);

        question.setText(String.valueOf(x) +" + " + String.valueOf(y));

        for (int i = 0; i < 4; i++){
            int randOption = random.nextInt(50)+1;
            Button temp = (Button) optionLayout.getChildAt(i);
            temp.setText(String.valueOf(randOption));
            temp.setTag(String.valueOf(randOption));
        }

        Button temp = (Button) optionLayout.getChildAt(optionNum);
        temp.setText(String.valueOf(x+y));
        temp.setTag(String.valueOf(x+y));



    }


    public void timer(){
        playAgain.setVisibility(View.INVISIBLE);
        countDown = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished/1000) + "s");
            }

            @Override
            public void onFinish() {
                String oldScore = scoreBoard.getText().toString();
                answer.setText("Your Score " + oldScore);
                answer.animate().rotation(3600).setDuration(2000);
                playAgain.setVisibility(View.VISIBLE);
                gameIsActive = false;

            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        optionLayout = findViewById(R.id.optionLayout);

        question = findViewById(R.id.question);
        scoreBoard = findViewById(R.id.scoreBoard);
        answer = findViewById(R.id.answer);

        time = findViewById(R.id.time);
        playAgain = findViewById(R.id.playAgain);
        startPage = findViewById(R.id.startPage);
        gamePage = findViewById(R.id.gamePage);
        gamePage.setVisibility(View.INVISIBLE);


    }
}
