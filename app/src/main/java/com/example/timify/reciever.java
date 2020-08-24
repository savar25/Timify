package com.example.timify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;

public class reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       alarm.player.stop();
    }
}
