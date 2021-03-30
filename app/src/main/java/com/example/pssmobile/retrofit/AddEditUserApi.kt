package com.example.pssmobile.retrofit

import UsersDetails
import com.example.pssmobile.data.model.AddEditUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AddEditUserApi {

    @POST("User/ManageUser")
    suspend fun addEditUser(
            @Body body: UsersDetails
    ): AddEditUserResponse
}