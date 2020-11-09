package com.traidev.shokanjali.ui.pandits;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.shokanjali.BuildConfig;
import com.traidev.shokanjali.ContactNow;
import com.traidev.shokanjali.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PanditAdapter extends RecyclerView.Adapter <PanditAdapter.MyViewHolder>{


    private List<PanditViewModel> pandits;
    private Context context;
    static int CODE_FOR_RESULT=981;


    public PanditAdapter(List<PanditViewModel> pandits, Context context)
    {
        this.pandits = pandits;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pandit_modal,parent,false);
            return new MyViewHolder(view);
    
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.Title.setText(pandits.get(position).getTitle());
        holder.Address.setText(pandits.get(position).getAddress());
        final String number = pandits.get(position).getMobile();
        String mask = number.replaceAll("\\w(?=\\w{4})", "X");
        holder.Mobile.setText(mask);
        LinearLayout layout = holder.linear;

        Glide.with(context).load("http://traidev.com/LIVE_APPS/Shokanjali/pandit/"+pandits.get(position).getThumbnil()).into(holder.Img);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ContactNow.class);
                i.putExtra("pandit_mobile", number);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
            return pandits.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView Img;
        TextView Title,Mobile,Address;
        LinearLayout linear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Img = (CircleImageView) itemView.findViewById(R.id.pandit_profile);
            Title = (TextView) itemView.findViewById(R.id.pandit_name);
            Address = (TextView) itemView.findViewById(R.id.pandit_address);
            Mobile = (TextView) itemView.findViewById(R.id.pandit_mobile);
            linear = (LinearLayout) itemView.findViewById(R.id.PanditclickLinearPost);

        }

    }

    public void OnClickShare(View view ,LinearLayout linear){

        Bitmap bitmap =getBitmapFromView(linear);
        try {
            //File file = new File(this.getExternalCacheDir(),File.separator+ "logicchip.png");
            File file = new File(context.getExternalCacheDir(),"logicchip.png");
            // File file = new File(this.getCacheDir(),File.separator+ "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file.getC));
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");
            //startActivity(Intent.createChooser(intent, "Share image via"));
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Share image via"),CODE_FOR_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }





}
