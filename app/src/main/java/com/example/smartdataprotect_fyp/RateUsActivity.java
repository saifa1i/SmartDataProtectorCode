package com.example.smartdataprotect_fyp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RateUsActivity extends AppCompatActivity {

    Button btnratesubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
    btnratesubmit = (Button) findViewById(R.id.btnratesubmit);

    btnratesubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(RateUsActivity.this,MainActivity.class);
            startActivity(i);
        }
    });
    }
}
