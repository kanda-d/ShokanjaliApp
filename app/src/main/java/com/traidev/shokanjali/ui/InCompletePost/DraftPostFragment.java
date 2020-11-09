package com.traidev.shokanjali.ui.InCompletePost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.shokanjali.MainApiUrl;
import com.traidev.shokanjali.Main_Interface;
import com.traidev.shokanjali.R;
import com.traidev.shokanjali.SharedPrefManager;
import com.traidev.shokanjali.ui.home.HomeViewModel;
import com.traidev.shokanjali.ui.home.PostAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.shokanjali.MainActivity.onResetFragment;

public class DraftPostFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<HomeViewModel> posts;
    private DraftPostAdapter adapter;
    private Main_Interface main_interface;
    private ImageView Notfound;
    private ProgressBar UpdateProgress;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_draft_posts, container, false);

        onResetFragment = true;

        UpdateProgress = root.findViewById(R.id.updateProg);
        Notfound = root.findViewById(R.id.notFound);

        recyclerView = root.findViewById(R.id.all_darft_postRec);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchPost();

        return root;
    }


    public void fetchPost()
    {
        UpdateProgress.setVisibility(View.VISIBLE);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());

        String id = sharedPrefManager.getsUser().getMobile();
        main_interface = MainApiUrl.getApiClient().create(Main_Interface.class);

        Call<List<HomeViewModel>> call = main_interface.getDraftPost(id);

        call.enqueue(new Callback<List<HomeViewModel>>() {
            @Override
            public void onResponse(Call<List<HomeViewModel>> call, Response<List<HomeViewModel>> response) {
              if(response.code() != 404)
              {
                     posts = response.body();
                     Notfound.setVisibility(View.GONE);
                     adapter = new DraftPostAdapter(posts,getActivity());
                     recyclerView.setAdapter(adapter);
              }
              else
              {  Notfound.setVisibility(View.VISIBLE);}
                 UpdateProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<HomeViewModel>> call, Throwable t) {
                UpdateProgress.setVisibility(View.GONE);

            }
        });
    }


}