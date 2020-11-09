package com.traidev.shokanjali.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.traidev.shokanjali.AddNewPost;
import com.traidev.shokanjali.MainActivity;
import com.traidev.shokanjali.MainApiUrl;
import com.traidev.shokanjali.Main_Interface;
import com.traidev.shokanjali.R;
import com.traidev.shokanjali.SearchPosts;
import com.traidev.shokanjali.SharedPrefManager;
import com.traidev.shokanjali.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<HomeViewModel> posts;
    ScrollView scr;
    private Dialog myDialog;
    private FrameLayout infoFramg;

    private TextView nation,city,Close;
    private View line1,line2;
    private Button add;
    private PostAdapter adapter;
    private Main_Interface main_interface;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        final String cityData =  sharedPrefManager.getsUser().getCity();


        myDialog = new Dialog(getContext());

        View root = inflater.inflate(R.layout.fragment_home, container, false);
/*
       infoFramg = root.findViewById(R.id.frameHome);
        Close = root.findViewById(R.id.closeHome);
        add = root.findViewById(R.id.addPostHome);
*/


/*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddNewPost.class));
            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoFramg.setVisibility(View.GONE);
            }
        });
*/


        shimmerFrameLayout = root.findViewById(R.id.loadingShimmer);
        scr = root.findViewById(R.id.sccr);
        nation = root.findViewById(R.id.nation);
        city = root.findViewById(R.id.city);
        line1 = root.findViewById(R.id.l1);
        line2= root.findViewById(R.id.l2);
        recyclerView = (RecyclerView) root.findViewById(R.id.all_postRec);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        scr.setNestedScrollingEnabled(false);

        fetchPost("nation");

        nation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line1.setBackgroundResource(R.color.colorPrimary);
                line2.setBackgroundResource(R.color.browser_actions_divider_color);

                nation.setTextColor(Color.parseColor("#555555"));
                city.setTextColor(Color.parseColor("#979797"));
                fetchPost("nation");
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                line2.setBackgroundResource(R.color.colorPrimary);
                line1.setBackgroundResource(R.color.browser_actions_divider_color);
                city.setTextColor(Color.parseColor("#555555"));
                nation.setTextColor(Color.parseColor("#979797"));
                   ShowPopup();
            }
        });

        return root;
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

        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, listOfString);
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
                {
                    Toast.makeText(getContext(),"City Name!",Toast.LENGTH_LONG).show();}
                else {
                    myDialog.dismiss();
                   fetchPost(keyword.getText().toString());
                    SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
                    String name =  sharedPrefManager.getsUser().getName();
                    String mobile =  sharedPrefManager.getsUser().getMobile();
                    SharedPrefManager.getInstance(getContext()).saveUser(name,mobile,keyword.getText().toString());

                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void fetchPost(String choose)
    {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

        main_interface = MainApiUrl.getApiClient().create(Main_Interface.class);

        Call<List<HomeViewModel>> call = main_interface.getPost(choose);

        call.enqueue(new Callback<List<HomeViewModel>>() {
            @Override
            public void onResponse(Call<List<HomeViewModel>> call, Response<List<HomeViewModel>> response) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                posts = response.body();
                adapter = new PostAdapter(posts,getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<HomeViewModel>> call, Throwable t) {

            }
        });
    }


}