package com.traidev.shokanjali.ui.home;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.traidev.shokanjali.BuildConfig;
import com.traidev.shokanjali.R;
import com.traidev.shokanjali.SplashActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter {


    private List<HomeViewModel> plist;
    private Context context;
    private Context contextd;
    static int CODE_FOR_RESULT=981;

    public PostAdapter(List<HomeViewModel> plist, Context context)
    {
        this.plist = plist;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view;
        contextd = parent.getContext();

          if(viewType == HomeViewModel.TYPE_1)
          {
              view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_post1,parent,false);
              return new Modal1st(view);
          }
         else if(viewType == HomeViewModel.TYPE_2)
          {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_post2,parent,false);
                return new Modal2nd(view);
          }
          else if(viewType == HomeViewModel.TYPE_3)
          {
              view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modal_post3,parent,false);
              return new Modal3rd(view);
          }

          return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HomeViewModel object = plist.get(position);

        switch (object.getModel())
        {
            case HomeViewModel.TYPE_1:
                ((Modal1st) holder).Title.setText(plist.get(position).getTitle());
                ((Modal1st) holder).Mtype.setText(plist.get(position).getType());
                ((Modal1st) holder).Shok.setText(plist.get(position).getShok());
                ((Modal1st) holder).About.setText(plist.get(position).getAbout());
                ((Modal1st) holder).Details.setText(plist.get(position).getDetails());

                final LinearLayout layout =  ((Modal1st) holder).linear;

                Glide.with(context).load("https://traidev.com/LIVE_APPS/Shokanjali/user/"+plist.get(position).getThumbnil()).into(((Modal1st) holder).Img);

                ((Modal1st) holder).fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout,"com.facebook.katana");
                    }
                });

                ((Modal1st) holder).what.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout,"com.whatsapp");
                    }
                });

                ((Modal1st) holder).insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout,"com.instagram.android");
                    }
                });

                ((Modal1st) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout,"all");
                    }
                });
                break;

            case HomeViewModel.TYPE_2:
                ((Modal2nd) holder).Title.setText(plist.get(position).getTitle());
                ((Modal2nd) holder).Mtype.setText(plist.get(position).getType());
                ((Modal2nd) holder).Shok.setText(plist.get(position).getShok());
                ((Modal2nd) holder).About.setText(plist.get(position).getAbout());
                ((Modal2nd) holder).Details.setText(plist.get(position).getDetails());
                Glide.with(context).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+plist.get(position).getThumbnil()).into(((Modal2nd) holder).Img);
                final LinearLayout layout2 =  ((Modal2nd) holder).linear;


                ((Modal2nd) holder).fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout2,"com.facebook.katana");
                    }
                });

                ((Modal2nd) holder).what.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout2,"com.whatsapp");
                    }
                });

                ((Modal2nd) holder).insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout2,"com.instagram.android");
                    }
                });

                ((Modal2nd) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout2,"all");
                    }
                });


                break;
            case HomeViewModel.TYPE_3:
                ((Modal3rd) holder).Mtype.setText(plist.get(position).getType());
                ((Modal3rd) holder).Shok.setText(plist.get(position).getShok());
                ((Modal3rd) holder).Details.setText(plist.get(position).getDetails());
                Glide.with(context).load("http://traidev.com/LIVE_APPS/Shokanjali/user/"+plist.get(position).getThumbnil()).into(((Modal3rd) holder).Img);

                final LinearLayout layout3 =  ((Modal3rd) holder).linear;


                ((Modal3rd) holder).fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout3,"com.facebook.katana");
                    }
                });

                ((Modal3rd) holder).what.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout3,"com.whatsapp");
                    }
                });

                ((Modal3rd) holder).insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout3,"com.instagram.android");
                    }
                });

                ((Modal3rd) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickShare(v, layout3,"all");
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
         if(!plist.isEmpty())
         {
             return plist.size();
         }
         else
         {
             return 0;
         }
    }

    public static class Modal1st extends RecyclerView.ViewHolder {

        ImageView Img, fb,what,insta,share;
        TextView Title,Mtype,Details,Shok,About;
        LinearLayout linear,promo;

         Modal1st(@NonNull View itemView) {
            super(itemView);

             Img = (ImageView) itemView.findViewById(R.id.pProfile);
             fb = (ImageView) itemView.findViewById(R.id.fb);
             insta = (ImageView) itemView.findViewById(R.id.insta);
             what = (ImageView) itemView.findViewById(R.id.wh);
             share = (ImageView) itemView.findViewById(R.id.share);
             Title = (TextView) itemView.findViewById(R.id.ptitle);
             Mtype = (TextView) itemView.findViewById(R.id.mText);
             Details = (TextView) itemView.findViewById(R.id.pdetails);
             Shok = (TextView) itemView.findViewById(R.id.pshok);
             About = (TextView) itemView.findViewById(R.id.pAbout);
             promo =  itemView.findViewById(R.id.hidePromo);
             linear = (LinearLayout) itemView.findViewById(R.id.clickLinearPost);

        }

    }

    public static class Modal2nd extends RecyclerView.ViewHolder {

        ImageView Img,fb,what,insta,share;
        TextView Title,Mtype,Details,Shok,About;
        LinearLayout linear,promo;

        Modal2nd(@NonNull View itemView) {
            super(itemView);

            Img = (ImageView) itemView.findViewById(R.id.pProfile);
            fb = (ImageView) itemView.findViewById(R.id.fb);
            insta = (ImageView) itemView.findViewById(R.id.insta);
            what = (ImageView) itemView.findViewById(R.id.wh);
            share = (ImageView) itemView.findViewById(R.id.share);
            promo = itemView.findViewById(R.id.hidePromo);
            Title = (TextView) itemView.findViewById(R.id.ptitle);
            Mtype = (TextView) itemView.findViewById(R.id.mText);
            Details = (TextView) itemView.findViewById(R.id.pdetails);
            Shok = (TextView) itemView.findViewById(R.id.pshok);
            About = (TextView) itemView.findViewById(R.id.pAbout);
            linear = (LinearLayout) itemView.findViewById(R.id.clickLinearPost);

        }

    }

    public static class Modal3rd extends RecyclerView.ViewHolder {

        ImageView Img,fb,what,insta,share;;
        TextView Title,Mtype,Details,Shok;
        LinearLayout linear,promo;

        Modal3rd(@NonNull View itemView) {
            super(itemView);

            Img = (ImageView) itemView.findViewById(R.id.pProfile);
            Title = (TextView) itemView.findViewById(R.id.ptitle);
            fb = (ImageView) itemView.findViewById(R.id.fb);
            promo =  itemView.findViewById(R.id.hidePromo);
            what = (ImageView) itemView.findViewById(R.id.wh);
            insta = (ImageView) itemView.findViewById(R.id.insta);
            share = (ImageView) itemView.findViewById(R.id.share);
            Mtype = (TextView) itemView.findViewById(R.id.mText);
            Details = (TextView) itemView.findViewById(R.id.pdetails);
            Shok = (TextView) itemView.findViewById(R.id.pshok);
            linear = (LinearLayout) itemView.findViewById(R.id.clickLinearPost);

        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (plist.get(position).getModel())
        {
            case 1:
                return HomeViewModel.TYPE_1;
            case 2:
                return HomeViewModel.TYPE_2;
            case 3:
                return HomeViewModel.TYPE_3;
                default:
                    return -1;
        }
    }

    public final void OnClickShare(View view ,LinearLayout linear,String social){

        Bitmap bitmap =getBitmapFromView(linear);
        try {
            File file = new File(context.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            if(social != "all")
            {
                intent.setPackage(social);
            }
            intent.putExtra(Intent.EXTRA_TEXT,
                    "भावपूर्ण श्रद्धांजलि\uD83D\uDE4F \uD83D\uDE1E \n\nCheckout शोकांजलि App at: https://play.google.com/store/apps/details?id=com.traidev.shokanjali");
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpeg");
            //startActivity(Intent.createChooser(intent, "Share image via"));
            ((Activity)contextd).startActivityForResult(Intent.createChooser(intent, "Share Post via"),CODE_FOR_RESULT);
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
