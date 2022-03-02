package com.codewithsk.chetankoli.Server;


import com.codewithsk.chetankoli.Models.Categiry;
import com.codewithsk.chetankoli.Models.Videos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @POST("videos")
    Call<ArrayList<Videos>> getAllVideos();

    @POST("cat")
    Call<ArrayList<Categiry>> getCategiry();

    @FormUrlEncoded
    @POST("catvids")
    Call<ArrayList<Videos>> getCategiryWiseVideos(@Field("cat") String categiry);

}
