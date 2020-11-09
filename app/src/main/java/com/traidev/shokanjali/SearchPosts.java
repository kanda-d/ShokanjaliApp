package com.traidev.shokanjali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.traidev.shokanjali.ui.home.HomeViewModel;
import com.traidev.shokanjali.ui.home.PostAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPosts extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<HomeViewModel> posts;
    private PostAdapter adapter;
    private Main_Interface main_interface;
    private ImageView mike,back,notFound;
    private EditText SearchKey;
    private ShimmerFrameLayout shimmerFrameLayout;
    Bundle extras;
    private TextView data;
    String searchString;
    ScrollView searchResutl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);


        shimmerFrameLayout = findViewById(R.id.SearchloadingShimmer);
        SearchKey = findViewById(R.id.searchKey);
        notFound = findViewById(R.id.notFoundSearch);
        data = findViewById(R.id.searchfor);
        searchResutl = findViewById(R.id.ResultSearch);

        recyclerView = (RecyclerView) findViewById(R.id.all_postRec);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        if(savedInstanceState == null)
        {
            extras = getIntent().getExtras();
            if(extras == null) {
                searchString= null;
            }
            else {
                searchString= extras.getString("msg");
                SearchPost(searchString);
                data.setText("You Searched for " +searchString);
                SearchKey.setText(searchString);
            }
        }

        SearchKey.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SearchPost(SearchKey.getText().toString());
                    data.setText("You Search for : " +SearchKey.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    public void SearchPost(String keyword)
    {
        shimmerFrameLayout.startShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        main_interface = MainApiUrl.getApiClient().create(Main_Interface.class);
        Call<List<HomeViewModel>> call = main_interface.getSearches(keyword);

        call.enqueue(new Callback<List<HomeViewModel>>() {
            @Override
            public void onResponse(Call<List<HomeViewModel>> call, Response<List<HomeViewModel>> response) {
                if(String.valueOf(response.code()).contains("201"))
                {
                    searchResutl.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    posts = response.body();
                    adapter = new PostAdapter(posts,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    notFound.setVisibility(View.GONE);
                }
                else
                {
                    searchResutl.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(Call<List<HomeViewModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),String.valueOf(t.getMessage()),Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
