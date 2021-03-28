package com.example.pssmobile.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.pssmobile.R
import com.example.pssmobile.data.UserPreferences
import com.example.pssmobile.ui.login.auth.AuthActivity
import com.example.pssmobile.ui.login.home.HomeActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {
          // Change this after implementation of logout
            val activity = if (it == null) AuthActivity::class.java else AuthActivity::class.java
            startNewActivity(activity)
        })
    }

}