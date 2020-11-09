package com.traidev.shokanjali.PanditsClass;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.traidev.shokanjali.R;

import java.util.List;

public class ClassPanditRecylerView extends RecyclerView.Adapter<ClassPanditRecylerView.ViewHolder> {

    private List<ClassPanditsModal> classStudentsRecylerViewList;
    private int LayoutChange;

    public ClassPanditRecylerView(List<ClassPanditsModal> classStudentsRecylerViewList) {
        this.classStudentsRecylerViewList = classStudentsRecylerViewList;
    }

    @NonNull
    @Override
    public ClassPanditRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pandit_modal,viewGroup,false);
                return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String panditName = classStudentsRecylerViewList.get(position).getPanditName();
        String panditProfile = classStudentsRecylerViewList.get(position).getPanditProfile();
        String panditDetails = classStudentsRecylerViewList.get(position).getPanditDetails();
        holder.setPName(panditName);
        holder.setPprofile(panditProfile);
        holder.setPdetails(panditDetails);
    }

    @Override
    public int getItemCount() {
         return classStudentsRecylerViewList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView PersonName,Detials,Profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PersonName = itemView.findViewById(R.id.pandit_name);
            Profile = itemView.findViewById(R.id.pandit_profile);
            Detials = itemView.findViewById(R.id.pdetails);
        }


        public void setPName(String personName) {
            PersonName.setText(personName);
        }
        public void setPprofile(String pprofile) {
            Profile.setText(pprofile);
        }
        public void setPdetails(String pdetails) {
            Detials.setText(pdetails);
        }

    }
}
