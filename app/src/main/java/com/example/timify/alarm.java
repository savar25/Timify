package com.example.timify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class alarm extends Service {

    int[] music=new int[]{0,R.raw.tone1,R.raw.tone2,R.raw.tone3,R.raw.tone4,R.raw.tone5};
    private static  String stepCover="Note";
    Notification notification;
    static MediaPlayer player;
    private static final String TAG = "alarm";
    database database;

    @Override
    public void onCreate() {
        super.onCreate();

        database=new database(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d(TAG, "onCreateAlarm: called");
            NotificationChannel stepCheck = new NotificationChannel(stepCover,"Alarm Time", NotificationManager.IMPORTANCE_HIGH);
            stepCheck.setDescription("Trial for channel");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(stepCheck);
            Intent intent=new Intent(getApplicationContext(),reciever.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

            notification=new NotificationCompat.Builder(getApplicationContext(),stepCover)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Alarm")
                    .setContentText("Ring Time")
                    .setContentIntent(pendingIntent)
                    .build();


        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: called");
        boolean flg=intent.getBooleanExtra("repeat",false);
        final Integer hr=intent.getIntExtra("hr",0);
        final Integer min=intent.getIntExtra("min",0);

           if(intent.getIntExtra("tone",9)==0){
               player=MediaPlayer.create(getApplicationContext(),Settings.System.DEFAULT_RINGTONE_URI);
           }else {
               player=MediaPlayer.create(getApplicationContext(),music[intent.getIntExtra("tone",1)]);
           }

           player.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.stop();
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hr);
                calendar.set(Calendar.MINUTE,min);
                calendar.set(Calendar.SECOND,0);
                String time=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
                 database.del(time);
                alarmFragment.alarmers=database.getVals();
                Collections.reverse(alarmFragment.alarmers);

                Log.d(TAG, "onStartCommand: "+alarmFragment.alarmers.size());
                alarmFragment.recyclerView.setAdapter(new AlarmAdapter(alarmFragment.alarmers,getApplicationContext()));
                alarmFragment.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }
        },5000);



        if(flg){
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(getApplicationContext(), alarm.class);
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,intent.getIntExtra("hr",0));
            calendar.set(Calendar.MINUTE,intent.getIntExtra("min",0));
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.DAY_OF_WEEK,intent.getIntExtra("day",1));
            intent1.putExtra("hr", calendar.get(Calendar.HOUR_OF_DAY));
            intent1.putExtra("min", calendar.get(Calendar.MINUTE));
            intent1.putExtra("sec", calendar.get(Calendar.SECOND));
            intent1.putExtra("repeat",false);
            intent1.putExtra("day", calendar.get(Calendar.DAY_OF_WEEK));


            intent1.putExtra("request", intent.getIntExtra("request",0));
            intent1.putExtra("tone",intent.getIntExtra("tone",0));

            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getForegroundService(getApplicationContext(), intent.getIntExtra("request",0), intent1, 0);
            } else {
                pendingIntent = PendingIntent.getService(getApplicationContext(), intent.getIntExtra("request",0), intent1, 0);
            }


            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        startForeground(1,notification);

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setup(){

    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }
}
