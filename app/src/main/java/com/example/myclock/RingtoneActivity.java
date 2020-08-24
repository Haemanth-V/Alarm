package com.example.myclock;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class RingtoneActivity {

    private MediaPlayer mediaPlayer[];
    private int ringtone;

    public RingtoneActivity(Context context){

        mediaPlayer = new MediaPlayer[5];
        mediaPlayer[0] = MediaPlayer.create(context, R.raw.swinging);
        mediaPlayer[1] = MediaPlayer.create(context, R.raw.morning_alarm);
        mediaPlayer[2] = MediaPlayer.create(context, R.raw.carol_of_the_bells);
        mediaPlayer[3] = MediaPlayer.create(context, R.raw.nice_alarm_clock);
        mediaPlayer[4] = MediaPlayer.create(context, R.raw.clock_buzz);

    }

    public void playRingtone(int i, boolean looping){
        for(int j=0;j<5;j++)
            if(mediaPlayer[j].isPlaying())
                stopRingtone(j);
        mediaPlayer[i].start();
        mediaPlayer[i].setLooping(looping);
    }

    public void stopRingtone(int i){
        mediaPlayer[i].stop();
    }
}
