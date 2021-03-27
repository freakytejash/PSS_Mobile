package com.example.pssmobile.repository

import com.example.pssmobile.retrofit.UserApi


class UserRepository(
    private val api: UserApi
) : BaseRepository() {

    suspend fun getUser() = safeApiCall {
        //api.getUser()
    }

}