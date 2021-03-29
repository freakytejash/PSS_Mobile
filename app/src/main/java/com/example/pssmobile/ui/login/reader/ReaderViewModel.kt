package com.example.pssmobile.ui.login.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.repository.ReaderRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.base.BaseViewModel
import kotlinx.coroutines.launch

class ReaderViewModel(private val repository: ReaderRepository) : BaseViewModel(repository){

    /*private val _zohoCreatorReader: MutableLiveData<Resource<ZohoCreatorResponse>> = MutableLiveData()
    val zohoCreatorReader: LiveData<Resource<ZohoCreatorResponse>>
        get() = _zohoCreatorReader

    fun getZohoCreator() = viewModelScope.launch {
        _zohoCreatorReader.value = repository.getZohoCreatorResponse()
    }*/
}