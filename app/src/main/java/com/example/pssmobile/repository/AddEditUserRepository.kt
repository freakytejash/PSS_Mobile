package com.example.pssmobile.repository

import UsersDetails
import com.example.pssmobile.retrofit.AddEditUserApi

class AddEditUserRepository(private val api: AddEditUserApi): BaseRepository() {
    suspend fun addEditUser(usersDetails: UsersDetails) = safeApiCall {
        api.addEditUser(usersDetails)
    }

}