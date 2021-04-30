package com.example.pssmobile.ui.login.reader

import Data
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pssmobile.adapter.DailyRunsheetAdapter
import com.example.pssmobile.databinding.FragmentPatrolRunsheetBinding
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.utils.Singleton
import java.util.ArrayList

class PatrolRunsheetFragment : BaseFragment<ZohoViewModel, FragmentPatrolRunsheetBinding, ZohoRepository>() {
    private lateinit var mContext: Context
    private lateinit var dailyRunsheetAdapter: DailyRunsheetAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getDailyRunsheetData(Singleton.zohoCreatorUserId)

        viewModel.dailyRunsheetData.observe(viewLifecycleOwner, Observer {
            Log.d("App", "Daily Runsheet Response" + it.toString())
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    val dataList = it.value.detail.data

                    binding.rvRunsheetList.layoutManager = LinearLayoutManager(
                            mContext,
                            LinearLayoutManager.VERTICAL,
                            false
                    )
                    binding.rvRunsheetList.addItemDecoration(
                            DividerItemDecoration(
                                    binding.rvRunsheetList.context,
                                    DividerItemDecoration.VERTICAL
                            )
                    )
                    var listSize = dataList.size;
                    binding.tvJobCount.isVisible = true;
                    binding.tvJobCount.setText(" " +listSize + " Jobs")
                    dailyRunsheetAdapter = DailyRunsheetAdapter(mContext, dataList as ArrayList<Data>)
                    binding.rvRunsheetList.adapter = dailyRunsheetAdapter
                    binding.rvRunsheetList.setHasFixedSize(true)
                }
            }
        })
    }

    override fun getViewModel(): Class<ZohoViewModel> = ZohoViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentPatrolRunsheetBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        ZohoRepository(remoteDataSource.buildApi(ZohoApi::class.java))
}