package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCommand extends AppCompatActivity {

    Button btnupdatecmd;
    EditText ncmdGps,ncmdRing, ncmdUnlcok, ncmdWipdata, ncmdCustom, ncmdCall, usercmd;
    String ngps,nRing,nUnlock,nwipeData,ncustom,ncall, cmduser;

    DataBaseHelper mydb = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_command);

        ncmdCall = (EditText) findViewById(R.id.ncallcmd);
        ncmdCustom = (EditText) findViewById(R.id.ncustommessagecmd);
        ncmdGps = (EditText) findViewById(R.id.ngpscmd);
        ncmdRing = (EditText) findViewById(R.id.nringcmd);
        ncmdUnlcok = (EditText) findViewById(R.id.nunlockcmd);
        ncmdWipdata = (EditText) findViewById(R.id.ndatawipecmd);
        usercmd = (EditText) findViewById(R.id.usernamecmd);


        btnupdatecmd = (Button) findViewById(R.id.btnupdatecmd);



        btnupdatecmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ncall = ncmdCall.getText().toString();
                ncustom = ncmdCustom.getText().toString();
                ngps = ncmdGps.getText().toString();
                nRing = ncmdRing.getText().toString();
                nUnlock = ncmdUnlcok.getText().toString();
                nwipeData = ncmdWipdata.getText().toString();
                cmduser = usercmd.getText().toString();

                boolean updateResult = mydb.updateCommand(ngps, nRing,  ncustom, nUnlock, nwipeData, ncall, cmduser);

                boolean valid = validation(ncall, ncustom, ngps, nRing, nUnlock, nwipeData, cmduser);
                if (valid == true){
                    if (updateResult == true){
                    Toast.makeText(UpdateCommand.this,"Commands Received",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateCommand.this,MainActivity.class);
                    startActivity(i);

                }else {
                        Toast.makeText(UpdateCommand.this,"Error in updating commands",Toast.LENGTH_SHORT).show();

                    }} else {
                    Toast.makeText(UpdateCommand.this,"fix the errors",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public  boolean validation(String call, String custom, String gps, String ring, String unLcok, String wipeData, String cmdU){
        boolean valid = true;
        if (cmdU.isEmpty() || cmdU.length() < 3) {
            usercmd.setError("Incorrect or dosen't Match!");
            valid = false;
        } else {
            usercmd.setError(null);
        }
        if (call.isEmpty() ) {
            ncmdCall.setError("Should not be empty");
            valid = false;
        } else {
            ncmdCall.setError(null);
        }
        if (custom.isEmpty() ) {
            ncmdCustom.setError("Should not be empty");
            valid = false;
        } else {
            ncmdCustom.setError(null);
        }
        if (gps.isEmpty() ) {
            ncmdGps.setError("Should not be empty");
            valid = false;
        } else {
            ncmdGps.setError(null);
        }
        if (ring.isEmpty() ) {
            ncmdRing.setError("Should not be empty");
            valid = false;
        } else {
            ncmdRing.setError(null);
        }
        if (unLcok.isEmpty() ) {
            ncmdUnlcok.setError("Should not be empty");
            valid = false;
        } else {
            ncmdUnlcok.setError(null);
        }
        if (wipeData.isEmpty() ) {
            ncmdWipdata.setError("Should not be empty");
            valid = false;
        } else {
            ncmdWipdata.setError(null);
        }
        return valid;
    }
}
