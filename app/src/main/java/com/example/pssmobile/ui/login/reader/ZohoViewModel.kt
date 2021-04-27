package com.example.pssmobile.ui.login.reader

import RunsheetModel
import UpdateJobRequestModel
import UpdateJobResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pssmobile.data.model.AddSiteRequest
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.reflect.Type

class ZohoViewModel(private val repository: ZohoRepository) : BaseViewModel(repository) {

    private val _zohodailyRunsheet: MutableLiveData<Resource<RunsheetModel>> = MutableLiveData()
    val dailyRunsheetData: LiveData<Resource<RunsheetModel>>
        get() = _zohodailyRunsheet

    fun getDailyRunsheetData(userId: String) = viewModelScope.launch {
        _zohodailyRunsheet.value = repository.getDailyRunsheetData(userId)
    }
    
    private val _updateJobResponse : MutableLiveData<Resource<UpdateJobResponse>> = MutableLiveData()
    val updateJobResponse : LiveData<Resource<UpdateJobResponse>>
        get() = _updateJobResponse

    fun updateDailyRunsheetJob(updateJobRequestModel: UpdateJobRequestModel) = viewModelScope.launch {
        _updateJobResponse.value = repository.updateDailyRunsheetJob(updateJobRequestModel)
    }

    /*private val _addSiteResponse : MutableLiveData<Resource<UpdateJobResponse>> = MutableLiveData()
    val addSiteResponse : LiveData<Resource<UpdateJobResponse>>
        get() = _updateJobResponse

    fun addSiteDetails(addSiteRequest: AddSiteRequest) = viewModelScope.launch {
        _updateJobResponse.value = repository.addSite(addSiteRequest)
    }*/


    /*suspend fun saveZohoAuthToken(zohotoken: String) {
        repository.saveZohoAuthToken(zohotoken)
    }*/

    /*fun getdailyRunsheet(zohoAuthToken: String){
        var zohoTokenApi: ZohoApi? = APIClient.getClient(BASE_URL, zohoAuthToken)?.create(ZohoApi::class.java)
        var call :Call<JsonObject> = zohoTokenApi?.getDailyRunsheet()!!
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful){
                    val gson: Gson = Gson()
                    val type: Type = object :TypeToken<DailyRunsheetModel>(){
                    }.type

                    var dailyRunsheeResponse: DailyRunsheetModel = gson.fromJson(response.body(), type)
                    Log.d("App", "Daily runsheet response: " + dailyRunsheeResponse.toString())

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })
    }*/
}