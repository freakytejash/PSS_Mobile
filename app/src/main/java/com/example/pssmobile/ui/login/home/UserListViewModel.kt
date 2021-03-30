package com.example.pssmobile.ui.login.home

import UserList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.repository.UserListRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.base.BaseViewModel
import kotlinx.coroutines.launch

class UserListViewModel(private val repository: UserListRepository) : BaseViewModel(repository) {

    private val userdataList: MutableLiveData<Resource<UserList>> = MutableLiveData()
    val userData: LiveData<Resource<UserList>>
        get() = userdataList

    fun getUserData() = viewModelScope.launch {
        userdataList.value = repository.getUserListData()
    }
}