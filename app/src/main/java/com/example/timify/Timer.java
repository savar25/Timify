package com.example.timify;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Timer extends Fragment {


    TimePicker tp;
    NumberPicker np;
    Button start,pause,reset;
    Integer hr,min,sec;
    long millis;
    CountDownTimer countDownTimer;
    boolean isRunning,isPaused=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_timer, container, false);

        tp=view.findViewById(R.id.timePicker);
        np=view.findViewById(R.id.secondPicker);
        start=view.findViewById(R.id.start);
        reset=view.findViewById(R.id.reset);
        pause=view.findViewById(R.id.pause);
        tp.setHour(0);
        tp.setMinute(0);
        np.setMinValue(00);
        np.setMaxValue(59);
        tp.setIs24HourView(true);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning|| isPaused) {
                    isRunning=true;
                    isPaused = false;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, tp.getHour());
                    calendar.set(Calendar.MINUTE, tp.getMinute());
                    calendar.set(Calendar.SECOND, np.getValue());
                    sec = np.getValue();
                    millis = calendar.getTimeInMillis();
                    startCountdown(millis);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countDownTimer!=null && isRunning){
                    countDownTimer.cancel();
                    isPaused=true;
                    isRunning=false;
                }
                Toast.makeText(getContext(), "Timer Paused", Toast.LENGTH_SHORT).show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tp.setMinute(0);
                tp.setHour(0);
                np.setValue(00);
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                    isPaused=false;
                    isRunning=false;
                }

                Toast.makeText(getContext(), "Timer Reset", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void startCountdown(long millis){
         countDownTimer=new CountDownTimer(millis,1000) {
            @Override
            public void onTick(long l) {
                sec--;
                if(sec==-1){
                    if(tp.getMinute()==0 && tp.getHour()==0){
                        Toast.makeText(getContext(), "Timer Done", Toast.LENGTH_SHORT).show();
                        final MediaPlayer player=MediaPlayer.create(getContext(), Settings.System.DEFAULT_RINGTONE_URI);
                        player.start();
                        countDownTimer.cancel();
                        isRunning=false;
                        isPaused=true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.stop();
                            }
                        },5000);
                        return;
                    }else if(tp.getMinute()>0){
                        sec=59;
                        if(tp.getMinute()==0){
                            tp.setMinute(59);
                            tp.setHour(tp.getHour()-1);
                        }else {
                            tp.setMinute(tp.getMinute() - 1);
                        }
                    }
                }
                np.setValue(sec);

            }

            @Override
            public void onFinish() {

            }

        }.start();
    }
}