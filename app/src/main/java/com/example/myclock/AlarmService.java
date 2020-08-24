package com.example.myclock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class AlarmService extends Service {

    private int ringtoneChosen;
    private long timeInMillis;
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        Log.d(TAG, "onCreate: Service");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL = "example.Notification";
        String CHANNEL_NAME = "Service";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager!=null;
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL);
        notification = builder.setOngoing(true)
                .setContentTitle("Alarm Service")
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ringtoneChosen = intent.getIntExtra("RINGTONE",0);
        timeInMillis = intent.getLongExtra("TIME",0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this, AlertReceiver.class);
        intent1.putExtra("RINGTONE", ringtoneChosen);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent1, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
            Log.d(TAG, "Alarm set!");
        }else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra("RINGTONE",ringtoneChosen);
        broadcastIntent.putExtra("TIME",timeInMillis);
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this,Restarter.class);
        Log.d(TAG, "onDestroy - Restart Service! ");
        this.sendBroadcast(broadcastIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
