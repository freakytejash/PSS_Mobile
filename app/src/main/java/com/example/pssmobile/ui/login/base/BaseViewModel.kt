package com.example.pssmobile.ui.login.base

import androidx.lifecycle.ViewModel
import com.example.pssmobile.repository.BaseRepository
import com.example.pssmobile.retrofit.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(api: UserApi) = withContext(Dispatchers.IO) { repository.logout(api) }

}