package com.example.pssmobile.ui.login.home

import UserList
import UsersDetails
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pssmobile.R
import com.example.pssmobile.adapter.DailyRunsheetAdapter
import com.example.pssmobile.adapter.UserAdapter
import com.example.pssmobile.databinding.FragmentHomeBinding
import com.example.pssmobile.databinding.FragmentPatrolRunsheetBinding
import com.example.pssmobile.repository.UserListRepository
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.GetUsersApi
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.ui.login.reader.ZohoViewModel
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddUser.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUser : BaseFragment<UserListViewModel, FragmentHomeBinding, UserListRepository>() {
    private lateinit var mContext: Context
    private lateinit var userAdapter: UserAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressbar.visibility = View.VISIBLE
        //viewModel.getDailyRunsheetData("3354762000000189027")

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
}