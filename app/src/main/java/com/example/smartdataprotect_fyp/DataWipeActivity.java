package com.example.smartdataprotect_fyp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DataWipeActivity extends AppCompatActivity {

    Button btnWipe,btnAdmin;
    static final int RESULT_Enable = -1;
    public static final int USES_POLICY_WIPE_DATA = 4;

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_wipe);
    btnAdmin = (Button) findViewById(R.id.btnAdmin);
    btnWipe = (Button) findViewById(R.id.btnWipe);

        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

        componentName = new ComponentName(DataWipeActivity.this,AdminSupport.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);

        if (active)        {
            btnAdmin.setText("Disbale");
            btnWipe.setVisibility(View.VISIBLE);
        }
        else {
            btnAdmin.setText("Get Admin Support Enable!");
            btnWipe.setVisibility(View.GONE);

        }


        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeAdminFunction();
            }
        });

        btnWipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wipeDataFunction();
            }
        });

    }

    public  void  activeAdminFunction(){
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active){
            devicePolicyManager.removeActiveAdmin(componentName);
            btnAdmin.setText("Disbale");
            btnWipe.setVisibility(View.VISIBLE);
        }
        else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable admin ap!");
            startActivityForResult(intent, RESULT_Enable);

        }
    }

    public void wipeDataFunction(){
        devicePolicyManager.wipeData(4);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_Enable :
                if (requestCode == Activity.RESULT_OK) {
                    btnAdmin.setText("Disbale");
                    btnWipe.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(this,"failed!",Toast.LENGTH_LONG).show();
                }
                return;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
