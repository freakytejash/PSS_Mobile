package com.example.pssmobile.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.pssmobile.R


class ScanNfcTagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_nfc_tag)

        val et_siteName = findViewById<EditText>(R.id.et_siteName)
        val et_imeiNo = findViewById<EditText>(R.id.et_ImeiNo)
        val btn_continue = findViewById<Button>(R.id.btn_continue)

        et_imeiNo.setText(getDeviceIMEI())

        btn_continue.setOnClickListener {
            val intent = Intent(this, DetailsFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceIMEI(): String {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    }
}