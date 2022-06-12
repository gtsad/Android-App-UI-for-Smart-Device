package com.example.uiproject;

import static com.example.uiproject.MainActivity.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyBroadcastReceiver extends BroadcastReceiver {

    SharedPreferences sharedpreferences;
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;

            assert notificationManager != null;
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel) ;
        }

        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;

        if(sharedpreferences.getBoolean("onbool", false)){

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("onoff", "off");
            editor.apply();
            sharedpreferences.edit().putBoolean("onbool", false).apply();
            sharedpreferences.edit().putBoolean("changes",true).apply();
    }
        else {

            sharedpreferences.edit().putString("onoff", "on").apply();
            sharedpreferences.edit().putBoolean("onbool", true).apply();
            sharedpreferences.edit().putBoolean("timer",false).apply();
            sharedpreferences.edit().putBoolean("changes",true).apply();

        }

        context.sendBroadcast(new Intent("INTERNET_LOST"));
    }

}