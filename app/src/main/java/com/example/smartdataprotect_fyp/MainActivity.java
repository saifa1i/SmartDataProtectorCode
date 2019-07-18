package com.example.smartdataprotect_fyp;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;
   public String mUserMobilePhone;
    AudioManager audioManager;
    private static MainActivity ins;
    static final int RESULT_Enable = -1;

    private LocationManager locationManager;
    private LocationListener locationListener;

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    String commandReceived,lati,lon;

    Double strlatitude,strlongtitude;

    DataBaseHelper mydb = new DataBaseHelper(this);
    SharedPreferences mSharedPreferences;
    public static MainActivity getInst()
    {
        return ins;
    }
    //BroadcastReceiver broadcastReceiver;
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";

    public static final String CHANNEL_NAME = "Notification Channel";
    private static android.graphics.BitmapFactory BitmapFactory ;
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    public static final int NOTIFICATION_ID = 101;
    String Number;

    NotificationCompat.Builder builder;
    Notification notification;

    CardView cvGps,cvRing,cvCustom,cvWipedata,cvUnlock,cvCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerReceiver(broadcastReceiver, new IntentFilter("performFunction"));


        cvCall = (CardView) findViewById(R.id.cvCall);
        cvRing = (CardView) findViewById(R.id.cvRing);
        cvGps = (CardView) findViewById(R.id.cvGps);
        cvCustom = (CardView) findViewById(R.id.cvCustom);
        cvWipedata = (CardView) findViewById(R.id.cvWipedata);
        cvUnlock = (CardView) findViewById(R.id.cvUnlock);


        cvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CallForwardingActivity.class);
                startActivity(i);
            }
        });


        cvWipedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DataWipeActivity.class);
                startActivity(i);
            }
        });

        cvGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(i);
            }
        });

        cvRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RingingActivity.class);
                startActivity(i);
            }
        });

        cvUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UnlockActivity.class);
                startActivity(i);
            }
        });

        cvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CustomMessageActivity.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    //SMSREADING
        ins = this;


        if (!hasReadSmsPermission()) {

            showRequestPermissionsInfoAlertDialog();
        }


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserMobilePhone = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            performFunction(commandReceived);
        }
    };

    //SMSREADING:
    private void showRequestPermissionsInfoAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }
    public void updateUI(final String s, final String number)
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
             commandReceived = s;
             Number = number;

           performFunction(commandReceived);            }
        });
    }

   @RequiresApi(api = Build.VERSION_CODES.O)
   public void performFunction(String command){

        boolean checkring = mydb.checkRing(command);
       boolean checkgps = mydb.checkGps(command);
       boolean checkwipe = mydb.checkWipe(command);
       boolean checkCustom = mydb.checkCustom(command);
       boolean checkunlock = mydb.checkUnlock(command);
       boolean checkcall = mydb.checkCall(command);

        if (checkring == true){
           ringFunction();
            Toast.makeText(MainActivity.this,"Command match! Ringing",Toast.LENGTH_LONG).show();
        }
        else if (checkCustom == true){
            customFunction();
            Toast.makeText(MainActivity.this,"Command match! Notification On!",Toast.LENGTH_LONG).show();
        }
        else if (checkgps == true){
            gpsFunction();
            Toast.makeText(MainActivity.this,"Command match! Location Found",Toast.LENGTH_LONG).show();
        }
        else if (checkwipe == true){
            wipeDataFunction();
            Toast.makeText(MainActivity.this,"Command match! DataErasing!",Toast.LENGTH_LONG).show();
        }
        else if (checkunlock == true){
            unlcokFunction();
            Toast.makeText(MainActivity.this,"Command match! UNLOCKED!",Toast.LENGTH_LONG).show();
        }
        else if (checkcall == true){
            Toast.makeText(MainActivity.this,"Command match! CallForwarding "+Number,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this,"Command Doesn't match!",Toast.LENGTH_LONG).show();
        }
    }

    public void ringFunction(){
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

    public void wipeDataFunction(){
        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

        componentName = new ComponentName(MainActivity.this,AdminSupport.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active){
            devicePolicyManager.wipeData(4);
        }
        else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable admin ap!");
            startActivityForResult(intent, RESULT_Enable);
        }
    }

    public void gpsFunction(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // txtgps.append("\n" + location.getLatitude() + "\n" + location.getLongitude());
            strlatitude = location.getLatitude();
            strlongtitude = location.getLongitude();
            lati = String.valueOf(strlatitude);
            lon = String.valueOf(strlongtitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 1);

            return;
        } else {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //locationFunction();
            locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);

        }

      //  SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(Number,null,"Latitude "+lati+"longitude: "+lon,null,null);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Number,null,"Latitude "+lati+"longitude: "+lon,null,null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                //locationFunction();
                locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
                return;
        }
    }



    public void customFunction(){

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);

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
        builder.setContentText("Kindly Contact at "+Number);
        builder.setSmallIcon(R.mipmap.app_logo_round);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));
        notification = builder.build();
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1002, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.mipmap.app_logo_round, getResources().getString(R.string.app_name), pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
    }

    public void unlcokFunction(){
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();
        Context context;
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "myapp:mywakelogtag");
        wakeLock.acquire();

    }

    public void callForwardingFunction(){

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
           Intent i = new Intent(MainActivity.this,MainActivity.class);
           startActivity(i);
        } else if (id == R.id.updateCommands) {
            Intent i = new Intent(MainActivity.this,UpdateCommand.class);
            startActivity(i);
        }  else if (id == R.id.rateus) {
            Intent i = new Intent(MainActivity.this,RateUsActivity.class);
            startActivity(i);
        } else if (id == R.id.help) {
            Intent i = new Intent(MainActivity.this,helpUsActivity.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
