package com.traidev.shokanjali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOTP extends AppCompatActivity  implements View.OnClickListener {

    private PinView pinView;
    private Button next;
    private ProgressBar Lprog;
    private TextView topText,textU,resendButton,LoginToggle;
    private EditText userName, userPhone;
    private ConstraintLayout first, second;
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
        setContentView(R.layout.activity_create_otp);

        Lprog = findViewById(R.id.updateProgLogin);
        topText = findViewById(R.id.topTextC);
        LoginToggle = findViewById(R.id.loginChange);
        pinView = findViewById(R.id.pinView);
        next = findViewById(R.id.button);
        userName = findViewById(R.id.username);
        userPhone = findViewById(R.id.userPhone);
        resendButton = findViewById(R.id.resendbtn);
        first = findViewById(R.id.first_step);
        second = findViewById(R.id.secondStep);
        textU = findViewById(R.id.textView_noti);
        first.setVisibility(View.VISIBLE);

        next.setOnClickListener(this);
        LoginToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginOTP.class));
            }
        });
        resendButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final String phone = userPhone.getText().toString();
        final String name = userName.getText().toString();

        switch (v.getId())
        {
            case R.id.button:
                if (next.getText().equals("Let's go!")) {
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                        Lprog.setVisibility(View.VISIBLE);
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUser(name,phone,otp);

                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse dr = response.body();

                                if(response.code() == 201)
                                {
                                    Lprog.setVisibility(View.GONE);
                                    next.setText("Verify");
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Toast.makeText(CreateOTP.this,"Check your SMS!",Toast.LENGTH_LONG).show();
                                    first.setVisibility(View.GONE);
                                    second.setVisibility(View.VISIBLE);
                                    topText.setText("Please Enter Otp.\nfor verify your self!.");
                                }
                                else if(response.code() == 422)
                                {
                                    Lprog.setVisibility(View.GONE);
                                    Toast.makeText(CreateOTP.this,"Mobile Number already Exist!",Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                                Lprog.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        Toast.makeText(CreateOTP.this, "Please Enter Name & Mobile Number!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (next.getText().equals("Verify")) {
                    String OTP = pinView.getText().toString();
                    Lprog.setVisibility(View.VISIBLE);

                    String playerID = FirebaseInstanceId.getInstance().getToken();

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().verifyUser(phone,otp,playerID);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if(response.code() == 201)
                            {
                                Lprog.setVisibility(View.GONE);
                                pinView.setLineColor(Color.GREEN);
                                textU.setText("OTP Verified");
                                textU.setTextColor(Color.BLUE);
                                next.setText("Next");
                                Toast.makeText(CreateOTP.this,"You Are Registered !",Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(getApplicationContext()).saveUser(name,phone,"");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else if(response.code() == 421)
                            {
                                Lprog.setVisibility(View.GONE);Toast.makeText(CreateOTP.this,"Invalid Otp !",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Lprog.setVisibility(View.GONE);
                                pinView.setLineColor(Color.RED);
                                textU.setText("X Incorrect OTP");
                                textU.setTextColor(Color.RED);
                                Toast.makeText(CreateOTP.this,"Invalid Otp !",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if (next.getText().equals("Next")) {
                    Lprog.setVisibility(View.GONE);
                    startActivity(new Intent(this, MainActivity.class));
                }



        }

    }



}
