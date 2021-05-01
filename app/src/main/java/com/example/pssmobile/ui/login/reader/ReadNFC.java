package com.example.pssmobile.ui.login.reader;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.internal.view.SupportMenu;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pssmobile.Database.DbHelper;
import com.example.pssmobile.Database.listDataActivity;
import com.example.pssmobile.R;
import com.example.pssmobile.retrofit.ApiClient;
import com.example.pssmobile.retrofit.RequestInterface;
import com.example.pssmobile.ui.login.auth.AuthActivity;
import com.example.pssmobile.ui.login.writer.AddCheckPointActivity;
import com.example.pssmobile.ui.login.writer.LocationList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ReadNFC extends AppCompatActivity {

    private Toolbar mTopToolbar;

    public static final String TAG = "ReadNFC";
    String BASE_URL = "https://creator.zoho.com";
    String Date;
    TextView Internet_validation;
    private final int MY_PERMISSIONS_REQUEST_CODE = 1;
    TextView TimerText;
    Button btnExit;
    Button btnHelp;
    Calendar calendar;
    String chck_ID;
    Context context;
    SQLiteDatabase dataBase;
    String date_ID;
    TextView edtLocations;
    TextView edt_name;
    private FloatingActionButton floatingActionButton;
    ImageView imageView;
    String imei = "";
   // String android_id = "";
    boolean isUpdate;
    DbHelper mHelper;
    RequestQueue mQueu;
    Tag myTag;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    SimpleDateFormat simpleDateFormat;
    String text = "";
    TextView textView;
    long time = System.currentTimeMillis();
    IntentFilter[] writeTagFilters;
    String How_many_scan;
    String edtchckName;
    String Select_Job;

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            return false;
        }
        return true;
    }

   // @SuppressLint("WrongConstant")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean isGranted = true;
            int length = grantResults.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (grantResults[i] != 0) {
                    isGranted = false;
                    break;
                } else {
                    i++;
                }
            }
            if (isGranted) {
                startApplication();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 1);
    }

   @SuppressLint("MissingPermission")
    public void startApplication() {
        @SuppressLint("WrongConstant") TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        this.imei = Build.VERSION.SDK_INT >= 26 ? telephonyManager.getImei() : telephonyManager.getDeviceId();
     /*   android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

       /* mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);*/

        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.imageView = (ImageView) findViewById(R.id.imageViews);
        this.imageView.setImageResource(R.drawable.pss);
        this.context = this;
        this.edtLocations = (TextView) findViewById(R.id.IdLocation);
        this.mQueu = Volley.newRequestQueue(this);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.mHelper = new DbHelper(this);
        this.calendar = Calendar.getInstance();
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.Date = this.simpleDateFormat.format(this.calendar.getTime());
        this.btnHelp = (Button) findViewById(R.id.btn_Q);
        this.btnExit = (Button) findViewById(R.id.btn_exit);
        this.edt_name = (TextView) findViewById(R.id.TextTest);
        this.textView = (TextView) findViewById(R.id.textPopUp);
        this.TimerText = (TextView) findViewById(R.id.Timertxt);
        this.Internet_validation = (TextView) findViewById(R.id.InternetValidation);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReadNFC.this.startActivity(new Intent(ReadNFC.this, listDataActivity.class));
               // ReadNFC.this.startActivity(new Intent(ReadNFC.this, ScanNfcTagActivity.class));
            }
        });
        this.edt_name.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ReadNFC.this.edt_name.getText().toString().length() > 3) {
                    ReadNFC.this.textWatcher();
                    ReadNFC.this.jsonParse();
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });
        if (checkPermissions()) {
            startApplication();
        } else {
            setPermissions();
        }
        if (this.nfcAdapter == null) {
            toastMessage("This device doesn't support NFC.");
            finish();
        }
        readFromIntent(getIntent());
        this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        IntentFilter tagDetected = new IntentFilter("android.nfc.action.TAG_DISCOVERED");
        tagDetected.addCategory("android.intent.category.DEFAULT");
        this.writeTagFilters = new IntentFilter[]{tagDetected};
        this.btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReadNFC.this.finish();
            }
        });
        this.btnHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReadNFC.this.startActivity(new Intent(ReadNFC.this, ExpandableLayoutMaterialDesign.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_read_nfc, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public static boolean isConnectingToInternet(Context context2) {
        @SuppressLint("WrongConstant")
        ConnectivityManager connectivity = (ConnectivityManager) context2.getSystemService("connectivity");
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
    public void jsonParse() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://creator.zoho.com/api/json/mobileapp/view/Checkpoint_Database_Report1?authtoken=26e6d588c42d569d93fcb35917e5ce7f&zc_ownername=accountsperthsecurityservices&criteria=(ID=");
        sb.append(edt_name.getText().toString());
        sb.append(")&raw=true");
        //Toast.makeText(context, ""+edt_name.getText().toString(), Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, sb.toString(), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Checkpoint_Database");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject checkpoint_db = jsonArray.getJSONObject(i);
                        String edtChechpoint = checkpoint_db.getString("Checkpoint_Entry1.Site");
                        String string = checkpoint_db.getString("Bureau");
                        Select_Job = checkpoint_db.getString("Select_Job");
                        How_many_scan = checkpoint_db.getString("How_many_scan");
                        edtchckName = checkpoint_db.getString("Checkpoint_Name");
                        TextView textView = ReadNFC.this.edtLocations;
                        StringBuilder sb = new StringBuilder();
                        sb.append(edtchckName);
                        sb.append(" \n ");
                        sb.append(edtChechpoint);
                        textView.append(sb.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        this.mQueu.add(jsonObjectRequest);
    }

    public void CountDown() {
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ReadNFC.this.edt_name.setText("");
                ReadNFC.this.textView.setText("Data Sent");
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        TextView textView = ReadNFC.this.TimerText;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exiting in : ");
                        sb.append(millisUntilFinished / 1000);
                        textView.setText(sb.toString());
                    }

                    public void onFinish() {
                        ReadNFC.this.finish();
                    }
                }
                        .start();
            }
        }.start();
    }

    public void CountDownOff() {
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ReadNFC.this.edt_name.setText("");
                ReadNFC.this.textView.setText("Data Save To Database");
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        TextView textView = ReadNFC.this.TimerText;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exiting in : ");
                        sb.append(millisUntilFinished / 1000);
                        textView.setText(sb.toString());
                    }

                    public void onFinish() {
                        ReadNFC.this.finish();
                    }
                }.start();
            }
        }.start();
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if ("android.nfc.action.TAG_DISCOVERED".equals(action) || "android.nfc.action.TECH_DISCOVERED".equals(action) || "android.nfc.action.NDEF_DISCOVERED".equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
            getWindow().setFlags(6816768, 6816768);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs != null && msgs.length != 0) {
            byte[] payload = msgs[0].getRecords()[0].getPayload();
            int languageCodeLength = payload[0] & 51;
            try {
                this.text = new String(payload, languageCodeLength + 1, (payload.length - languageCodeLength) - 1, (payload[0] & 128) == 0 ? "UTF-8" : "UTF-16");
            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncoding", e.toString());
            }
            this.edt_name.setText(this.text);
            SharedPreferences.Editor editor = getSharedPreferences("MyData", 0).edit();
            editor.putString("key", this.text);
            editor.putLong("ExpiredDate", this.time + TimeUnit.SECONDS.toMillis(60));
            editor.apply();
        }
    }

    public void textWatcher() {
        if (this.edt_name.getText().length() <= 8) {
            insert_dataKey();
            CountDown();
        } else if (getSharedPreferences("MyData", 0).getString("key", "").equalsIgnoreCase(this.text)) {
            SharedPreferences sharedpreferences = getSharedPreferences("MyData", 0);
            if (sharedpreferences.getLong("ExpiredDate", -1) < System.currentTimeMillis()) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if (isConnectingToInternet(this)) {
                    editor.clear();
                    editor.apply();
                    insert_data();
                    CountDown();
                    return;
                }
                this.chck_ID = this.edt_name.getText().toString();
                this.date_ID = this.Date;
                saveData();
                editor.clear();
                editor.apply();
                CountDownOff();
                return;
            }
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    ReadNFC.this.textView.setText("ID match with the previous tags");
                    ReadNFC.this.toastMessage("Please scan another tags!");
                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            TextView textView = ReadNFC.this.TimerText;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Exiting in : ");
                            sb.append(millisUntilFinished / 1000);
                            textView.setText(sb.toString());
                        }

                        public void onFinish() {
                            ReadNFC.this.finish();
                        }
                    }.start();
                }
            }
                    .start();
        } else if (isConnectingToInternet(this)) {
            insert_data();
            CountDown();
        } else {
            this.chck_ID = this.edt_name.getText().toString();
            this.date_ID = this.Date;
            saveData();
            CountDownOff();
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void toastMessage(String message) {
        Toast.makeText(this, message, 0).show();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if ("android.nfc.action.TAG_DISCOVERED".equals(intent.getAction())) {
            this.myTag = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        if (isConnectingToInternet(this)) {
            this.Internet_validation.setText("ONLINE");
            this.floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(-16711936));
        } else {
            this.floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(SupportMenu.CATEGORY_MASK));
            this.Internet_validation.setText("OFFLINE");
        }
        super.onResume();
    }

    private void saveData() {
        this.dataBase = this.mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_CHCKPOINID, this.chck_ID);
        values.put(DbHelper.KEY_DATEID, this.date_ID);
        if (!this.isUpdate) {
            this.dataBase.insert(DbHelper.TABLE_NAME, null, values);
        }
        this.dataBase.close();
    }

    public void insert_dataKey() {
       /* ((AppConfig.insertKey) new Retrofit.Builder().setEndpoint(this.BASE_URL).build().create(AppConfig.insertKey.class)).insertDataKey(this.edt_name.getText().toString(), this.imei, new Callback<Response>() {
            public void success(Response result, Response response) {
                try {
                    String resp = new BufferedReader(new InputStreamReader(result.getBody().mo9235in())).readLine();
                    StringBuilder sb = new StringBuilder();
                    sb.append("TEST ");
                    sb.append(resp);
                    Log.d("success", sb.toString());
                    new JSONObject(resp).getInt("success");
                } catch (IOException e) {
                    Log.d("Exception", e.toString());
                } catch (JSONException e2) {
                    Log.d("JsonException", e2.toString());
                }
            }

            public void failure(RetrofitError error) {
                Toast.makeText(ReadNFC.this, error.toString(), 1).show();
            }
        });*/

        RequestInterface apiCalling = ApiClient.getClient().create(RequestInterface.class);

        Call<ResponseBody> call = apiCalling.insertDataKey(this.edt_name.getText().toString(), this.imei);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        //Toast.makeText(ReadNFC.this, "" + json, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ReadNFC.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReadNFC.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

       /* String url="https://creator.zoho.com/api/accountsperthsecurityservices/json/mobileapp/form/Scan_Checkpoints/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap&Scan_Checkpoint1="+this.edt_name.getText().toString()+"&IMEI="+imei;
        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            //  StringRequest req2;
            @Override
            public void onResponse(String response) {

                Toast.makeText(context, ""+response, Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




                Toast.makeText(ReadNFC.this, "Error Try again Later" + error.toString(), Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(context,ThankyouActivity.class);
                //            startActivity(intent);

                String message = null;
                if (error instanceof NetworkError) {

                    message = "Due to bad internet Connection e";
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    // Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    // Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
*/
    }

    public void insert_data() {
        /*((AppConfig.insert) new Retrofit.Builder().setEndpoint(this.BASE_URL).build().create(AppConfig.insert.class)).insertData(this.edt_name.getText().toString(), this.imei, new Callback<Response>() {
            public void success(Response result, Response response) {
                try {
                    String resp = new BufferedReader(new InputStreamReader(result.getBody().mo9235in())).readLine();
                    StringBuilder sb = new StringBuilder();
                    sb.append("TEST ");
                    sb.append(resp);
                    Log.d("success", sb.toString());
                    new JSONObject(resp).getInt("success");
                } catch (IOException e) {
                    Log.d("Exception", e.toString());
                } catch (JSONException e2) {
                    Log.d("JsonException", e2.toString());
                }
            }

            public void failure(RetrofitError error) {
                Toast.makeText(ReadNFC.this, error.toString(), 1).show();
            }
        });*/
     /*   String url="https://creator.zoho.com/api/accountsperthsecurityservices/json/mobileapp/form/Scan_Checkpoints/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap&Scan_Checkpoint1"+this.edt_name.getText().toString()+"&IMEI="+imei;
        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            //  StringRequest req2;
            @Override
            public void onResponse(String response) {

                Toast.makeText(context, ""+response, Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



\
                Toast.makeText(ReadNFC.this, "Error Try again Later" + error.toString(), Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(context,ThankyouActivity.class);
                //            startActivity(intent);

                String message = null;
                if (error instanceof NetworkError) {

                    message = "Due to bad internet Connection e";
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    //Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    // Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    // Save();
                    Toast.makeText(ReadNFC.this, "" + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
*/

        RequestInterface apiCalling = ApiClient.getClient().create(RequestInterface.class);

        Call<ResponseBody> call = apiCalling.insertData(ReadNFC.this.edt_name.getText().toString(), this.imei, edtchckName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        //  Toast.makeText(ReadNFC.this, "" + json, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ReadNFC.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReadNFC.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* @Override
    public void onCreateOptionsMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_read_nfc, menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reader_list_nfc:
                Intent intent = new Intent(ReadNFC.this, ScanNfcTagActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_nfc:
                Intent intent1 = new Intent(ReadNFC.this, AuthActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
