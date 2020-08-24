package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void alarm(View view){
        Intent intent = new Intent(this, Alarm.class);
        startActivity(intent);

    }

    public void timer(View view){
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);

    }

    public void stopWatch(View view){
        Intent intent = new Intent(this, StopWatch.class);
        startActivity(intent);

    }
}