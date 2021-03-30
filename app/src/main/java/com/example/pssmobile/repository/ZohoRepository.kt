package com.example.pssmobile.repository

import com.example.pssmobile.retrofit.ZohoApi

class ZohoRepository(private val api: ZohoApi) : BaseRepository() {

    suspend fun getDailyRunsheetData(userId: String) = safeApiCall {
        api.getDailyRunsheet(userId)
    }

    /*suspend fun saveZohoAuthToken(zohotoken: String){
        preferences.saveAuthToken(zohotoken)
    }*/
}