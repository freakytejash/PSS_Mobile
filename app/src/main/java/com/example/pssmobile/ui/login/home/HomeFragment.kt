package com.example.pssmobile.ui.login.home

import UsersDetails
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pssmobile.adapter.UserAdapter
import com.example.pssmobile.data.model.Detail
import com.example.pssmobile.data.model.User
import com.example.pssmobile.databinding.FragmentHomeBinding
import com.example.pssmobile.databinding.FragmentPatrolRunsheetBinding
import com.example.pssmobile.repository.UserListRepository
import com.example.pssmobile.repository.UserRepository
import com.example.pssmobile.retrofit.GetUsersApi
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.retrofit.UserApi
import com.example.pssmobile.ui.login.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.ArrayList


class HomeFragment : BaseFragment<UserListViewModel, FragmentHomeBinding, UserListRepository>() {
    private lateinit var mContext: Context
    private lateinit var userAdapter: UserAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getUserData()

        viewModel.userData.observe(viewLifecycleOwner, Observer {
            Log.d("App", "Users List" + it.toString())
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    val dataList = it.value.detail

                    binding.rvUserlist.layoutManager = LinearLayoutManager(
                        mContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    binding.rvUserlist.addItemDecoration(
                        DividerItemDecoration(
                            binding.rvUserlist.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    userAdapter = UserAdapter(mContext, dataList as ArrayList<UsersDetails>)
                    binding.rvUserlist.adapter = userAdapter
                    binding.rvUserlist.setHasFixedSize(true)
                }
            }
        })
    }

    override fun getViewModel(): Class<UserListViewModel> = UserListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        UserListRepository(remoteDataSource.buildApi(GetUsersApi::class.java))
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        *//*binding.buttonLogout.setOnClickListener {
            logout()
        }*//*
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
    }*/
}