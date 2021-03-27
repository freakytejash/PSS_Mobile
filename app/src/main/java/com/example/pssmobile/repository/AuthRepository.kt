package com.example.pssmobile.repository

import com.example.pssmobile.data.UserPreferences
import com.example.pssmobile.retrofit.AuthApi


class AuthRepository(
        private val api: AuthApi,
        private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }

}