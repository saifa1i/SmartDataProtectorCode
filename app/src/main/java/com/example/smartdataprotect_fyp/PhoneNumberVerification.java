package com.example.smartdataprotect_fyp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneNumberVerification extends AppCompatActivity {

    EditText verifyNum;
    //int strresult;
    Button verifyBtn;
    String strresult, verificationId,useridcmd;

    DataBaseHelper mydb = new DataBaseHelper(this);

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verification);

        mAuth = FirebaseAuth.getInstance();

        verifyNum = (EditText) findViewById(R.id.verifyNum);
        verifyBtn = (Button) findViewById(R.id.verifyBtn);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");

        sendVerificationCode(phone);

        //strresult = (int) Integer.parseInt(String.valueOf(verifyNum.getText()));

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String phone1 = intent1.getStringExtra("phone");
                String password = intent1.getStringExtra("password");
                String name = intent1.getStringExtra("name");
                String email = intent1.getStringExtra("email");
                */

                strresult = verifyNum.getText().toString();
//                Intent intent = getIntent();

    //

                boolean valid = validation(strresult);
//                Boolean result = mydb.insertData(userID,name,password,phone1,email);

               if (valid == true) {
                    verifyVerificationCode(strresult);
  //                 if (result == true) {
                     }
             //      else {
                 //      Toast.makeText(PhoneNumberVerification.this, "Error in Database Connection!", Toast.LENGTH_SHORT).show();

               //    }

                else{
                        Toast.makeText(PhoneNumberVerification.this, "Fix error", Toast.LENGTH_LONG).show();
                    }

            }



        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String strresult = phoneAuthCredential.getSmsCode();

            if (strresult != null) {
                verifyNum.setText(strresult);
                //verifying the code
                verifyVerificationCode(strresult);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneNumberVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId =s;

        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);


        signInWithPhoneAuthCredential(credential);
    }


    private  void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(PhoneNumberVerification.this, new OnCompleteListener <AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PhoneNumberVerification.this, "Verified", Toast.LENGTH_LONG).show();

                    useridcmd = getIntent().getExtras().getString("userid");
                    Intent i = new Intent(PhoneNumberVerification.this, CommandList.class);
            //        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("userid",useridcmd);
                    startActivity(i);
                } else {

                    //verification unsuccessful.. display an error message

                    String message = "Somthing is wrong, we will fix it soon...";

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered...";
                    }
                }
            }   });

    }


    public  boolean validation( String result ){
        boolean valid = true;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (result.isEmpty() || result.length()<6 ) {
                verifyNum.setError("Enter Correct Verification Code!");
                valid = false;
            } else {
                verifyNum.setError(null);
            }
        }

        return valid;
    }

}
