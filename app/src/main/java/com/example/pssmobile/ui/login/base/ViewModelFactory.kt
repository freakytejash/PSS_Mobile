package com.example.pssmobile.ui.login.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pssmobile.repository.*
import com.example.pssmobile.ui.login.auth.AuthViewModel
import com.example.pssmobile.ui.login.home.AddEditUserViewModel
import com.example.pssmobile.ui.login.home.HomeViewModel
import com.example.pssmobile.ui.login.home.UserListViewModel
import com.example.pssmobile.ui.login.reader.ReaderViewModel
import com.example.pssmobile.ui.login.reader.ZohoViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as UserRepository) as T
            modelClass.isAssignableFrom(ReaderViewModel::class.java) -> ReaderViewModel(repository as ReaderRepository) as T
            modelClass.isAssignableFrom(ZohoViewModel::class.java) -> ZohoViewModel(repository as ZohoRepository) as T
            modelClass.isAssignableFrom(UserListViewModel::class.java) -> UserListViewModel(repository as UserListRepository) as T
            modelClass.isAssignableFrom(AddEditUserViewModel::class.java) -> AddEditUserViewModel(repository as AddEditUserRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}