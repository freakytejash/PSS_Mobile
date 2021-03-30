package com.example.pssmobile.retrofit

import UserList
import retrofit2.http.GET

interface GetUsersApi {


    @GET("User/GetUsers?roleId=2")
    suspend fun getUsersList(): UserList
}