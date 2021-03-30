package com.example.pssmobile.repository

import com.example.pssmobile.retrofit.GetUsersApi


class UserListRepository(private val api: GetUsersApi) : BaseRepository() {

    suspend fun getUserListData(roleId: String) = safeApiCall {
        api.getUsersList(roleId)
    }

}