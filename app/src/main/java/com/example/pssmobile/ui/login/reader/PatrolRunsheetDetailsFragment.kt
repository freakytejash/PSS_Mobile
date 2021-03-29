package com.example.pssmobile.ui.login.reader

import Data
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pssmobile.databinding.FragmentPatrolRunsheetDetailsBinding
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class PatrolRunsheetDetailsFragment :  BaseFragment<ZohoViewModel, FragmentPatrolRunsheetDetailsBinding, ZohoRepository>() {
    val args: PatrolRunsheetDetailsFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var model = args.datamodel

        binding.tvLocation.text = model.location1
        binding.tvJobType.text = model.job_type
        binding.tvStartDate.text = model.start_date_time
        //binding.tvAddress.text = model.location1
        binding.tvEndDate.text = model.end_date_time
        binding.tvJob.text = model.select_a_job1.display_value
        binding.tvAllocatedTo.text = model.allocated_to.display_value
        binding.tvDateCompleted.text = model.date_time_job_completed
        binding.tvPatrolOfficer.text = model.patrol_Officer
    }

    override fun getViewModel() = ZohoViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)
            = FragmentPatrolRunsheetDetailsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        ZohoRepository(remoteDataSource.buildApi(ZohoApi::class.java))

}