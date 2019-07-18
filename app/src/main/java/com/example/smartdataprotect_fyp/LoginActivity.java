package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserId,txtpass;
    TextView tvregister, textViewPassword;
    Button btnlogin;
    String Username,password;

    DataBaseHelper mydb = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserId = (EditText) findViewById(R.id.txtUserID);
        txtpass = (EditText) findViewById(R.id.txtPassword);
        tvregister = (TextView) findViewById(R.id.tvRegister);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        Username = txtUserId.getText().toString();
        password = txtpass.getText().toString();

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentreg = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentreg);
            }
        });

        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfor = new Intent(LoginActivity.this, NewPasswordActivity.class);
                startActivity(intentfor);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = txtUserId.getText().toString().trim();
                password = txtpass.getText().toString().trim();

                boolean valid = validation(Username,password);
                boolean result = mydb.checkUser(Username,password);

                if (valid == true){

                if (result == true){

                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);

                    Toast.makeText(LoginActivity.this,"Successfully LOG IN!",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(LoginActivity.this,"Username or Password is Incorrect",Toast.LENGTH_SHORT).show();
                }

                } else {
                    Toast.makeText(LoginActivity.this,"fix the errors",Toast.LENGTH_LONG).show();
                }



            }
        });

    }

    public  boolean validation( String userid, String pass ){
        boolean valid = true;


        if (userid.isEmpty() || userid.length() < 3) {
            txtUserId.setError("at least 3 characters");
            valid = false;
        } else {
            txtUserId.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            txtpass.setError("Password should be have more than 4 characters");
            valid = false;
        } else {
            txtpass.setError(null);
        }

        return valid;
    }



}
