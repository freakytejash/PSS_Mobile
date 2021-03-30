package com.example.pssmobile.ui.login.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.pssmobile.databinding.FragmentLoginBinding
import com.example.pssmobile.repository.AuthRepository
import com.example.pssmobile.retrofit.AuthApi
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.*
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.ui.login.home.HomeActivity
import com.example.pssmobile.ui.login.reader.ScanNfcTagActivity
import com.example.pssmobile.ui.login.writer.LocationList
import com.example.pssmobile.utils.Singleton
import com.google.gson.annotations.Since
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressbar.visible(false)
        binding.buttonLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.detail.tockenString!!)
                        Singleton.zohoCreatorUserId = it.value.detail.userDataContract.zohoCreatorUserId
                        if(it.value.detail.userDataContract.roleId == 1) {
                            viewModel.saveRoleId(it.value.detail.userDataContract.roleId!!)
                            requireActivity().startNewActivity(HomeActivity::class.java)
                        } else if(it.value.detail.userDataContract.roleId == 2){
                            viewModel.saveRoleId(it.value.detail.userDataContract.roleId!!)
                            requireActivity().startNewActivity(ScanNfcTagActivity::class.java)
                        } else if(it.value.detail.userDataContract.roleId == 3){
                            viewModel.saveRoleId(it.value.detail.userDataContract.roleId!!)
                            requireActivity().startNewActivity(LocationList::class.java)
                        }
                    }
                }
                is Resource.Failure -> handleApiError(it) { login() }
            }
        })

        binding.editTextTextPassword.addTextChangedListener {
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            binding.buttonLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }


        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.editTextTextEmailAddress.text.toString().trim()
        val password = binding.editTextTextPassword.text.toString().trim()
        viewModel.login(email, password)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}