package com.traidev.shokanjali.ui.regsiterPandit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.traidev.shokanjali.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegPandit extends Fragment {

    private EditText name,shokakul,prathis,mobile,detials;
    private Button AddPost;
    private RadioGroup radioGroup;
    private String dtype;
    private RadioButton radioButton;

    private static final int REQUEST_WRITE_PERMISSION = 200;
    private CircleImageView ProfileImage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);


        return root;
    }



}