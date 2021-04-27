package com.example.pssmobile.retrofit;

import com.example.pssmobile.data.model.AddCheckpointRequest;
import com.example.pssmobile.data.model.AddSiteRequest;
import com.example.pssmobile.data.model.BureauListResponse;
import com.example.pssmobile.data.model.SiteCheckpointResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestWriterInterface {

    @POST("api/Site/AddSite")
    Call<ResponseBody> addSite(@Body AddSiteRequest request);

    @GET("api/Site/GetBureauList")
    Call<BureauListResponse> getBureauList();

    @GET("api/Checkpoint/GetSiteList")
    Call<SiteCheckpointResponse> getSiteCheckpointList();

    @POST("api/Checkpoint/AddCheckpoint")
    Call<ResponseBody> addCheckpoint(@Body AddCheckpointRequest request);

}
