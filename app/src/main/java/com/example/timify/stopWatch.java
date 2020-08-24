package com.example.timify;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class stopWatch extends Fragment {



    Chronometer chronometer;
    TextView mills;
   long millis;
    long medianOffset;
    Button start,stop,reset,lap;
    Boolean run=false,isStop=false;
    CountDownTimer countDownTimer;
    ArrayList<String> times=new ArrayList<>();
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stop_watch, container, false);
        chronometer=view.findViewById(R.id.chronometer2);
        mills=view.findViewById(R.id.sec);
        start=view.findViewById(R.id.start1);
        stop=view.findViewById(R.id.stop);
        reset=view.findViewById(R.id.reset1);
        lap=view.findViewById(R.id.Lap);
        listView=view.findViewById(R.id.list);



        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                millis=0;
                countDownTimer=new CountDownTimer(1000,1) {

                    @Override
                    public void onTick(long l) {
                        if (!isStop) {
                            if (millis == 0) {
                                millis = 1000;
                            }
                            millis = l;
                            mills.setText(":" + String.valueOf(1000 - millis));
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStop=false;
                if (!run) {
                    if(countDownTimer!=null){
                        countDownTimer.start();
                    }
                    chronometer.setBase(SystemClock.elapsedRealtime() - medianOffset);
                    chronometer.start();
                    run = true;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                countDownTimer.cancel();
                medianOffset = SystemClock.elapsedRealtime() - chronometer.getBase();

                run = false;
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStop=true;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();

                countDownTimer.cancel();

                millis=0;
                mills.setText(":"+String.valueOf(0));

                medianOffset = 0;
                run = false;

                times=new ArrayList<>();
                listView.setAdapter(null);



            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                times.add(String.valueOf(chronometer.getText())+":"+String.valueOf(1000-millis));
                ListAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,android.R.id.text1,times){
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //Use super class to create the View
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView)v.findViewById(android.R.id.text1);

                        //Put the image on the TextView

                        tv.setText(times.get(position));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                        //Add margin between image and text (support various screen densities)

                        return v;
                    }
                };

                listView.setAdapter(adapter);

            }
        });

        return view;
    }
}