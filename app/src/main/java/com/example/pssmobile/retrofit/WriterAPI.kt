package com.example.pssmobile.retrofit

import com.example.pssmobile.data.model.WriterResponse
import retrofit2.Call
import retrofit2.http.GET

interface WriterAPI {
    @GET("/api/json/mobileapp/view/Checkpoint_Database_Report1?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap&raw=true&zc_ownername=accountsperthsecurityservices")
    fun getJSON(): Call<WriterResponse?>?
}