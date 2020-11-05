package com.example.smartdataprotect_fyp;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RingingActivity extends AppCompatActivity {

     AudioManager audioManager;
     Button btnRing;
     int vib;
     int sil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);

       // registerReceiver(broadcastReceiver, new IntentFilter("ringingFunction"));

         btnRing = (Button) findViewById(R.id.btnRing);
         btnRing.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                ringingFunction();
             }
         });


    }

  /*  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            ringingFunction();
        }
    };*/

    public void ringingFunction(){
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        if (AudioManager.RINGER_MODE_VIBRATE ==1) {


            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        else if (AudioManager.RINGER_MODE_SILENT ==0){
          audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
      }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setVolume(101);
        }
        //  r.setLooping(true);

        r.play();
    }

    @Override
    public void onBackPressed() {
        finish();

        super.onBackPressed();
    }
}
