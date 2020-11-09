package com.traidev.shokanjali;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.share.Share;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.traidev.shokanjali.ui.InCompletePost.DraftPostFragment;
import com.traidev.shokanjali.ui.about.AboutFragment;
import com.traidev.shokanjali.ui.home.HomeFragment;
import com.traidev.shokanjali.ui.pandits.PanditsFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Dialog myDialog;
    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout SingFrame;

    public static boolean onResetFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        FirebaseMessaging.getInstance().subscribeToTopic("ShokanjaliUsers");

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        myDialog = new Dialog(this);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        final SharedPrefManager sm = new SharedPrefManager(this);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        SingFrame = findViewById(R.id.nav_host_fragment);
        setDefaultFragment(new HomeFragment());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(SharedPrefManager.getInstance(getApplication()).getsUser().getMobile() != null)
        {
            navigationView.getMenu().findItem(R.id.nlogout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nlogin).setVisible(false);
        }
        else
        {
            navigationView.getMenu().findItem(R.id.nlogout).setVisible(false);
        }


        navigationView.bringToFront();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nhome:
                        setFragment(new HomeFragment());
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nshare:
                        shareContent();
                        break;
                    case R.id.nlogin:
                        startActivity(new Intent(getApplicationContext(),LoginOTP.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nlogout:
                        LogOutUser();
                        onRestart();
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.npandit:
                        setFragment(new PanditsFragment());
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.addPost:
                        if(sm.getsUser().getName()!=null)
                        startActivity(new Intent(getApplicationContext(),AddNewPost.class));
                        else
                        startActivity(new Intent(getApplicationContext(),LoginOTP.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.draftPost:
                        if(sm.getsUser().getName()!=null)
                            setFragment(new DraftPostFragment());
                        else
                            startActivity(new Intent(getApplicationContext(),LoginOTP.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nAbout:
                        setFragment(new AboutFragment());
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.rPandit:
                        if(sm.getsUser().getName()!=null)
                            startActivity(new Intent(getApplicationContext(),AddNewPandit.class));
                        else
                            startActivity(new Intent(getApplicationContext(),LoginOTP.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nhelp:
                        startActivity(new Intent(getApplicationContext(),ContactNow.class));
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public void LogOutUser() {

        User user = SharedPrefManager.getInstance(this).getsUser();
        String Names = user.getName().substring(0, Math.min(user.getName().length(), 10));

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to Logout "+Names+" ?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPrefManager.getInstance(getApplicationContext()).clear();

                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:
                startActivity(new Intent(this, SearchPosts.class));
                break;
            case R.id.location_menu:
                ShowPopup();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setDefaultFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(SingFrame.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(SingFrame.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void shareContent(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "एक नई सोच और एक नई पहल  \n" +
                        "अपने परिवारजनों एवं चिर परिचितों के स्वर्गवास एवं पुण्य तिथि को ऑनलाइन माध्यम से आसन किफायती तरीके से लोगो तक जल्दी और सही जानकारी पहुँचाने का नवीन माध्यम" +
                        " \nAn online digital cost effective platform to upload,  check and share the tragedy's of your loved ones. \nCheckout शोकांजलि App at: https://play.google.com/store/apps/details?id=com.traidev.shokanjali");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public void ShowPopup() {
        final AutoCompleteTextView keyword;
        Button search;

        myDialog.setContentView(R.layout.pop_up_ncert);
        keyword =  myDialog.findViewById(R.id.keys);
        search =  myDialog.findViewById(R.id.searchNow);

        String csv = getString(R.string.Citys);
        String[] elements = csv.split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listOfString);
        keyword.setAdapter(arrayAdapter);
        keyword.setThreshold(1);
        keyword.addTextChangedListener(new TextWatcher() {
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = keyword.getText().toString();
                if(key.isEmpty())
                {Toast.makeText(getApplicationContext(),"City Name!",Toast.LENGTH_LONG).show();}
                else {
                    myDialog.dismiss();
                    Intent id = new Intent(getApplicationContext(), SearchPosts.class);
                    id.putExtra("msg", keyword.getText().toString());
                    startActivity(id);

                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    public void alertOpen()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(getDrawable(R.drawable.logo))
//set title
                .setTitle("Are you sure to Exit!")
//set message
//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        MainActivity.this.finish();
                    }
                })
//set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(onResetFragment == true){
                onResetFragment = false;
                setFragment(new HomeFragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



}
