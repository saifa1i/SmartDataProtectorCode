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

public class UnlockActivity extends AppCompatActivity {

    Button btnUnlock,btnAdminU;
    static final int RESULT_Enable = -1;
    public static final int USES_POLICY_WIPE_DATA = 4;

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        btnAdminU = (Button) findViewById(R.id.btnAdminU);

        btnUnlock = (Button) findViewById(R.id.btnUnlock);
        btnAdminU.setVisibility(View.GONE);

        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

        componentName = new ComponentName(UnlockActivity.this,AdminSupport.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);

        if (active)        {
            btnAdminU.setText("Disbale");
            btnUnlock.setVisibility(View.VISIBLE);
        }
        else {
            btnAdminU.setText("Get Admin Support Enable!");
            btnUnlock.setVisibility(View.GONE);

        }
        btnAdminU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeAdminFunction();
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockFunction();
            }
        });


    }

    public  void  activeAdminFunction(){
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active){
            devicePolicyManager.removeActiveAdmin(componentName);
            btnAdminU.setText("Disbale");
            btnUnlock.setVisibility(View.VISIBLE);
        }
        else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable admin ap!");
            startActivityForResult(intent, RESULT_Enable);

        }
    }

    public void unlockFunction(){
        Context context = null;
     devicePolicyManager.lockNow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_Enable :
                if (requestCode == Activity.RESULT_OK) {
                    btnAdminU.setText("Disbale");
                    btnUnlock.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(this,"failed!",Toast.LENGTH_LONG).show();
                }
                return;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
