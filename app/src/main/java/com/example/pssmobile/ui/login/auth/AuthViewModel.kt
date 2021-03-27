package com.example.pssmobile.ui.login.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.data.model.LoginService
import com.example.pssmobile.repository.AuthRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.base.BaseViewModel
import kotlinx.coroutines.launch


class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginService>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginService>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }
}