package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText txtName,txtUserid,txtPass,txtemail,txtPhone;
    Button btnRegister, btnloginS;
    String name,userID,password,phone,email;

    DataBaseHelper mydb = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtName = (EditText) findViewById(R.id.txtName);
        txtUserid = (EditText) findViewById(R.id.txtUserID);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtemail = (EditText) findViewById(R.id.txtEmail);
        btnloginS = (Button) findViewById(R.id.btnLoginS);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnloginS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intentLogin);
            }
        });




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtName.getText().toString();
                userID = txtUserid.getText().toString();
                password = txtPass.getText().toString();
                phone = txtPhone.getText().toString();
                email = txtemail.getText().toString();


                Boolean result = mydb.insertData(userID,name,password,phone,email);





                boolean valid = validation(name,userID,password,phone,email);
                if (valid == true){

                    if (result == true) {
                        Intent intentcmd = new Intent(SignUpActivity.this, PhoneNumberVerification.class);
                        intentcmd.putExtra("phone", phone);
                        intentcmd.putExtra("userid",userID);

                        startActivity(intentcmd);

                        Toast.makeText(SignUpActivity.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                      Toast.makeText(SignUpActivity.this, "Error in Database Connection!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(SignUpActivity.this,"fix the errors",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public  boolean validation(String name, String userid, String pass, String Phone, String email){
boolean valid = true;
        if (name.isEmpty() || name.length() < 3) {
            txtName.setError("at least 3 characters");
            valid = false;
        }
        else if(!name.matches("[a-zA-Z ]+"))
        {
            txtName.requestFocus();
            txtName.setError("ENTER ONLY ALPHABETICAL CHARACTER");
        }
        else {
            txtName.setError(null);
        }

        if (userid.isEmpty() || userid.length() < 3) {
            txtUserid.setError("at least 3 characters");
            valid = false;
        } else {
            txtUserid.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtemail.setError("enter a valid email address");
            valid = false;
        } else {
            txtemail.setError(null);

        }
        if (phone.isEmpty() || !isPhoneNumberValid(phone)) {
            txtPhone.setError("enter a valid phone number with your country code");
            valid = false;
        } else {
            txtPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            txtPass.setError("Password should be have more than 4 characters");
            valid = false;
        } else {
            txtPass.setError(null);
        }

        return valid;
    }

    private boolean isPhoneNumberValid(String phone) {
        boolean valid = true;
        String regex = "^(?:0092|\\+92|0)[0-3][0-9]{9}";

        if (!phone.matches(regex)) {
            valid = false;
        }
        return valid;
    }
}
