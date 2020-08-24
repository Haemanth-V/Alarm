package com.example.myclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int ringtone = intent.getIntExtra("RINGTONE",0);
        long timeInMillis = intent.getLongExtra("TIME",0);
        Log.d(TAG, "onReceive Restarter");
        Intent serviceIntent = new Intent(context,AlarmService.class);
        serviceIntent.putExtra("RINGTONE",ringtone);
        serviceIntent.putExtra("TIME",timeInMillis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else
            context.startService(serviceIntent);
    }
}

