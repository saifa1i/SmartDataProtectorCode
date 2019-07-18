package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class CommandList extends AppCompatActivity {

    Button btncmdSubmit;
    EditText cmdGps,cmdRing, cmdUnlcok, cmdWipdata, cmdCustom,cmdCall,txtusercmd;
    String gps,Ring,Unlock,wipeData,custom,call,userID;
    DataBaseHelper mydb = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_list);

        cmdCall = (EditText) findViewById(R.id.callcmd);
        cmdCustom = (EditText) findViewById(R.id.custommessagecmd);
        cmdGps = (EditText) findViewById(R.id.gpscmd);
        cmdRing = (EditText) findViewById(R.id.ringcmd);
        cmdUnlcok = (EditText) findViewById(R.id.unlockcmd);
        cmdWipdata = (EditText) findViewById(R.id.datawipecmd);
        txtusercmd = (EditText) findViewById(R.id.txtusercmd);


    btncmdSubmit= (Button) findViewById(R.id.btncmdSubmit);

        btncmdSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            call = cmdCall.getText().toString();
            custom = cmdCustom.getText().toString();
            gps = cmdGps.getText().toString();
            Ring = cmdRing.getText().toString();
            Unlock = cmdUnlcok.getText().toString();
            wipeData = cmdWipdata.getText().toString();
            userID = getIntent().getExtras().getString("userid");


            boolean userResult = mydb.checkUserID(userID);
            boolean result = mydb.insertCommands(gps,Ring,custom,Unlock,wipeData,call,userID);

            boolean valid = validation(call, custom, gps, Ring, Unlock, wipeData);
            if (valid == true){
if(userResult == true) {
    if (result == true) {
        Intent i = new Intent(CommandList.this, LoginActivity.class);
        startActivity(i);
        Toast.makeText(CommandList.this, "Commands Saved!", Toast.LENGTH_SHORT).show();

    } else {
        Toast.makeText(CommandList.this, "Error in Commands Saving!", Toast.LENGTH_SHORT).show();

    }
}else{
    Toast.makeText(CommandList.this,"Username dosen't Match!",Toast.LENGTH_LONG).show();
}

            } else {
                Toast.makeText(CommandList.this,"fix the errors",Toast.LENGTH_LONG).show();
            }
        }
    });
    }
    public  boolean validation(String call, String custom, String gps, String ring, String unLcok, String wipeData){
        boolean valid = true;
        if (call.isEmpty() ) {
            cmdCall.setError("Should not be empty");
            valid = false;
        } else {
            cmdCall.setError(null);
        }
        if (custom.isEmpty() ) {
            cmdCustom.setError("Should not be empty");
            valid = false;
        } else {
            cmdCustom.setError(null);
        }
        if (gps.isEmpty() ) {
            cmdGps.setError("Should not be empty");
            valid = false;
        } else {
            cmdGps.setError(null);
        }
        if (ring.isEmpty() ) {
            cmdRing.setError("Should not be empty");
            valid = false;
        } else {
            cmdRing.setError(null);
        }
        if (unLcok.isEmpty() ) {
            cmdUnlcok.setError("Should not be empty");
            valid = false;
        } else {
            cmdUnlcok.setError(null);
        }
        if (wipeData.isEmpty() ) {
            cmdWipdata.setError("Should not be empty");
            valid = false;
        } else {
            cmdWipdata.setError(null);
        }
        return valid;
    }



}
