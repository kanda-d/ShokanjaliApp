package com.traidev.shokanjali.ui.pandits;



import com.google.gson.annotations.SerializedName;

public class PanditViewModel {

    @SerializedName("id")
    private String Id;

    @SerializedName("title")
    private String Title;

    @SerializedName("thumb")
    private String Thumbnil;

    @SerializedName("about")
    private String Address;

    @SerializedName("mobile")
    private String Mobile;



    public String getTitle() {
        return Title;
    }
    public String getId() {
        return Id;
    }

    public String getThumbnil() {
        return Thumbnil;
    }

    public String getAddress() {
        return Address;
    }

    public String getMobile() {
        return Mobile;
    }

}




