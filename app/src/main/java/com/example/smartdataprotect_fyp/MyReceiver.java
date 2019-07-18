package com.example.smartdataprotect_fyp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";
    TextView viewText;
    String smsSender = "";
    String smsBody = "";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
     //   throw new UnsupportedOperationException("Not yet implemented");
       context.sendBroadcast(new Intent("performFunction"));
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {


            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody += smsMessage.getMessageBody();

                smsSender += smsMessage.getOriginatingAddress().toString();

            }

            if
            (MainActivity.getInst() != null){
                MainActivity.getInst().updateUI(smsBody,smsSender);
               // MainActivity.getInst().updateUI(smsSender);
            }
            Log.d(TAG, "Sms Received");

        //    Log.d(TAG, "SMS detected: From " + smsSender + " With text " + smsBody);

            Toast.makeText(context, "BroadcastReceiver caught SMS: from "+smsSender + " "+  smsBody, Toast.LENGTH_LONG).show();


        }
    }

}
