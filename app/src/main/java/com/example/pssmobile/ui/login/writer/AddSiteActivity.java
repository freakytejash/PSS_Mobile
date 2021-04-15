package com.example.pssmobile.ui.login.writer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.pssmobile.R;
import com.example.pssmobile.data.model.AddSiteRequest;
import com.example.pssmobile.databinding.ActivityAddSiteBinding;
import com.example.pssmobile.retrofit.ApiClient;
import com.example.pssmobile.retrofit.RequestInterface;
import com.example.pssmobile.ui.login.reader.ReadNFC;
import com.example.pssmobile.ui.login.reader.ZohoViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class AddSiteActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddSiteBinding binding;
    ZohoViewModel viewModel;
    RadioButton  invoiceRadioBtn;
    private static final int PICKFILE_RESULT_CODE = 1;
    String encodeFileToBase64Binary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSiteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // viewModel = ViewModelProviders.of(AddSiteActivity.this).get(ZohoViewModel.class);
        binding.btnUpdate.setOnClickListener(this);
        binding.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String filePath = data.getData().getPath();
                    binding.tvFilePath.setText(filePath);
                    File dir = Environment.getExternalStorageDirectory();
                    File yourFile = new File(dir, filePath );
                   /* encodeFileToBase64Binary = convertFileToByteArray(yourFile);
                    Log.e("file",encodeFileToBase64Binary);*/

                }
                break;

        }
    }

/*    public static String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }*/

   /* private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        Log.e("file",encoded);
        Log.d("file",encoded);
        Log.i("file",encoded);
        Log.v("file",encoded);

        return encoded;
    }*/



    public void addSite(){
        AddSiteRequest request = new AddSiteRequest();
        request.siteName = binding.etSiteName.getText().toString().trim();
        request.siteAddress = binding.etSiteAddress.getText().toString().trim();
        request.suburb = binding.etSuburb.getText().toString().trim();
        request.postCode = binding.etPostCode.getText().toString().trim();
        int invoiceScheduleId = binding.radioGroupInvoiceSchedule.getCheckedRadioButtonId();
        invoiceRadioBtn = findViewById(invoiceScheduleId);
        request.invoicingSchedule = invoiceRadioBtn.getText().toString();
        request.sitebriefing.url = binding.etSiteBriefing.getText().toString().trim();
        request.active= binding.checkBoxIsActive.isChecked();
        request.keys = binding.etPssKey.getText().toString().trim();
        request.customers12= binding.etContact1.getText().toString().trim();
        request.phoneNo= binding.etContactNo1.getText().toString().trim();
        request.email = binding.etContactEmail1.toString().trim();
        request._long = binding.etLongitude.toString().trim();
        request.lat = binding.etLatitude.toString().trim();


        RequestInterface apiCalling = ApiClient.getAddSiteClient().create(RequestInterface.class);
        Call<ResponseBody> call = apiCalling.addSite(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        Log.d("AddsiteActivity","Response: " + json.toString());
                        //Toast.makeText(ReadNFC.this, "" + json, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddSiteActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddSiteActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update){
            addSite();
        }
    }
}