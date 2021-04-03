package com.example.pssmobile.ui.login.reader

import Data
import com.example.pssmobile.R
import android.os.Bundle
import android.view.*
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.pssmobile.databinding.FragmentPatrolRunsheetDetailsBinding
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.DetailsFormActivity
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.ui.login.home.HomeActivity
import com.example.pssmobile.ui.login.startNewActivity


class PatrolRunsheetDetailsFragment :  BaseFragment<ZohoViewModel, FragmentPatrolRunsheetDetailsBinding, ZohoRepository>() {
    val args: PatrolRunsheetDetailsFragmentArgs by navArgs()
    private lateinit var model: Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model = args.datamodel

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit ->{
                //requireActivity().startNewActivity(DetailsFormActivity::class.java)
                val action: NavDirections = PatrolRunsheetDetailsFragmentDirections.actionPatrolRunsheetDetailsFragmentToPatrolRunsheetEntryEditFragment(model)
                view?.let { Navigation.findNavController(it).navigate(action) }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getViewModel() = ZohoViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)
            = FragmentPatrolRunsheetDetailsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        ZohoRepository(remoteDataSource.buildApi(ZohoApi::class.java))

}