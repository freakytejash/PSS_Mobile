package com.example.pssmobile.ui.login.writer

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.nfc.*
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.internal.view.SupportMenu
import com.example.pssmobile.R
import java.io.IOException

class NFCWriter : AppCompatActivity() {
    var BASE_URL = "https://creator.zoho.com"
    var CheckpoinDescription: TextView? = null
    private val MIMETYPE = "*/*"
    private var _editTextData: TextView? = null
    var btnSubmit: Button? = null
    var btn_writeEdit: Button? = null
    var clicked = false
    private val context: Context? = null
    var et_Address: TextView? = null
    var et_Bureau: TextView? = null
    var et_ID: TextView? = null
    private var nfcAdapter: NfcAdapter? = null
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nfc)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (Tools.checkNFC(nfcAdapter)) {
            intentHandler(intent)
        } else {
            Tools.displayToast(this, "This device doesn't support NFC or it is disabled.")
        }
        supportActionBar!!.setTitle("Write & Save" as CharSequence)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        _editTextData = findViewById<View>(R.id.et_CheckpointName) as TextView
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        //this.btnSubmit = (Button) findViewById(R.id.btn_submit);
        et_Bureau = findViewById<View>(R.id.et_Bureau) as TextView
        et_ID = findViewById<View>(R.id.et_id) as TextView
        et_Address = findViewById<View>(R.id.et_Address) as TextView
        CheckpoinDescription = findViewById<View>(R.id.et_CheckpointDescription) as TextView
        val getID = intent.getStringExtra("getIDs")
        val getCheckpoint_Name = intent.getStringExtra("getCheckpoint_Name")
        val getAddress = intent.getStringExtra("getAddress")
        val getBureau = intent.getStringExtra("getBureau")
        val getCheckpoint_description = intent.getStringExtra("getCheckpoint_description")
        if (getID != null) {
            et_ID!!.text = getID
            _editTextData!!.text = getCheckpoint_Name
            et_Bureau!!.text = getBureau
            et_Address!!.text = getAddress
            CheckpoinDescription!!.text = getCheckpoint_description
        }
        btn_writeEdit = findViewById<View>(R.id.btn_write) as Button
        btn_writeEdit!!.setOnClickListener {
            Toast.makeText(this@NFCWriter, "Tap the TAG in the phone to write.", 0).show()
            this@NFCWriter.et_ID!!.setEnabled(false)
            this@NFCWriter.btn_writeEdit!!.setEnabled(false)
            this@NFCWriter.et_ID!!.setTextColor(SupportMenu.CATEGORY_MASK)
            this@NFCWriter.clicked = true
            //intentHandler(getIntent());
        }
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentHandler(intent)
    }

    private fun intentHandler(intent: Intent) {
        val intentAction = intent.action
        if ("android.nfc.action.NDEF_DISCOVERED" == intentAction || "android.nfc.action.TECH_DISCOVERED" == intentAction) {
            val tag = intent.getParcelableExtra<Parcelable>("android.nfc.extra.TAG") as Tag?
            for (tech in tag!!.techList) {
                if (tech == Ndef::class.java.name) {
                    writeNfcTag(et_ID!!.text.toString(), tag)
                }
            }
        }
    }

    private fun writeNfcTag(msg: String, tag: Tag?) {
        if (clicked) {
            val ndefRecords = arrayOfNulls<NdefRecord>(1)
            if (Build.VERSION.SDK_INT >= 21) {
                ndefRecords[0] = NdefRecord.createTextRecord("", msg)
            }
            val ndefMessage = NdefMessage(ndefRecords)
            val ndef = Ndef.get(tag)
            try {
                ndef.connect()
                ndef.writeNdefMessage(ndefMessage)
                ndef.close()
                val application = application
                val sb = StringBuilder()
                sb.append("Text written to the tag: ")
                sb.append(msg)
                Tools.displayToast(application, sb.toString())
            } catch (e: FormatException) {
                //ThrowableExtension.printStackTrace(e);
                Toast.makeText(this@NFCWriter, "" + e.message, Toast.LENGTH_SHORT).show()
            } catch (e2: IOException) {
                //ThrowableExtension.printStackTrace(e2);
                Toast.makeText(this@NFCWriter, "" + e2.message, Toast.LENGTH_SHORT).show()
            }
            et_ID!!.isEnabled = true
            btn_writeEdit!!.isEnabled = true
            et_ID!!.setTextColor(-16711936)
            clicked = false
            return
        }
        Toast.makeText(applicationContext, "Please Click Write to Tag", Toast.LENGTH_SHORT).show()
    }

    /* access modifiers changed from: protected */
    public override fun onResume() {
        super.onResume()
        Tools.foregroundDispatchSetup(this, nfcAdapter!!, "*/*", arrayOf(arrayOf(Ndef::class.java.name)))
    }

    /* access modifiers changed from: protected */
    public override fun onPause() {
        nfcAdapter!!.disableForegroundDispatch(this)
        super.onPause()
    }

    override fun onBackPressed() {
        AskOption().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != 16908332) {
            return super.onOptionsItemSelected(item)
        }
        onBackPressed()
        return true
    }

    private fun AskOption(): AlertDialog {
        return AlertDialog.Builder(this).setTitle("PSS" as CharSequence).setMessage("Are you sure you want go back to menu?" as CharSequence).setPositiveButton("Yes" as CharSequence, DialogInterface.OnClickListener { dialog, whichButton -> this@NFCWriter.finish() }).setNegativeButton("No" as CharSequence, DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).create()
    }
}