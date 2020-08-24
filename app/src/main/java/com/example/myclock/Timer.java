package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private NumberPicker numberPickerHours, numberPickerMinutes, numberPickerSeconds;
    private TextView textViewTime;
    private boolean timerRunning;
    private Button buttonStartPause, buttonResetTimer;
    private CountDownTimer countDownTimer;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        numberPickerHours = (NumberPicker) findViewById(R.id.numberPickerHours);
        numberPickerMinutes = (NumberPicker) findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = (NumberPicker) findViewById(R.id.numberPickerSeconds);
        textViewTime = (TextView) findViewById(R.id.textViewTimerTime);
        buttonStartPause = (Button) findViewById(R.id.buttonStartPause);
        buttonResetTimer = (Button) findViewById(R.id.buttonResetTimer);
        timerRunning = false;
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

    }

    public void setTimer(View view){

         setTimerText(numberPickerHours.getValue(),
                numberPickerMinutes.getValue(),
                numberPickerSeconds.getValue());
        timeLeft = numberPickerHours.getValue()*60*60;
        timeLeft += numberPickerMinutes.getValue()*60;
        timeLeft += numberPickerSeconds.getValue();
        timeLeft *= 1000;
        buttonStartPause.setText("Start");
        buttonStartPause.setVisibility(View.VISIBLE);
    }

    private void setTimerText(int hours, int minutes, int seconds){

        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        textViewTime.setText(time);
    }

    public void startPauseTimer(View view){

        if(timerRunning){

            //Pause Timer
            countDownTimer.cancel();
            timerRunning = false;
            buttonStartPause.setText("Start");
        }
        else{

            //Start Timer
            countDownTimer = new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    long tl = timeLeft/1000;
                    int hours = (int)tl/(60*60);
                    tl = tl%(60*60);
                    int minutes = (int)tl/60;
                    tl = tl%60;
                    int seconds = (int)tl;
                    setTimerText(hours, minutes, seconds);
                }

                @Override
                public void onFinish() {

                    timerRunning = false;
                    buttonStartPause.setText("Start");
                    buttonStartPause.setVisibility(View.INVISIBLE);

                }
            }.start();
            timerRunning = true;
            buttonStartPause.setText("Pause");
        }
    }

    public void resetTimer(View view){

        if(timerRunning)
            countDownTimer.cancel();
        timeLeft = 0;
        setTimerText(0,0,0);
        timerRunning = false;
        buttonStartPause.setVisibility(View.INVISIBLE);
    }
}