package com.traidev.shokanjali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginOTP extends AppCompatActivity implements View.OnClickListener{

    private Button next;
    private ProgressBar Lprog;
    private TextView topText,textU,resendButton,LoginToggle;
    private EditText  luser,lpass;
    Random rand = new Random();
    final String otp = String.format("%04d", rand.nextInt(10000));

    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if(sharedPrefManager.getsUser().getName() != null)
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login_otp);


        Lprog = findViewById(R.id.updateProgLogin);
        topText = findViewById(R.id.topTextL);
        luser = findViewById(R.id.lUsername);
        lpass = findViewById(R.id.lPassword);
        LoginToggle = findViewById(R.id.loginChange);
        next = findViewById(R.id.button);

        next.setOnClickListener(this);
        LoginToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateOTP.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        final String uname = luser.getText().toString();
        String pass = lpass.getText().toString();

        switch (v.getId()) {
            case R.id.button:
                if (next.getText().equals("Send Otp") && !next.getText().equals("Login")) {
                if (!TextUtils.isEmpty(uname)) {
                    Lprog.setVisibility(View.VISIBLE);
                    next.setText("Login");
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().loginUser(uname, otp);
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if (response.code() == 201) {
                                lpass.setVisibility(View.VISIBLE);
                                Lprog.setVisibility(View.GONE);
                                luser.setEnabled(false);
                                topText.setText("Enter Otp we Just Send you!");
                                Toast.makeText(LoginOTP.this, "Enter Otp Just Send you!", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 422) {
                                Lprog.setVisibility(View.GONE);
                                Toast.makeText(LoginOTP.this, "Mobile number is not Exist!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else {
                    Toast.makeText(LoginOTP.this, "Please Enter Mobile Number!", Toast.LENGTH_SHORT).show();
                }
             }
               else if (next.getText().equals("Login") && !next.getText().equals("Send Otp")) {
                    if (!pass.isEmpty()) {
                        Lprog.setVisibility(View.VISIBLE);

                        String playerID = FirebaseInstanceId.getInstance().getToken();

                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().loginVerify(uname, pass,playerID);
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse dr = response.body();
                                if (response.code() == 201) {
                                    Lprog.setVisibility(View.GONE);
                                    SharedPrefManager.getInstance(getApplicationContext()).saveUser(dr.getMessage(), uname,"");
                                    Toast.makeText(LoginOTP.this, "Welcome Back " + dr.getMessage(), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else if (response.code() == 422) {
                                    Toast.makeText(LoginOTP.this, "Wrong Otp !", Toast.LENGTH_LONG).show();
                                    Lprog.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        Toast.makeText(LoginOTP.this, "Please Enter 4 Digit OTP !", Toast.LENGTH_SHORT).show();
                    }

                }
        }

    }

}
