/*
package com.example.pssmobile.ui.login.writer

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pssmobile.R
import com.example.pssmobile.data.model.Getters
import com.example.pssmobile.ui.login.writer.adapter.ListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class LocationList {
    val BASE_URL = "https://creator.zoho.com/"
    var context: Context? = null

    // access modifiers changed from: private
    var mAdapter: ListAdapter? = null

    */
/* access modifiers changed from: private *//*

    var mArrayList: ArrayList<Getters>? = null

    */
/* access modifiers changed from: private *//*

    var mRecyclerView: RecyclerView? = null
    var tvNFCContent: EditText? = null
    var progressDialog: ProgressDialog? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        getSupportActionBar().setTitle("Locations" as CharSequence)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getSupportActionBar().setDisplayShowHomeEnabled(true)
        context = this
        tvNFCContent = findViewById<View>(R.id.searchText) as EditText?
        tvNFCContent!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                this@MainActivity.mAdapter.getFilter().filter(s)
            }
        })
        initViews()
        loadJSON()
    }

    fun onBackPressed() {
        AskOption().show()
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != 16908332) {
            return super.onOptionsItemSelected(item)
        }
        onBackPressed()
        return true
    }

    private fun AskOption(): AlertDialog {
        return AlertDialog.Builder(this).setTitle("PSS" as CharSequence).setMessage("Are you sure you want to Exit?" as CharSequence).setPositiveButton("Yes" as CharSequence, DialogInterface.OnClickListener { dialog, whichButton -> this@MainActivity.finish() }).setNegativeButton("No" as CharSequence, DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).create()
    }

    private fun initViews() {
        mRecyclerView = findViewById<View>(R.id.card_recycler_view) as RecyclerView?
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    private fun loadJSON() {
        progressDialog!!.setMessage("Please Wait")
        progressDialog!!.show()
        (Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(RequestInterface::class.java) as RequestInterface).getJSON().enqueue(object : Callback<JSONResponse?> {
            override fun onResponse(call: Call<JSONResponse?>, response: Response<JSONResponse?>) {
                this@MainActivity.mArrayList = ArrayList<Any?>(Arrays.asList((response.body() as JSONResponse?).getAndroid()))
                this@MainActivity.mAdapter = ListAdapter(this@MainActivity.mArrayList)
                this@MainActivity.mRecyclerView.setAdapter(this@MainActivity.mAdapter)
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<JSONResponse?>, t: Throwable) {
                Log.d("Error", t.message!!)
            }
        })
    }
}*/
