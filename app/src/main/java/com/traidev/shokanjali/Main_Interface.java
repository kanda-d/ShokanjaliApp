package com.traidev.shokanjali;

import com.traidev.shokanjali.ui.home.HomeViewModel;
import com.traidev.shokanjali.ui.pandits.PanditViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Main_Interface {


    @POST("fetch_funerals.php")
    Call<List<HomeViewModel>> getPost( @Query("PostChoice") String key);

    @POST("fetch_funerals.php")
    Call<List<HomeViewModel>> getSearches(
            @Query("keyword") String key
    );

    @POST("fetch_funerals.php")
    Call<List<HomeViewModel>> getDraftPost(
            @Query("getid") String user
    );

    @POST("fetch_pandit.php")
    Call<List<PanditViewModel>> getPandit();

    @FormUrlEncoded
    @POST("pandit_contact.php")
    Call<DefaultResponse> sendMessage(
            @Field("name") String name,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("pandit") String pid,
            @Field("message") String msg
    );

    @POST("login_user.php")
    Call<DefaultResponse> loginUser(
            @Query("username") String email,
            @Query("otp") String otp
    );

    @POST("order_status.php")
    Call<DefaultResponse> orderStatus(
            @Query("postid") int id,
            @Query("otype") String password,
            @Query("userId") String user,
            @Query("order_status") String mobileid
    );


    @POST("login_user.php")
    Call<DefaultResponse> loginVerify(
            @Query("mobile") String email,
            @Query("loginotp") String password,
            @Query("mobile_id") String mobileid
    );

    @Multipart
    @POST("reg_pandit.php")
        Call<DefaultResponse> createPandit(
            @Part MultipartBody.Part file, @Part("file") RequestBody title,
            @Part("name") String name,
            @Part("address") String address,
            @Part("mobile") String mobile,
            @Part("loc") String loc,
            @Part("user") String user,
            @Part("about") String about
    );

    @POST("create_user.php")
    Call<DefaultResponse> createUser(
            @Query("name") String name,
            @Query("mobile") String mobile,
            @Query("otp") String otp
    );

    @POST("verify_user.php")
    Call<DefaultResponse> verifyUser(
            @Query("Vmobile") String mobile,
            @Query("verifyOtp") String otp,
            @Query("mobile_id") String mobileid
    );

    @Multipart
    @POST("add_funerals.php")
    Call<DefaultResponse> addFunral(@Part MultipartBody.Part file,
                                     @Part("file") RequestBody title,
                                     @Part MultipartBody.Part addhar,
                                     @Part("aadhar") RequestBody adhtitle,
                                     @Part("name") String name,
                                     @Part("type") String type,
                                     @Part("details") String details,
                                     @Part("shok") String shok,
                                     @Part("about") String about,
                                     @Part("address") String address,
                                     @Part("mobile") String mob,
                                     @Part("lang") String lang,
                                     @Part("view") int view,
                                     @Part("user") String user
    );

    @Multipart
    @POST("update_funerals.php")
    Call<DefaultResponse> updatePost(
             @Part("post_id") String id,
             @Part("name") String name,
             @Part("type") String type,
             @Part("details") String details,
             @Part("shok") String shok,
             @Part("about") String about,
             @Part("address") String address,
             @Part("mobile") String mob,
             @Part("view") int view

    );

    @Multipart
    @POST("update_funerals.php")
    Call<DefaultResponse> updatePostProfile(@Part MultipartBody.Part file, @Part("file") RequestBody title,
                                     @Part("post_id") String id,
                                     @Part("name") String name,
                                     @Part("type") String type,
                                     @Part("details") String details,
                                     @Part("shok") String shok,
                                     @Part("about") String about,
                                     @Part("address") String address,
                                     @Part("mobile") String mob,
                                     @Part("view") int view

    );



}
