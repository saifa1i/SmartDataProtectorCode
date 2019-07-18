package com.example.smartdataprotect_fyp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CustomMessageActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    public static final String CHANNEL_NAME = "Notification Channel";
    private static android.graphics.BitmapFactory BitmapFactory ;
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    public static final int NOTIFICATION_ID = 101;
    String Number;

    Button btnCustom;
    NotificationCompat.Builder builder;
    Notification notification;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_message);


        btnCustom = (Button) findViewById(R.id.btnCustom);
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
        //Boolean value to set if lights are enabled for Notifications from this Channel
        notificationChannel.enableLights(true);
        //Boolean value to set if vibration are enabled for Notifications from this Channel
        notificationChannel.enableVibration(true);
        //Sets the color of Notification Light
        notificationChannel.setLightColor(Color.GREEN);
        //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
        notificationChannel.setVibrationPattern(new long[] {
                500,
                500,
                500,
                500,
                500
        });

 builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Smart Data Protector If You find this mobile!");
        builder.setContentText("Kindly Contact at"+Number);
        builder.setSmallIcon(R.mipmap.app_logo_round);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));
 notification = builder.build();
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1002, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.mipmap.app_logo_round, getResources().getString(R.string.app_name), pendingIntent);


        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customFucntion();
            }
        });

    }

    public void customFucntion(){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(CustomMessageActivity.this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

    }
}
