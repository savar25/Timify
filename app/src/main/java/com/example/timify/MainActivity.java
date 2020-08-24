package com.example.timify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.alarm:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new alarmFragment()).commit();
                        break;
                    case R.id.stopwatch:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new stopWatch()).commit();
                        break;
                    case R.id.timer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new Timer()).commit();
                }
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.container,new alarmFragment()).commit();
    }
}