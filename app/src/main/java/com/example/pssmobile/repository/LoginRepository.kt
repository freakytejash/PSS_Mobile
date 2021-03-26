package com.example.pssmobile.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pssmobile.data.model.LoggedInUser
import com.example.pssmobile.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainActivityRepository {

    val serviceSetterGetter = MutableLiveData<LoggedInUser>()

    fun getServicesApiCall(): MutableLiveData<LoggedInUser> {

        val call = RetrofitClient.apiInterface.getServices()

        call.enqueue(object: Callback<LoggedInUser> {
            override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<LoggedInUser>,
                response: Response<LoggedInUser>
            ) {
                // TODO("Not yet implemented")
                Log.v("DEBUG : ", response.body().toString())

                val data = response.body()

                val msg = data!!.displayName

                //serviceSetterGetter.value = LoggedInUser(msg)
            }
        })

        return serviceSetterGetter
    }
}