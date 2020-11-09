package com.traidev.shokanjali;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.traidev.shokanjali.location.PlaceAutoSuggestAdapter;

import org.json.JSONObject;

import java.io.File;
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

public class AddNewPost extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, PaymentResultListener {

    private CircleImageView ProfileImage;

    private EditText name,shokakul,prathis,mobile,detials,other;
    private Button AddPost;
    private int ImageCheck,AddhaCheck = 0;
    private RadioGroup radioGroup;
    private String dtype,lang = null;
    private RadioButton radioButton;
    private Bundle ex;
    private RelativeLayout rel;
    private static final int IMG_REQUEST = 777;
    AutoCompleteTextView autoCompleteTextView;
    public TextView t1,t2,t3,d1,d2,d3,sh1,sh2,sh3,n2,n3,p2,p3;
    public Button btS,btE,btP,ShowHide;
    private Switch s1,s2;
    private  Boolean ConditionChange = false;
    private Uri postUri = null;
    private Uri aadharUri = null;
    ProgressDialog progressDialog;

    String pCheck = null;
    int idforPay;

    public ImageView im1,im2,im3,UploadAadhar;
    String dname,dpra,dshoka,dmob,ddetails,location,OtherText,UpdateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        ProfileImage = findViewById(R.id.Dprofile);
        s1 = findViewById(R.id.switch1);
        s2 = findViewById(R.id.switch2);
        UploadAadhar = findViewById(R.id.Dadhar);
        autoCompleteTextView=findViewById(R.id.autocomplete);

        name = findViewById(R.id.dName);
        other = findViewById(R.id.other);
        prathis = findViewById(R.id.dPratishthan);
        shokakul = findViewById(R.id.dShoka);
        mobile = findViewById(R.id.dMobiles);
        detials = findViewById(R.id.dDetails);
        AddPost = findViewById(R.id.addPost);
        rel = findViewById(R.id.FormDispaly);

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(s1.isChecked())
                {
                    s2.setChecked(false);
                    lang = "Hindi";
                }
            }
        });
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(s2.isChecked())
                {
                    s1.setChecked(false);
                    lang = "English";
                }
            }
        });

        ((RadioGroup)findViewById(R.id.fancy_radio_group)).setOnCheckedChangeListener(AddNewPost.this);

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Keep Calm! Post is Uploading.... ");

        btS = findViewById(R.id.btnStandard);
        ShowHide = findViewById(R.id.btnShowHide);
        btE = findViewById(R.id.btnExtended);
        btP = findViewById(R.id.btnPremium);

        t1 = findViewById(R.id.type1);
        t2 = findViewById(R.id.type2);
        t3 = findViewById(R.id.type3);

        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im3 = findViewById(R.id.im3);

        d1 = findViewById(R.id.detials1);
        d2 = findViewById(R.id.detials2);
        d3 = findViewById(R.id.detials3);

        p2 = findViewById(R.id.pra2);
        p3 = findViewById(R.id.pra3);


        sh1 = findViewById(R.id.shoka1);
        sh2 = findViewById(R.id.shoka2);
        sh3 = findViewById(R.id.shoka3);

        n2 = findViewById(R.id.name2);
        n3 = findViewById(R.id.name3);


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(AddNewPost.this);

                pCheck="Profile";
            }
        });

        UploadAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(AddNewPost.this);
                pCheck="Aadhar";

            }
        });

        if (savedInstanceState == null)
        {
            ex = getIntent().getExtras();

            if(ex == null) {
            }
            else {
                ConditionChange = true;
                String names,types,detailss,shoks,pratis,citys,mobiles,profile;
                UpdateId= ex.getString("post_id");
                profile = ex.getString("pic_url");
                names = ex.getString("title");
                types = ex.getString("type");
                detailss = ex.getString("details");
                shoks = ex.getString("shok");
                pratis = ex.getString("about");
                citys = ex.getString("city");
                mobiles = ex.getString("mobile");

                name.setText(names);
                detials.setText(detailss);
                mobile.setText(mobiles);
                shokakul.setText(shoks);
                prathis.setText(pratis);
                autoCompleteTextView.setText(citys);
                Glide.with(getApplicationContext()).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+profile).into((ProfileImage));
                Glide.with(getApplicationContext()).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+profile).into((im1));
                Glide.with(getApplicationContext()).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+profile).into((im2));
                Glide.with(getApplicationContext()).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+profile).into((im3));
            }

        }

        ShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel.setVisibility(View.INVISIBLE);
            }
        });

        btS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(1,9900);

            }
        });

        btE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(2,14900);
            }
        });

        btP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 uploadFile(1,19900);

            }
        });

        if(SharedPrefManager.getInstance(getApplication()).getsUser().getMobile() == null)
        {
            AddPost.setVisibility(View.INVISIBLE);
        }

        AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageCheck != 100)
                {
                    Toast.makeText(getApplicationContext(),"Please Upload Profile Image!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(AddhaCheck != 100)
                {
                    Toast.makeText(getApplicationContext(),"Please Upload Aadhar Card!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dtype == null)
                {
                    Toast.makeText(getApplicationContext(),"Please Choose Type",Toast.LENGTH_SHORT).show();
                    return;
                }

                dname = name.getText().toString();
                location = autoCompleteTextView.getText().toString();
                dpra = prathis.getText().toString();
                dshoka = shokakul.getText().toString();
                ddetails = detials.getText().toString();
                dmob = mobile.getText().toString();
                OtherText = other.getText().toString();

                if(dname.isEmpty() == true){ name.setError("Add Person Name");}
                if(location.isEmpty() == true){ autoCompleteTextView.setError("Add City Name");}
                if(dpra.isEmpty()== true){ prathis.setError("Add Pratisthan");}
                if(ddetails.isEmpty()== true){ detials.setError("Add Person Detials ");}
                if(dshoka.isEmpty()== true){ shokakul.setError("Add Shokakul Details");}
                else if(dtype.equals("Others"))
                {
                    dtype = OtherText;
                }

                else
                {
                    rel.setVisibility(View.VISIBLE);
                    t1.setText(dtype);
                    t2.setText(dtype);
                    t3.setText(dtype);

                    p2.setText("फर्म : " + dpra + "Mobile : "+ dmob);
                    p3.setText("फर्म : " + dpra + "Mobile : "+ dmob);

                    sh1.setText("शोकाकुल : -" + dshoka);
                    sh2.setText("शोकाकुल : -" + dshoka);
                    sh3.setText("शोकाकुल : -" + dshoka);

                    n2.setText(dname);
                    n3.setText(dname);

                    d1.setText(ddetails);
                    d2.setText(ddetails);
                    d3.setText(ddetails);

                    im1.setImageURI(postUri);
                    im2.setImageURI(postUri);
                    im3.setImageURI(postUri);
                }

            }
        });
    }

    private void uploadFile(int view, final int price) {
        progressDialog.show();
        // Parsing any Media type file

        if(ConditionChange == true)
           {
            if(ImageCheck == 100)
            {
                File file = new File(postUri.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updatePostProfile(fileToUpload, filename,UpdateId,dname,dtype,ddetails,
                        dshoka,dpra,location,dmob,view);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            if (response.code() == 201 ) {
                                idforPay = Integer.parseInt(serverResponse.getMessage());
                                Toast.makeText(getApplicationContext(), "Pay only for Registration!",Toast.LENGTH_SHORT).show();
                                startPayment(price);
                            } else {
                            }
                        } else {
                            assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
                        }
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });

            }

            else
            {
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updatePost(UpdateId,dname,dtype,ddetails,
                    dshoka,dpra,location,dmob,view);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            if (response.code() == 201 ) {
                                idforPay = Integer.parseInt(serverResponse.getMessage());
                                Toast.makeText(getApplicationContext(), "Pay only for Registration!",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            assert serverResponse != null;
//                    Log.v("Response", serverResponse.toString());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    }
                });
            }
        }
        else
        {
            if(postUri != null && aadharUri != null)
            {
                File file = new File(postUri.getPath());
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                File Adharfile = new File(aadharUri.getPath());
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), Adharfile);
                MultipartBody.Part addhartoUpload = MultipartBody.Part.createFormData("aadhar", Adharfile.getName(), requestBody2);
                RequestBody addharName = RequestBody.create(MediaType.parse("text/plain"), Adharfile.getName());

                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().addFunral(fileToUpload, filename,addhartoUpload,addharName,dname,dtype,ddetails,
                        dshoka,dpra,location,dmob,lang,view,SharedPrefManager.getInstance(getApplication()).getsUser().getMobile());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            if (response.code() == 201 ) {
                                idforPay = Integer.parseInt(serverResponse.getMessage());
                                Toast.makeText(getApplicationContext(), "Pay only for Registration!",Toast.LENGTH_SHORT).show();
                                startPayment(price);

                            } else {
                                Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),"Please Upload Profile & Aadhar Card !",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                if(pCheck == "Profile")
                {
                    ImageCheck = 100;
                    postUri = result.getUri();
                    ProfileImage.setImageURI(postUri);
                }
               else
                {
                    AddhaCheck = 100;
                    aadharUri = result.getUri();
                    UploadAadhar.setImageURI(aadharUri);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

            radioGroup = (RadioGroup) findViewById(R.id.fancy_radio_group);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);
            if(radioButton.getText().equals("Others"))
            {
                other.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Add Other Type!",Toast.LENGTH_LONG).show();
            }
            else
            {other.setVisibility(View.GONE);}
            dtype= radioButton.getText().toString();
    }

    public void startPayment(double am) {

        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_live_QuWR92ngFLxEdt");

        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Registration");

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
        inte.putExtra("type", "post");
        startActivity(inte);
        finish();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
        Intent inte = new Intent(getApplicationContext(), PaymentSuccesfull.class);
        inte.putExtra("status", "FAILED");
        inte.putExtra("id", idforPay);
        inte.putExtra("type", "post");
        startActivity(inte);
        finish();

    }





}
