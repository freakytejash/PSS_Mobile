package com.example.pssmobile.repository

import com.example.pssmobile.data.UserPreferences
import com.example.pssmobile.retrofit.ReaderApi
import com.example.pssmobile.retrofit.UserApi

class ReaderRepository(private val api: ReaderApi) : BaseRepository() {
    /*suspend fun getZohoCreatorResponse() = safeApiCall {
        api.getZohoCreatorReaderResponse()
    }*/
}