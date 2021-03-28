package com.example.pssmobile.ui.login.writer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NfcAdapter
import android.widget.Toast

object Tools {
    fun checkNFC(nfcAdapter: NfcAdapter?): Boolean {
        return if (nfcAdapter != null && nfcAdapter.isEnabled) {
            true
        } else false
    }

    @SuppressLint("WrongConstant")
    fun displayToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, 1).show()
    }

    @SuppressLint("WrongConstant")
    fun foregroundDispatchSetup(activity: Activity, nfcAdapter: NfcAdapter, mimeType: String?, techList: Array<Array<String?>?>?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = 536870912
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val intentFilters = arrayOf(IntentFilter())
        intentFilters[0].addAction("android.nfc.action.NDEF_DISCOVERED")
        intentFilters[0].addCategory("android.intent.category.DEFAULT")
        try {
            intentFilters[0].addDataType(mimeType)
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, techList)
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("Mime type error. Please check it.")
        }
    }
}