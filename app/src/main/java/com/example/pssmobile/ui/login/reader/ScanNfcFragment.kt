package com.example.pssmobile.ui.login.reader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import com.example.pssmobile.R
import com.example.pssmobile.databinding.FragmentScanNfcBinding
import com.example.pssmobile.repository.ReaderRepository
import com.example.pssmobile.retrofit.ReaderApi
import com.example.pssmobile.ui.login.DetailsFormActivity
import com.example.pssmobile.ui.login.base.BaseFragment

class ScanNfcFragment : BaseFragment<ReaderViewModel, FragmentScanNfcBinding, ReaderRepository>() {
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*viewModel.getZohoCreator()
        viewModel.zohoCreatorReader.observe(viewLifecycleOwner, Observer {
            Log.d("App","Response of zoho creator: " + it.toString())
        })*/

        binding.etImeiNo.setText(getDeviceIMEI())

        binding.btnContinue.setOnClickListener {
            /*val intent = Intent(this, DetailsFormActivity::class.java)
            startActivity(intent)
            finish()*/
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceIMEI(): String {
        return Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun getViewModel() = ReaderViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentScanNfcBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ReaderRepository {
        return ReaderRepository(remoteDataSource.buildApi(ReaderApi::class.java))
    }
}