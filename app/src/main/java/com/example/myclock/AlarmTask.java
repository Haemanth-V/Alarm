package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class AlarmTask extends AppCompatActivity {

    private int question, product, ringtone;
    private TextView textViewnum1, textViewnum2;
    private EditText editText;
    private RingtoneActivity ringtoneActivity;
    private Random rand;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_task);

        Intent intent = getIntent();
        ringtone = intent.getIntExtra("RINGTONE",0);
        question = 1;
        rand = new Random();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textViewnum1 = (TextView) findViewById(R.id.textViewNum1);
        textViewnum2 = (TextView) findViewById(R.id.textViewNum2);
        editText = (EditText) findViewById(R.id.editTextAnswer);
        ringtoneActivity = new RingtoneActivity(this);
        ringtoneActivity.playRingtone(ringtone, true);
        displayQuestion();
    }

    private void displayQuestion(){
        int n1, n2;
        switch(question){
            case 1 :
                n1 = rand.nextInt(5)+5;
                n2 = rand.nextInt(10)+5;
                break;
            case 2 :
                n1 = rand.nextInt(5)+10;
                n2 = rand.nextInt(10)+15;
                break;
            default :
                n1 = rand.nextInt(5)+20;
                n2 = rand.nextInt(10)+30;
        }
        product = n1*n2;
        textViewnum1.setText(Integer.toString(n1));
        textViewnum2.setText(Integer.toString(n2));
        question++;
    }

    public void submit(View view){
        if(TextUtils.isEmpty(editText.getText().toString().trim())){
            String str = "Enter an answer!";
            Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        }
        else{
            int answer = Integer.parseInt(editText.getText().toString());
            if(answer!=product){
                String str = "Wrong answer! Enter again";
                vibrator.vibrate(500);
                Toast.makeText(this,str,Toast.LENGTH_LONG).show();
            }
            else{
                if(question <=3 ){
                   displayQuestion();
                }
                else {
                    finish();
                    ringtoneActivity.stopRingtone(ringtone);
                }
            }
        }
    }
}