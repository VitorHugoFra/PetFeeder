package com.example.vitor.myapplication.extra;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.vitor.myapplication.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channel1 = "channel1TD";
    public static final String channel1Name = "Channel 1";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel = new NotificationChannel(channel1, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if (mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
    return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message ){
        return new NotificationCompat.Builder(getApplicationContext(),channel1)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_pets_black_24dp);
    }
}
