package com.traidev.shokanjali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactNow extends AppCompatActivity {

    private EditText name,address,about,mobile,pid;
    private Button SendMessage;
    private ProgressBar UpdateProgress;

    Bundle extras;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_now);

        name = findViewById(R.id.pName);
        pid = findViewById(R.id.pandit);
        address = findViewById(R.id.pAddress);
        about = findViewById(R.id.pMessage);
        mobile = findViewById(R.id.pMobile);
        SendMessage = findViewById(R.id.addPandit);

        UpdateProgress = (ProgressBar) findViewById(R.id.updateProg);

        String mo;
        if (savedInstanceState == null)
        {
            extras = getIntent().getExtras();

            if(extras == null) {
                newString= null;
            }
            else {
                newString= extras.getString("pandit_mobile");
                pid.setVisibility(View.VISIBLE);
                String mask = newString.replaceAll("\\w(?=\\w{4})", "X");
                pid.setText(mask);
            }
        }

        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dname,addre,ab,dmob;
                dname = name.getText().toString();
                addre = address.getText().toString();
                ab = about.getText().toString();
                dmob = mobile.getText().toString();

                if(dname.isEmpty() == true){ name.setError("Add Name");}
                if(addre.isEmpty()== true){ address.setError("Add Address");}
                if(ab.isEmpty()== true){ about.setError("Add Message");}
                if(dmob.isEmpty()== true){ mobile.setError("Add Mobile ");}

                else
                {
                    UpdateProgress.setVisibility(View.VISIBLE);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().sendMessage(dname,addre,dmob,newString,ab);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if(response.code() == 201)
                            {
                                UpdateProgress.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"We will Connect you Soon!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplication(),MainActivity.class);
                                startActivity(intent);
                            }
                            else if(response.code() == 455)
                            {
                                UpdateProgress.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            UpdateProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });




    }
}
