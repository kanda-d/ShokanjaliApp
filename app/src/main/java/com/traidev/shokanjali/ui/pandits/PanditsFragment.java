package com.traidev.shokanjali.ui.pandits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.traidev.shokanjali.MainApiUrl;
import com.traidev.shokanjali.Main_Interface;
import com.traidev.shokanjali.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.shokanjali.MainActivity.onResetFragment;

public class PanditsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PanditViewModel> pandits;
    private PanditAdapter adapter;
    private Main_Interface main_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        onResetFragment = true;

        View root = inflater.inflate(R.layout.fragment_pandits, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.panditRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        fetchPost();

        return root;
    }


    public void fetchPost()
    {

        main_interface = MainApiUrl.getApiClient().create(Main_Interface.class);

        Call<List<PanditViewModel>> call = main_interface.getPandit();

        call.enqueue(new Callback<List<PanditViewModel>>() {
            @Override
            public void onResponse(Call<List<PanditViewModel>> call, Response<List<PanditViewModel>> response) {

                pandits = response.body();
                adapter = new PanditAdapter(pandits,getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<PanditViewModel>> call, Throwable t) {

            }
        });
    }


}