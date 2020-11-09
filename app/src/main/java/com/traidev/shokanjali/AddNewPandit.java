package com.traidev.shokanjali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.traidev.shokanjali.location.PlaceAutoSuggestAdapter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewPandit extends AppCompatActivity implements PaymentResultListener {

    private CircleImageView ProfileImage;

    private EditText name,address,about,mobile;
    private Button AddPandit;
    private FrameLayout frame;
    private LinearLayout infoFramg;
    SharedPrefManager sh;

    String idforPay;
    private int ImageCheck = 0;


    private Uri postUri = null;
    private ProgressBar UpdateProgress;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pandit);



        ProfileImage = (CircleImageView) findViewById(R.id.uploadProfile);
        final AutoCompleteTextView autoCompleteTextView=findViewById(R.id.pLocation);

        UpdateProgress = (ProgressBar) findViewById(R.id.updateProg);

         sh = new SharedPrefManager(this);


        name = findViewById(R.id.pName);
        frame = findViewById(R.id.frameErase);
        infoFramg = findViewById(R.id.frameInfo);
        address = findViewById(R.id.pAddress);
        about = findViewById(R.id.pAbout);
        mobile = findViewById(R.id.pMobile);
        AddPandit = findViewById(R.id.addPandit);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(AddNewPandit.this);
            }
        });

        String csv = getString(R.string.Citys);
        String[] elements = csv.split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listOfString);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
            }
        });


        AddPandit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ImageCheck == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please Upload Profile Image!",Toast.LENGTH_SHORT).show();
                    return;
                }

                File file = new File(postUri.getPath());
                // Parsing any Media type file
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


                String dname,addre,ab,dmob,location;

                dname = name.getText().toString();
                location = autoCompleteTextView.getText().toString();
                addre = address.getText().toString();
                ab = about.getText().toString();
                dmob = mobile.getText().toString();

                if(dname.isEmpty() == true){ name.setError("Add Pandit Ji Name");}
                if(addre.isEmpty()== true){ address.setError("Add Address");}
                if(ab.isEmpty()== true){ about.setError("Add About");}
                if(dmob.isEmpty()== true){ mobile.setError("Add Mobile ");}
                if(location.isEmpty()== true){ autoCompleteTextView.setError("Add City Name");}

                else
                {
                    frame.setVisibility(View.VISIBLE);
                    UpdateProgress.setVisibility(View.VISIBLE);
                    SharedPrefManager sh = new SharedPrefManager(getApplication());
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createPandit(fileToUpload, filename,dname,addre,dmob,location,sh.getsUser().getMobile(),ab);

                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse dr = response.body();

                            if(response.code() == 201)
                            {
                                UpdateProgress.setVisibility(View.GONE);
                                frame.setVisibility(View.GONE);
                                idforPay = dr.getMessage();

                                Toast.makeText(AddNewPandit.this,"Pay for Registration!",Toast.LENGTH_LONG).show();
                               /* Intent inte = new Intent(getApplicationContext(), GetwayActivity.class);
                                inte.putExtra("amount", 49);
                                startActivity(inte);*/

                                startPayment(4900);

                            }
                            else
                            {
                                frame.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                            frame.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageCheck = 100;
                postUri = result.getUri();
                ProfileImage.setImageURI(postUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void ShowInfo(View view) {

        infoFramg.setVisibility(View.VISIBLE);

    }

    public void HideInfo(View view) {

        infoFramg.setVisibility(View.GONE);

    }

    public void startPayment(double am) {

        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_live_QuWR92ngFLxEdt");

        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();


            options.put("name", "Pandit g Registration");



            options.put("description", "Reference No. #OID"+Math.random());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

            options.put("amount",am);

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        Intent inte = new Intent(getApplicationContext(), PaymentSuccesfull.class);
        inte.putExtra("status", "OK");
        inte.putExtra("id", idforPay);
        inte.putExtra("type", "pandit");
        startActivity(inte);
        finish();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        Intent inte = new Intent(getApplicationContext(), PaymentSuccesfull.class);
        inte.putExtra("status", "FAILED");
        inte.putExtra("id", idforPay);
        inte.putExtra("type", "pandit");
        startActivity(inte);
        finish();

    }



}
