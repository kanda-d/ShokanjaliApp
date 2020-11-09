package com.traidev.shokanjali.ui.home;



import com.google.gson.annotations.SerializedName;

public class HomeViewModel {

    public static final int TYPE_1 =1;
    public static final int TYPE_2 =2;
    public static final int TYPE_3 =3;


    @SerializedName("id")
    private String ID;

    @SerializedName("title")
    private String Title;

    @SerializedName("type")
    private String Type;

    @SerializedName("details")
    private String Details;

    @SerializedName("shok")
    private String Shok;

    @SerializedName("mobile")
    private String Mobile;

    @SerializedName("about")
    private String About;

    @SerializedName("thumb")
    private String Thumbnil;

    @SerializedName("address")
    private String Address;

    @SerializedName("model")
    private int Model;



    public String getID() {
        return ID;
    }


    public String getTitle() {
        return Title;
    }

    public String getThumbnil() {
        return Thumbnil;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getType() {
        return Type;
    }


    public String getDetails() {
        return Details;
    }

    public String getShok() {
        return Shok;
    }

    public String getAddress() {
        return Address;
    }

    public int getModel() {
        return Model;
    }

    public String getAbout() {
        return About;
    }
}




