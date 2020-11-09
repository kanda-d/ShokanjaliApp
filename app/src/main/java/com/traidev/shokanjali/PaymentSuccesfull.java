package com.traidev.shokanjali;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSuccesfull extends AppCompatActivity {

    TextView check; GifImageView gif;
    Button home;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_succesfull);
        progressDialog = new ProgressDialog(this);

        check = findViewById(R.id.text);
        home = findViewById(R.id.btnCheck);
        gif = findViewById(R.id.statusGif);
        gif.stopNestedScroll();

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            progressDialog.setTitle("Payment Status");
            progressDialog.setMessage("Keep Calm! Status is Updating.... ");

            int id = b.getInt("id");
            String type = b.getString("type");
            String status = b.getString("status");

            if(status.equals("OK"))
            {
               String trans = b.getString("trans");
               home.setText("Back to Home");
               check.setText("Your Payemnt has Successful! \n Contact you Soon!");
               gif.setImageResource(R.drawable.ok);
               changeStatus(id,type,"Success");
           }
           else
           {

               if(type.contains("pandit"))
                   home.setText("Register Again!");
               else
                   home.setText("Add Post Again!");

               check.setText("Your Payemnt has Unsuccessful! \n");
               gif.setImageResource(R.drawable.failed);
               changeStatus(id,type,"Failed");
           }
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public void changeStatus(int id, String type, String status)
    {
        progressDialog.show();

        SharedPrefManager sh = new SharedPrefManager(getApplication());

        Call<DefaultResponse> call =  RetrofitClient.getInstance().getApi().orderStatus(id,type,sh.getsUser().getMobile(),status);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if(response.code() == 201)
                Toast.makeText(getApplicationContext(),"We will Notify you Soon!",Toast.LENGTH_LONG).show();

                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void backToHome(View view) {
        if(home.getText().toString().contains("Add Post"))
        {
            startActivity(new Intent(this,
                    AddNewPost.class));

        }
        else
        {
            startActivity(new Intent(this,AddNewPandit.class));
        }
        finish();
    }
}
