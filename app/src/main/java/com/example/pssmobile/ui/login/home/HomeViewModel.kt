package com.example.pssmobile.ui.login.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.data.model.LoginService
import com.example.pssmobile.repository.UserRepository
import com.example.pssmobile.retrofit.Resource
import kotlinx.coroutines.launch
import com.example.pssmobile.ui.login.base.BaseViewModel

class HomeViewModel(
    private val repository: UserRepository
) : BaseViewModel(repository) {

    private val _user: MutableLiveData<Resource<LoginService>> = MutableLiveData()
    val user: LiveData<Resource<LoginService>>
        get() = _user

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        //_user.value = repository.getUser()
    }

}