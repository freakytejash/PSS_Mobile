package com.example.pssmobile.Database;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.pssmobile.R;
import com.example.pssmobile.adapter.DisplayAdapter;
import com.example.pssmobile.helper.ApiClient;
import com.example.pssmobile.helper.RequestInterface;
import com.example.pssmobile.ui.login.reader.ReadNFC;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class listDataActivity extends AppCompatActivity {
    String BASE_URL = "https://creator.zoho.com";
    boolean Trigger = false;
    /* access modifiers changed from: private */
    public AlertDialog.Builder build;
    Context context;
    /* access modifiers changed from: private */
    public SQLiteDatabase dataBase;
    String date = "";
    String imei = "";
    String android_id = "";
    private ListView listView;
    private DbHelper mHelper;
    String name = "";
    /* access modifiers changed from: private */
    public ArrayList<String> userId = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> user_chckID = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> user_dateID = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        this.context = this;
        @SuppressLint("WrongConstant") TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (Build.VERSION.SDK_INT >= 26) {
            this.imei = telephonyManager.getImei();
        } else {
            this.imei = telephonyManager.getDeviceId();
        }
       /* android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
        this.listView = (ListView) findViewById(R.id.listview);
        this.mHelper = new DbHelper(this);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, final int arg2, long arg3) {
                com.example.pssmobile.Database.listDataActivity.this.name = (String) com.example.pssmobile.Database.listDataActivity.this.user_chckID.get(arg2);
                com.example.pssmobile.Database.listDataActivity.this.date = (String) com.example.pssmobile.Database.listDataActivity.this.user_dateID.get(arg2);
                if (com.example.pssmobile.Database.listDataActivity.this.Trigger) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(com.example.pssmobile.Database.listDataActivity.this);
                    builder.setMessage((CharSequence) "Do you want to send this to ZOHO?").setCancelable(false).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            com.example.pssmobile.Database.listDataActivity.this.insert_data();
                            SQLiteDatabase access$300 = com.example.pssmobile.Database.listDataActivity.this.dataBase;
                            String str = DbHelper.TABLE_NAME;
                            StringBuilder sb = new StringBuilder();
                            sb.append("id=");
                            sb.append((String) com.example.pssmobile.Database.listDataActivity.this.userId.get(arg2));
                            access$300.delete(str, sb.toString(), null);
                            com.example.pssmobile.Database.listDataActivity.this.startActivity(new Intent(com.example.pssmobile.Database.listDataActivity.this, ReadNFC.class));
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setTitle("PSS PATROL SCANNER");
                    alert.show();
                    return;
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(com.example.pssmobile.Database.listDataActivity.this);
                builder2.setTitle((CharSequence) "PSS PATROL SCANNER");
                builder2.setMessage((CharSequence) "Please connect to Internet to send this data");
                builder2.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) null);
                builder2.show();
            }
        });
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, final int arg2, long arg3) {
                com.example.pssmobile.Database.listDataActivity.this.build = new AlertDialog.Builder(com.example.pssmobile.Database.listDataActivity.this);
                com.example.pssmobile.Database.listDataActivity.this.build.setTitle((CharSequence) "PSS PATROL SCANNER");
                com.example.pssmobile.Database.listDataActivity.this.build.setMessage((CharSequence) "Delete this data?");
                com.example.pssmobile.Database.listDataActivity.this.build.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase access$300 = com.example.pssmobile.Database.listDataActivity.this.dataBase;
                        String str = DbHelper.TABLE_NAME;
                        StringBuilder sb = new StringBuilder();
                        sb.append("id=");
                        sb.append((String) com.example.pssmobile.Database.listDataActivity.this.userId.get(arg2));
                        access$300.delete(str, sb.toString(), null);
                        com.example.pssmobile.Database.listDataActivity.this.displayData();
                        dialog.cancel();
                    }
                });
                com.example.pssmobile.Database.listDataActivity.this.build.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                com.example.pssmobile.Database.listDataActivity.this.build.create().show();
                return true;
            }
        });
        displayData();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, 0).show();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (ReadNFC.isConnectingToInternet(this)) {
            this.Trigger = true;
        } else {
            this.Trigger = false;
        }
        super.onResume();
    }

    public static boolean isConnectingToInternet(Context context2) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivity = (ConnectivityManager) context2.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void displayData() {
        this.dataBase = this.mHelper.getWritableDatabase();
        Cursor mCursor = this.dataBase.rawQuery("SELECT * FROM user", null);
        this.userId.clear();
        this.user_chckID.clear();
        this.user_dateID.clear();
        if (mCursor.moveToFirst()) {
            do {
                this.userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
                this.user_chckID.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_CHCKPOINID)));
                this.user_dateID.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_DATEID)));
            } while (mCursor.moveToNext());
        }
        this.listView.setAdapter(new DisplayAdapter(this, this.userId, this.user_chckID, this.user_dateID));
        mCursor.close();
    }

    public void insert_data() {
        RequestInterface apiCalling = ApiClient.getClient().create(RequestInterface.class);

        Call<ResponseBody> call = apiCalling.offlineInsert(this.name, this.date, this.imei);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        //Toast.makeText(listDataActivity.this, "" + json, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.pssmobile.Database.listDataActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
