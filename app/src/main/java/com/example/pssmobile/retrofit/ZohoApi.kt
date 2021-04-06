package com.example.pssmobile.retrofit

import RunsheetModel
import UpdateJobRequestModel
import UpdateJobResponse
import com.example.pssmobile.data.model.AddEditUserResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ZohoApi {
    /*@POST("oauth/v2/token?refresh_token=1000.0f1fc1e744e3fcf09dc2736bb88732d0.17b9cc6db94a837b0622994f0989c265&client_id=1000.GW5CJADUGJQTIUATHXV20BKMJDL7KN&client_secret=b9c1e0873714913fa637b6c9ab9a2b71acc93de028&grant_type=refresh_token")
    suspend fun getZohoToken() : ZohoToken*/

    @GET("PatrolReport/GetJobsList?")
    suspend fun getDailyRunsheet(@Query (value = "mobileUserId") userId: String): RunsheetModel

    @POST("PatrolReport/UpdateJob")
    suspend fun updateJob(
            @Body body: UpdateJobRequestModel
    ): UpdateJobResponse
}