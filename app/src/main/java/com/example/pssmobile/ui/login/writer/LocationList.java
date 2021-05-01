package com.example.pssmobile.ui.login.writer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pssmobile.R;
import com.example.pssmobile.data.model.Getters;
import com.example.pssmobile.data.model.WriterResponse;
import com.example.pssmobile.retrofit.WriterAPI;
import com.example.pssmobile.ui.login.MainActivity;
import com.example.pssmobile.ui.login.auth.AuthActivity;
import com.example.pssmobile.ui.login.reader.ScanNfcFragment;
import com.example.pssmobile.ui.login.reader.ScanNfcTagActivity;
import com.example.pssmobile.ui.login.writer.adapter.ListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.*;

public class LocationList extends AppCompatActivity {
    public static final String BASE_URL = "https://creator.zoho.com/";
    Context context;
    // access modifiers changed from: private
    public ListAdapter mAdapter;
    /* access modifiers changed from: private */
    public ArrayList<Getters> mArrayList;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    EditText tvNFCContent;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        getSupportActionBar().setTitle((CharSequence) "Locations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.context = this;
        tvNFCContent = (EditText) findViewById(R.id.searchText);
        tvNFCContent.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                LocationList.this.mAdapter.getFilter().filter(s);
            }
        });
        initViews();
        loadJSON();

    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_site:
                // do something
                return true;
            case R.id.logout:
                // do something
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

    public void onBackPressed() {
        AskOption().show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

         if (item.getItemId() == R.id.checkpoint_add_nfc)
        {
          Intent intent = new Intent(LocationList.this, AddCheckPointActivity.class);
          startActivity(intent);
        }
         else if (item.getItemId() == R.id.logout_nfc_writer)
         {
             Intent intent = new Intent(LocationList.this, AuthActivity.class);
             startActivity(intent);
         }
         else if (item.getItemId() != 16908332) {
             return super.onOptionsItemSelected(item);
         }
//        onBackPressed();
        return true;
    }

    private AlertDialog AskOption() {
        return new AlertDialog.Builder(this).setTitle((CharSequence) "PSS").setMessage((CharSequence) "Are you sure you want to Exit?").setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LocationList.this.finish();
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(LocationList.this));
    }


    private void loadJSON() {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        ((WriterAPI) new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(WriterAPI.class)).getJSON().enqueue(new Callback<WriterResponse>() {
            public void onResponse(Call<WriterResponse> call, Response<WriterResponse> response) {
                LocationList.this.mArrayList = new ArrayList(Arrays.asList(((WriterResponse) response.body()).getAndroid()));
                LocationList.this.mAdapter = new ListAdapter(LocationList.this.mArrayList);
                LocationList.this.mRecyclerView.setAdapter(LocationList.this.mAdapter);
                progressDialog.dismiss();
            }

            public void onFailure(Call<WriterResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

/*    @Override
    public void onCreateOptionsMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_site, menu);
    }*/
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_site,menu);
       // menu.removeItem(R.id.action_add);
        return super.onCreateOptionsMenu(menu);
    }


    /*public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            R.id.action_add
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;

}*/
}
