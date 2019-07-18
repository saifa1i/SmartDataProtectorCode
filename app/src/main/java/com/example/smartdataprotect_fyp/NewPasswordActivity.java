package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewPasswordActivity extends AppCompatActivity {

    EditText txtuserN,txtpassN;
    String userN,passN;
    Button btnnewPass;
    DataBaseHelper mydb = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        txtuserN = (EditText) findViewById(R.id.txtUsernameV);
        txtpassN = (EditText) findViewById(R.id.txtNewPassword);
        btnnewPass = (Button) findViewById(R.id.btnNewPass);




    btnnewPass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            userN = txtuserN.getText().toString();
            passN = txtpassN.getText().toString();

            boolean valid = validation(userN,passN);

            boolean update = mydb.updatePass(userN,passN);

            if (valid == true){
                if (update == true) {
                    Intent i = new Intent(NewPasswordActivity.this, LoginActivity.class);
                    startActivity(i);
                    Toast.makeText(NewPasswordActivity.this, "Password Changed!", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(NewPasswordActivity.this, "UserName Didn't match", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(NewPasswordActivity.this,"fix the errors",Toast.LENGTH_LONG).show();
            }
        }
    });

    }

    public  boolean validation( String useridN, String passNew ){
        boolean valid = true;


        if (useridN.isEmpty() || useridN.length() < 3) {
            txtuserN.setError("Incorrect or dosen't Match!");
            valid = false;
        } else {
            txtuserN.setError(null);
        }

        if (passNew.isEmpty() || passNew.length() < 4 ) {
            txtpassN.setError("Password should be have more than 4 characters");
            valid = false;
        } else {
            txtpassN.setError(null);
        }

        return valid;
    }
}
