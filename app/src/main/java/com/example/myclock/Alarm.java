package com.example.myclock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class Alarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "Alarm";
    private Button buttonSetAlarm, buttonDeleteAlarm;
    private TextView textView;
    private String[] ringtones;
    private int ringtoneChosen;
    private Calendar calendar;
    private boolean timeSet;
    private RingtoneActivity ringtoneActivity;
    private AlarmService alarmService;
    private static Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ringtones = new String[]{"Swinging", "Morning Alarm", "Carol of the bells",
                "Nice Alarm Clock", "Clock buzz"};
        timeSet = false;
        ringtoneChosen = 0;
        ringtoneActivity = new RingtoneActivity(this);
        buttonSetAlarm = findViewById(R.id.buttonSetAlarm);
        textView = findViewById(R.id.textViewDisplayTime);
        buttonDeleteAlarm =  findViewById(R.id.buttonDeleteAlarm);
        alarmService = new AlarmService();
        serviceIntent = new Intent(this, alarmService.getClass());

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        timeSet = true;
        updateTimeText(calendar);

    }

    public void setAlarm(View view){
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"Set Alarm");
    }

    private void updateTimeText(Calendar calendar){
        String str = "Alarm set for ";
        str += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        textView.setText(str);
    }

    public void startAlarm(View view){
        if(!timeSet){
            String msg = "Set a time for the alarm!";
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            return;
        }
        String msg = "Alarm created successfully!";
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        serviceIntent.putExtra("RINGTONE",ringtoneChosen);
        serviceIntent.putExtra("TIME",calendar.getTimeInMillis());
        serviceIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        startService(serviceIntent);

    }

    public void deleteAlarm(View view){
        String str =  "No Alarm Set";
        textView.setText(str);
        if(timeSet) {
            stopService(serviceIntent);
            str = "Alarm deleted!";
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
            timeSet = false;
        }

    }

    public void chooseRingtone(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a ringtone");
        builder.setSingleChoiceItems(ringtones, ringtoneChosen, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ringtoneChosen = which;
                ringtoneActivity.playRingtone(ringtoneChosen,false);
            }

        });
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ringtoneActivity.stopRingtone(ringtoneChosen);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Alarm onDestroy - Stop service");
        if(timeSet) {
            stopService(serviceIntent); //Responsible for invoking modified onDestroy()
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            broadcastIntent.putExtra("RINGTONE", ringtoneChosen);
            broadcastIntent.putExtra("TIME", calendar.getTimeInMillis());
            this.sendBroadcast(broadcastIntent);
        }

    }
}