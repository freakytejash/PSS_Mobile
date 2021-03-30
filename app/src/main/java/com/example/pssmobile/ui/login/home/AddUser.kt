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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pssmobile.R
import com.example.pssmobile.adapter.DailyRunsheetAdapter
import com.example.pssmobile.adapter.UserAdapter
import com.example.pssmobile.databinding.FragmentAddUserBinding
import com.example.pssmobile.databinding.FragmentHomeBinding
import com.example.pssmobile.databinding.FragmentPatrolRunsheetBinding
import com.example.pssmobile.repository.AddEditUserRepository
import com.example.pssmobile.repository.UserListRepository
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.AddEditUserApi
import com.example.pssmobile.retrofit.GetUsersApi
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.ui.login.handleApiError
import com.example.pssmobile.ui.login.reader.PatrolRunsheetDetailsFragmentArgs
import com.example.pssmobile.ui.login.reader.ZohoViewModel
import kotlinx.android.synthetic.main.fragment_add_user.*
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
class AddUser : BaseFragment<AddEditUserViewModel, FragmentAddUserBinding, AddEditUserRepository>() {
    private lateinit var mContext: Context
    val args: AddUserArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var userDetailsModel = args.userDetailsModel

        if (userDetailsModel != null){
            binding.etPersonName.setText(userDetailsModel.name)
            binding.etPhone.setText(userDetailsModel.phone.toString())
            binding.etEmailAddress.setText(userDetailsModel.email)
            binding.etPassword.setText(userDetailsModel.password)
            binding.etPersonUserName.setText(userDetailsModel.zohoCreatorUserName)
            if(userDetailsModel.zohoCreatorUserId.isNotEmpty()){
                binding.etZohoCreatorUserId.setText(userDetailsModel.zohoCreatorUserId)
            }
        }

        binding.btnSubmit.setOnClickListener {
            if(!binding.checkBox.isChecked){
                Toast.makeText(mContext, "Please check the Terms and conditions", Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.VISIBLE
                userDetailsModel.name = binding.etPersonName.text.toString()
                userDetailsModel.phone = binding.etPhone.text.toString().toDouble()
                userDetailsModel.email = binding.etEmailAddress.text.toString()
                userDetailsModel.password = binding.etPassword.text.toString()
                userDetailsModel.zohoCreatorUserName = binding.etPersonUserName.text.toString()
                userDetailsModel.zohoCreatorUserId = binding.etZohoCreatorUserId.text.toString()

                viewModel.addEditUser(userDetailsModel)
            }
        }

        viewModel.addEditUserResponse.observe(viewLifecycleOwner, Observer {
            Log.d("App","Edit user response: " + it.toString())
            progressBar.visibility = View.GONE
            when (it) {
                is Resource.Success -> {
                    if (it.value.message.equals("Success")){
                        val action: NavDirections = AddUserDirections.actionAddUserToHomeFragment()
                        view?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                    }else{
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

        })
        //binding.progressbar.visibility = View.VISIBLE
        //viewModel.getDailyRunsheetData("3354762000000189027")

    }

    override fun getViewModel(): Class<AddEditUserViewModel> = AddEditUserViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentAddUserBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AddEditUserRepository(remoteDataSource.buildApi(AddEditUserApi::class.java))
}