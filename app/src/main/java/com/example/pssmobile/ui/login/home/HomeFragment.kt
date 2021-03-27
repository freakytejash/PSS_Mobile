package com.example.pssmobile.ui.login.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.pssmobile.data.model.Detail
import com.example.pssmobile.data.model.User
import com.example.pssmobile.databinding.FragmentHomeBinding
import com.example.pssmobile.repository.UserRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.retrofit.UserApi
import com.example.pssmobile.ui.login.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.progressbar.visible(false)

        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //binding.progressbar.visible(false)
                    updateUI(it.value.detail)
                }
                is Resource.Loading -> {
                   // binding.progressbar.visible(true)
                }
            }
        })

        /*binding.buttonLogout.setOnClickListener {
            logout()
        }*/
    }

    private fun updateUI(user: Detail) {
        with(binding) {
            //textViewId.text = user.id.toString()
            //textViewName.text = user.name
            //textViewEmail.text = user.email
        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }
}