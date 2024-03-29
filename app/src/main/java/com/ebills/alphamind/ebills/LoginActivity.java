package com.ebills.alphamind.ebills;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ebills.alphamind.ebills.Storage.OTPToken.Otptoken;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText editText;
    EditText password;
    Button submitButton;
    Button newAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // initViews
        initViews();

//
//            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("1234567899")
//                    .setContentText("use above number for demo purpose ")
//                    .setConfirmText("Ok")
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog.dismissWithAnimation();
//                        }
//                    })
//                    .show();
        // Add Listener to button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pn = editText.getText().toString();
                String pass = password.getText().toString();

                if (pn.equals("") || pass.equals("")) {
                    // Empty Warning
                }

                // Demo Purposes
                else if (pn.equals("1234567899") && pass.equals("1234")) {

                    Otptoken otptoken = new Otptoken(LoginActivity.this);
                    try {
                        otptoken.saveOTP("1234");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                // Server
                else {

                    //Send it to the server
                    // Response ok

                    boolean ok = false;

                    if (ok) {
                        // Store Token into the SharedPref File

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                }

            }
        });
    }

    public void initViews() {
        editText = (EditText) findViewById(R.id.PhoneNumber);
        submitButton = (Button) findViewById(R.id.SubmitPhoneNumber);
        newAccount = findViewById(R.id.NewAccount);
        password = findViewById(R.id.Password_Login);
        String text = editText.getText().toString();
        Log.e(TAG, "initViews: " + text);
        editText.setText("");
    }
}
