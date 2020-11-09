package com.traidev.shokanjali.ui.InCompletePost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.shokanjali.AddNewPandit;
import com.traidev.shokanjali.AddNewPost;
import com.traidev.shokanjali.BuildConfig;
import com.traidev.shokanjali.ContactNow;
import com.traidev.shokanjali.PanditsClass.ClassPanditRecylerView;
import com.traidev.shokanjali.R;
import com.traidev.shokanjali.ui.home.HomeViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DraftPostAdapter extends RecyclerView.Adapter<DraftPostAdapter.ViewHolder>{

    private List<HomeViewModel> plist;
    private Context context;

    public DraftPostAdapter(List<HomeViewModel> plist, Context context)
    {
        this.plist = plist;
        this.context = context;
    }

    @NonNull
    @Override
    public DraftPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_modal_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.Title.setText(plist.get(position).getTitle());
        holder.ID.setText(plist.get(position).getID());
        holder.Mtype.setText(plist.get(position).getType());
        holder.Shok.setText(plist.get(position).getShok());
        holder.About.setText(plist.get(position).getAbout());
        holder.Details.setText(plist.get(position).getDetails());
        String Id = plist.get(position).getID();
        Glide.with(context).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+plist.get(position).getThumbnil()).into((holder).Img);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddNewPost.class);
                i.putExtra("post_id", plist.get(position).getID());
                i.putExtra("pic_url", plist.get(position).getThumbnil());
                i.putExtra("title", plist.get(position).getTitle());
                i.putExtra("type", plist.get(position).getType());
                i.putExtra("details", plist.get(position).getDetails());
                i.putExtra("shok", plist.get(position).getShok());
                i.putExtra("about", plist.get(position).getAbout());
                i.putExtra("city", plist.get(position).getAddress());
                i.putExtra("mobile", plist.get(position).getMobile());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
            return plist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img, fb,what,insta,share;
        TextView ID,Title,Mtype,Details,Shok,About;
        Button linear;

       public ViewHolder(@NonNull View itemView) {
            super(itemView);

             Img = (ImageView) itemView.findViewById(R.id.pProfile);
             fb = (ImageView) itemView.findViewById(R.id.fb);
             insta = (ImageView) itemView.findViewById(R.id.insta);
             what = (ImageView) itemView.findViewById(R.id.wh);
             share = (ImageView) itemView.findViewById(R.id.share);
             ID = (TextView) itemView.findViewById(R.id.post_id);
             Title = (TextView) itemView.findViewById(R.id.ptitle);
             Mtype = (TextView) itemView.findViewById(R.id.mText);
             Details = (TextView) itemView.findViewById(R.id.pdetails);
             Shok = (TextView) itemView.findViewById(R.id.pshok);
             About = (TextView) itemView.findViewById(R.id.pAbout);
             linear =  itemView.findViewById(R.id.btnEditToMain);

        }

    }




}
