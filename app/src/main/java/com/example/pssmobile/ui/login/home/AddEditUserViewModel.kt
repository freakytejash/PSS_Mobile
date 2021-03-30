package com.example.pssmobile.ui.login.home

import UsersDetails
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.data.model.AddEditUserResponse
import com.example.pssmobile.data.model.LoginService
import com.example.pssmobile.repository.AddEditUserRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.base.BaseViewModel
import kotlinx.coroutines.launch

class AddEditUserViewModel(private val repository: AddEditUserRepository) : BaseViewModel(repository){

    private val _addEditUserResponse: MutableLiveData<Resource<AddEditUserResponse>> = MutableLiveData()
    val addEditUserResponse: LiveData<Resource<AddEditUserResponse>>
        get() = _addEditUserResponse

    fun addEditUser(usersDetails: UsersDetails) = viewModelScope.launch {
         _addEditUserResponse.value = repository.addEditUser(usersDetails)
    }
}