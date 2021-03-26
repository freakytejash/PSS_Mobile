package com.example.pssmobile.retrofit

import com.example.pssmobile.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("services")
    fun getServices() : retrofit2.Call<LoggedInUser>

}