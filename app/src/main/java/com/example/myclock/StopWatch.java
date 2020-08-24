package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StopWatch extends AppCompatActivity {

    private TextView textViewTime;
    private Button buttonPlayPause, buttonReset, buttonLap;
    private boolean isRunning;
    private long timeMillis, timePause, timeStart, timeUpdate;
    private int lapNumber;
    private LinearLayout lapView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        textViewTime = (TextView) findViewById(R.id.textViewStopwatchTime);
        buttonPlayPause = (Button) findViewById(R.id.buttonPlayPause);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonLap = (Button) findViewById(R.id.buttonLap);
        lapView = (LinearLayout) findViewById(R.id.lapTimes);
        handler = new Handler();
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeMillis = SystemClock.uptimeMillis() - timeStart;
            timeUpdate = timeMillis + timePause;
            int seconds = (int)timeUpdate/1000;
            int minutes = seconds/60;
            seconds %=60;
            int milliSeconds = (int)timeUpdate%1000/10;
            textViewTime.setText(String.format("%02d:%02d.%02d",minutes,seconds,milliSeconds));
            handler.postDelayed(this,60);
        }
    };


    public void playPause(View view){

        if(!isRunning){
            timeStart = SystemClock.uptimeMillis();
            handler.postDelayed(runnable,0);
            isRunning = true;
            buttonPlayPause.setText("Pause");
            buttonLap.setVisibility(View.VISIBLE);
        }
        else{
            isRunning = false;
            timePause += timeMillis;
            handler.removeCallbacks(runnable);
            buttonPlayPause.setText("Start");
            buttonLap.setVisibility(View.INVISIBLE);
        }
    }

    public void reset(View view){
        timeMillis = 0;
        timePause = 0;
        timeUpdate = 0;
        timePause = 0;
        lapNumber = 0;
        lapView.removeAllViews();
        textViewTime.setText("00:00.00");
        if(isRunning) {
            timeStart = SystemClock.uptimeMillis();
        }
        else
            timeStart = 0;
    }

    public void lap(View view){

        lapNumber++;
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addView = layoutInflater.inflate(R.layout.row,null);
        TextView textView = (TextView) addView.findViewById(R.id.txtContent);
        textView.setText(lapNumber +")  " + textViewTime.getText());
        lapView.addView(addView);
    }
}