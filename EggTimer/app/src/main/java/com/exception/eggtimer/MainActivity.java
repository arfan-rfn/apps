package com.exception.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    SeekBar seekBar;
    CountDownTimer countDownTimer;
    MediaPlayer mPlayer;
    Button button;
    long timerStart = 30;

    public void buttonClicked(View view){
        button = findViewById(R.id.button);
        if (button.getText().toString().equals("go!")){
            seekBar.setVisibility(View.INVISIBLE);
            button.setText("stop!");
            startCountDown(timerStart, 1);
        }else if (button.getText().toString().equals("stop!")){
            button.setText("go!");
            seekBar.setVisibility(View.VISIBLE);
            countDownTimer.cancel();

        }
    }

    public void startCountDown(final long max, final int repeat){
        countDownTimer = new CountDownTimer(max*1000, repeat*1000) {
            @Override
            public void onTick(long max) {
                convertTime((int)max/1000);
            }

            @Override
            public void onFinish() {
                mPlayer.start();
                seekBar.setProgress(30);
                button.setText("go!");
                seekBar.setVisibility(View.VISIBLE);
            }
        }.start();


    }

    public void convertTime(int time){
        textView.setText(String.valueOf(time/60) + ":"+ String.valueOf(time%60));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.timer);

        mPlayer = MediaPlayer.create(this, R.raw.chicken);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(600);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                convertTime(progress);
                timerStart = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
