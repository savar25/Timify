package com.example.timify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class alarmFragment extends Fragment {

    Spinner toner;
    TimePicker tp;
    TextView sun, mon, tue, wed, thu, frie, sat;
    CheckBox checkBox;
    FloatingActionButton fab;
    boolean s, m, t, w, th, f, sa = false;
    private static Integer REQUEST_CODE = 0;
    Integer choice;
    private static final String TAG = "alarmFragment";
    static AlarmAdapter adapter;
    static ArrayList<alarmer> alarmers;
    static RecyclerView recyclerView;
    database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        fab = view.findViewById(R.id.fab);
        database=new database(getContext());
        recyclerView=view.findViewById(R.id.recyclerView);
        alarmers=new ArrayList<>();
        alarmers=database.getVals();
        Collections.reverse(alarmers);
        adapter=new AlarmAdapter(alarmers,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = m = t = w = th = f = sa = false;
                LayoutInflater li = LayoutInflater.from(getContext());
                final View promptsView = li.inflate(R.layout.time_setter, null);


                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                alertDialogBuilder.setView(promptsView);

                tp = promptsView.findViewById(R.id.tp);
                tp.setIs24HourView(true);
                sun = promptsView.findViewById(R.id.sun);
                mon = promptsView.findViewById(R.id.mon);
                tue = promptsView.findViewById(R.id.tue);
                wed = promptsView.findViewById(R.id.wed);
                thu = promptsView.findViewById(R.id.thu);
                frie = promptsView.findViewById(R.id.fri);
                sat = promptsView.findViewById(R.id.sat);
                checkBox = promptsView.findViewById(R.id.checkBox);

                toner = promptsView.findViewById(R.id.ring_alert);
                ArrayList<String> names = new ArrayList<>();
                names.add("Default");
                names.add("Tone 1");
                names.add("Tone 2");
                names.add("Tone 3");
                names.add("Tone 4");
                names.add("Tone 5");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                toner.setAdapter(adapter);

                toner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        choice = adapterView.getSelectedItemPosition();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                            s=changeColour(sun, s);
                            m=changeColour(mon, m);
                            t=changeColour(tue, t);
                            w=changeColour(wed, w);
                            th=changeColour(thu, th);
                            f=changeColour(frie, f);
                            sa=changeColour(sat, sa);

                    }
                });

                sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       s= changeColour(sun, s);
                    }
                });

                mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m=changeColour(mon, m);
                    }
                });

                tue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        t=changeColour(tue, t);
                    }
                });
                wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        w=changeColour(wed, w);
                    }
                });
                thu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        th=changeColour(thu, th);
                    }
                });
                frie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f=changeColour(frie, f);
                    }
                });
                sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sa=changeColour(sat, sa);
                    }
                });

                alertDialogBuilder.setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                                calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
                                calendar.set(Calendar.SECOND, 0);
                                daysetter(calendar);
                            }
                        }).create().show();


            }
        });

        return view;
    }

    public void setScheduler(Calendar calendar, boolean repeat) {

        Log.d(TAG, "setScheduler: "+calendar);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), alarm.class);
        if(repeat) {
            intent.putExtra("hr", calendar.get(Calendar.HOUR_OF_DAY));
            intent.putExtra("min", calendar.get(Calendar.MINUTE));
            intent.putExtra("sec", calendar.get(Calendar.SECOND));
            intent.putExtra("repeat",false);
            intent.putExtra("day", calendar.get(Calendar.DAY_OF_WEEK));
        }else {
            intent.putExtra("repeat",false);
        }

        intent.putExtra("request", REQUEST_CODE);
        intent.putExtra("tone",choice);


        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(getContext(), REQUEST_CODE, intent, 0);
        } else {
            pendingIntent = PendingIntent.getService(getContext(), REQUEST_CODE, intent, 0);
        }


        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(getContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
        REQUEST_CODE++;

        String time=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
        database.add(new alarmer(time,new Boolean[]{m,t,w,th,f,sa,s}));
        alarmers.add(new alarmer(time,new Boolean[]{m,t,w,th,f,sa,s}));
        Log.d(TAG, "setScheduler: "+alarmers.size());

        alarmers=database.getVals();
        Collections.reverse(alarmers);
        adapter=new AlarmAdapter(alarmers,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


    }

    public boolean changeColour(TextView tv, boolean flag) {
        if (!flag) {
            tv.setTextColor(Color.BLUE);
        } else {
            tv.setTextColor(Color.GRAY);
        }
        return  !flag;

    }


    public void daysetter(Calendar calendar){
        if(s){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        setScheduler(calendar,false);}
        else if(m){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        setScheduler(calendar,false);}
        else if(t){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
        setScheduler(calendar,false);}
        else if(w){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
        setScheduler(calendar,false);}
        else if(th){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
        setScheduler(calendar,false);}
        else if(f){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
        setScheduler(calendar,false);}
        else if(sa){ calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        setScheduler(calendar,false);}
        else {
            Log.d(TAG, "daysetter: called");
            setScheduler(calendar,false);
        }
    }
}