package com.example.pssmobile.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.pssmobile.R
import com.example.pssmobile.data.UserPreferences
import com.example.pssmobile.ui.login.auth.AuthActivity
import com.example.pssmobile.ui.login.home.HomeActivity
import com.example.pssmobile.ui.login.reader.ReadNFC
import com.example.pssmobile.ui.login.writer.LocationList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {
            // Change this after implementation of logout
            coroutineScope.launch {
                userPreferences.roleId.collect { roleId ->
                    if (it != null) {
                        when (roleId) {
                            1 -> {
                                startNewActivity(HomeActivity::class.java)
                            }
                            2 -> {
                                startNewActivity(ReadNFC::class.java)
                            }
                            3 -> {
                                startNewActivity(LocationList::class.java)
                            }
                            else ->{
                                startNewActivity(AuthActivity::class.java)
                            }
                        }
                    }
                    else{
                        startNewActivity(AuthActivity::class.java)
                    }
                }
            }
//            val activity = if (it == null) AuthActivity::class.java else AuthActivity::class.java
//            startNewActivity(activity)
        })
    }
}