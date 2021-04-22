package com.example.pssmobile.ui.login.writer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pssmobile.R;
import com.example.pssmobile.data.model.AddSiteRequest;
import com.example.pssmobile.data.model.BureauListResponse;
import com.example.pssmobile.databinding.ActivityAddSiteBinding;
import com.example.pssmobile.retrofit.ApiClient;
import com.example.pssmobile.retrofit.ApiClientWriter;
import com.example.pssmobile.retrofit.RequestInterface;
import com.example.pssmobile.ui.login.MainActivity;
import com.example.pssmobile.ui.login.reader.ReadNFC;
import com.example.pssmobile.ui.login.reader.ZohoViewModel;
import com.example.pssmobile.ui.login.writer.adapter.GeoCodeLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddSiteActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddSiteBinding binding;
    ZohoViewModel viewModel;
    RadioButton  invoiceRadioBtn;
    ArrayList<String> allocatejobType = new ArrayList<>();
    public ArrayList<BureauListResponse.BureauList> bureauListArrayList = new ArrayList<>();
    public ArrayList<AddSiteRequest.DocumentsAttached> documentsAttachedArrayList = new ArrayList<>();
    public AddSiteRequest.DocumentsAttached documentDetail;
    public AddSiteRequest.DocumentsAttached documentDetailReplace;
    public ArrayList<String> bureauName= new ArrayList<>();
    String bureauEmail ="";
    String bureauId ="";
    public AddSiteRequest.Sitebriefing siteUrl;

    private static final int PICKFILE_RESULT_CODE = 1;
    static final int REQUEST_CODE = 0;
    String encodeFileToBase64Binary="";
    String filePath ="";
    public String docFileName ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSiteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnUpdate.setOnClickListener(this);
        Utility.showLoader(this,false);
        bureauListCall();
        binding.rvFilePath.setLayoutManager(new LinearLayoutManager(AddSiteActivity.this));
        binding.btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddSiteActivity.this, Manifest.permission.CAMERA) + (ContextCompat.checkSelfPermission(AddSiteActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED)


                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddSiteActivity.this, Manifest.permission.CAMERA) ||
                            (ActivityCompat.shouldShowRequestPermissionRationale(AddSiteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSiteActivity.this);
                        builder.setTitle("Grant Permission");
                        builder.setMessage("Camera read contact and read external storage ");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(AddSiteActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AddSiteActivity.this, "Please allow  permission to upload file.", Toast.LENGTH_SHORT).show();
                                showSettingsDialog();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else {
                        ActivityCompat.requestPermissions(AddSiteActivity.this, new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                    }
                else {
                    String[] mimetypes = {"image/*", "application/pdf","text/plain", "application/msword"};
                    Intent intent = new Intent();
                    //intent.setType("application/pdf|text/plain|image/*|application/msword");
                    intent.setType("image/*, application/pdf,text/plain, application/msword");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent,
                            getString(R.string.allowed_additional_file_formats_txt)),
                            PICKFILE_RESULT_CODE);
                    //another way 
                  /*  Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    //isKitKat = true;
                    startActivityForResult(Intent.createChooser(intent, "Select file"), 1);*/
                }


            }
        });

        binding.etSuburb.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(AddSiteActivity.this, binding.etSuburb.getText(), Toast.LENGTH_LONG).show();
                    String address = binding.etSiteAddress.getText().toString()+" " + binding.etSuburb.getText().toString();
                    callButton(address);
                    Utility.hideLoader();
                    return true;
                }
                return false;
            }
        });


    }
    public void callRecyclerView(){
        binding.rvFilePath.setAdapter(new AddFileDetailAdapter(AddSiteActivity.this,documentsAttachedArrayList,
                new AddFileDetailAdapter.OnItemClickListener() {
            @Override
            public void onEditTextClickListener(int position) {
/*                EditText doc = findViewById(R.id.et_file_name_item);
                doc.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            String docName = doc.getText().toString().trim();
                            doc.setFocusable(false);
                            doc.setEnabled(false);
                            documentDetail = documentsAttachedArrayList.get(position);
                            documentDetailReplace = new AddSiteRequest.DocumentsAttached(docName,documentDetail.fileAttachment,
                                    documentDetail.documentUrl,documentDetail.fileName);
                            documentsAttachedArrayList.set(position, documentDetailReplace);

                            return true;
                        }
                        return false;
                    }
                });*/

            }

            @Override
            public void onDeletePositionListener(int position) {
                new AlertDialog.Builder(AddSiteActivity.this)
                        .setIcon(R.drawable.alert)
                        .setMessage("")
                        .setTitle("Attempt to Delete A File")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    documentsAttachedArrayList.remove(position);// only rempve fro list---
                                    callRecyclerView();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(AddSiteActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                }
                        ).show();
            }

            @Override
            public void onAddButtonClickListener(int position, String docName) {

                documentDetail = documentsAttachedArrayList.get(position);
                documentDetailReplace = new AddSiteRequest.DocumentsAttached(docName,documentDetail.fileAttachment,
                        documentDetail.documentUrl,documentDetail.fileName);
                documentsAttachedArrayList.set(position, documentDetailReplace);
            }


/*            public void onAddButtonClickListener(int position) {
                EditText doc = findViewById(R.id.et_file_name_item);
                String docName = doc.getText().toString().trim();

            }*/
                }));

    }

    public void callButton(String address1){
        Utility.showLoader(this, false);
        String address = address1;
        GeoCodeLocation locationAddress = new GeoCodeLocation();
        locationAddress.getAddressFromLocation(address, getApplicationContext(), new
                GeoCoderHandler());
    }

    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            String latitude;
            String longitude;
            String postCode;
            Double latDouble, longDouble;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    latitude = bundle.getString("latitude");
                    latDouble = Double.parseDouble(latitude);
                    latitude = String.format("%.6f", latDouble);
                    longitude = bundle.getString("longitude");
                    longDouble = Double.parseDouble(longitude);
                    longitude =String.format("%.6f", longDouble);
                    postCode = bundle.getString("postCode");
                    break;
                default:
                    locationAddress = null;
                    latitude = null;
                    longitude = null;
                    postCode = null;
            }

            if (postCode!=null){
                binding.etPostCode.setText(postCode);
                /*binding.etPostCode.setFocusable(false);
                binding.etPostCode.setEnabled(false);*/
            }
            if (latitude!=null){
                binding.etLatitude.setText(latitude);
                /*binding.etLatitude.setFocusable(false);
                binding.etLatitude.setEnabled(false);*/
            }
            if (longitude!=null){
                binding.etLongitude.setText(longitude);
                /*binding.etLongitude.setFocusable(false);
                binding.etLongitude.setEnabled(false);*/
            }

        }
    }



    private void bureauListCall() {

        final Call<BureauListResponse> call = ApiClientWriter.loadInterfaceWriter().getBureauList();
        call.enqueue(new Callback<BureauListResponse>() {
            @Override
            public void onResponse(Call<BureauListResponse> call, Response<BureauListResponse> response) {

                for (int i=0; i<response.body().detail.data.size(); i++){
                    bureauListArrayList.add(response.body().detail.data.get(i));
                    bureauName.add(response.body().detail.data.get(i).bureau);
                }
                setSpinnerValue(bureauListArrayList, bureauName);
                Utility.hideLoader();
            }

            @Override
            public void onFailure(Call<BureauListResponse> call, Throwable t) {

            }
        });
    }

    private void setSpinnerValue(ArrayList<BureauListResponse.BureauList> spinnerValue, ArrayList<String> bureauName) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddSiteActivity.this,
                android.R.layout.simple_spinner_item, bureauName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spBureau.setAdapter(arrayAdapter);
        binding.spBureau.setSelection(0);


        binding.spBureau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               for (int i=0; i<spinnerValue.size();i++){
                   if (bureauName.get(position).equalsIgnoreCase(spinnerValue.get(i).bureau)){
                       bureauId = spinnerValue.get(i).id;
                       bureauEmail = spinnerValue.get(i).email;
                       binding.etContactEmail1.setText(bureauEmail);

                   }
               }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void addSite(){
        Utility.showLoader(this,false);
        int invoiceScheduleId = binding.radioGroupInvoiceSchedule.getCheckedRadioButtonId();
        AddSiteRequest request = new AddSiteRequest();
        request.customers12=/*"3354762000052136171"*/bureauId;
        request.siteName = binding.etSiteName.getText().toString().trim();
        request.siteAddress = binding.etSiteAddress.getText().toString().trim();
        request.suburb =/*"Balcatta"*/ binding.etSuburb.getText().toString().trim();
        request.postCode = binding.etPostCode.getText().toString().trim();

        request.contactPerson = binding.etContact1.getText().toString().trim();
        request.phoneNo = binding.etContactNo1.getText().toString().trim();
        request.email2 = binding.etContactEmail1.getText().toString().trim();

        request.allocatedJobType = allocatejobType;
        request.active= binding.checkBoxIsActive.isChecked();
        invoiceRadioBtn = findViewById(invoiceScheduleId);
        request.invoicingSchedule = invoiceRadioBtn.getText().toString();
        request.sitebriefing =new AddSiteRequest.Sitebriefing(binding.etSiteBriefing.getText().toString().trim());

        request.keys = binding.etKeysHeld.getText().toString().trim();
        request.keyNumber = binding.etPssKey.getText().toString().trim();
        request.siteref = binding.etSiteReference.getText().toString().trim();
        request.notes = binding.etGeneralNotes.getText().toString().trim();
        request.operationsNote = binding.etOperationsNotes.getText().toString().trim();
        request.clampInfo = binding.etCarClampInfo.getText().toString().trim();
        request.ccheck = binding.cbCarClampBox.isChecked();

        request.documentsAttached = documentsAttachedArrayList;

        request._long =/*"115.822731" */  binding.etLongitude.getText().toString().trim();
        request.lat = /*"-31.859656"*/binding.etLatitude.getText().toString().trim();
        request.alarmResponseRate = binding.etAlarmResponse.getText().toString().trim();
        request.mobilePatrolRate = binding.etMobilePetrolRate.getText().toString().trim();
        request.additionalTimeAfter30MinOnSite = binding.etAdditionalTime.getText().toString().trim();
/*
        request.email = binding.etContactEmail1.getText().toString().trim();


        request.addedUser=binding.etContact1.getText().toString().trim();
        request.siteID = binding.etSiteReference.getText().toString().trim();*/


      //  RequestInterface apiCalling = ApiClient.getWriter().create(RequestInterface.class);
        Call<ResponseBody> call = ApiClientWriter.loadInterfaceWriter().addSite(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Utility.hideLoader();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        //Toast.makeText(ReadNFC.this, "" + json, Toast.LENGTH_SHORT).show();

                        Toast.makeText(AddSiteActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        Toast.makeText(AddSiteActivity.this, "Site Added Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddSiteActivity.this, LocationList.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddSiteActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddSiteActivity.this, LocationList.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.hideLoader();
                Toast.makeText(AddSiteActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(AddSiteActivity.this, LocationList.class);
                startActivity(intent);*/
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_update){
           if (validationTest()){
               allocatejobType.clear();
               if (binding.cbAlarmResponse.isChecked()){
                   allocatejobType.add(binding.cbAlarmResponse.getText().toString().trim());
               }
               if (binding.cbCarClamp.isChecked()){
                   allocatejobType.add(binding.cbCarClamp.getText().toString().trim());
               }
               if (binding.cbRandomPetrols.isChecked()){
                   allocatejobType.add(binding.cbRandomPetrols.getText().toString().trim());
               }
               if (binding.cbRequireLogBook.isChecked()){
                   allocatejobType.add(binding.cbRequireLogBook.getText().toString().trim());
               }
               if (binding.cbStaticGuard.isChecked()){
                   allocatejobType.add(binding.cbStaticGuard.getText().toString().trim());
               }
               if (binding.cbIGuard.isChecked()){
                   allocatejobType.add(binding.cbIGuard.getText().toString().trim());
               }
               addSite();
           }
           else {
               Toast.makeText(this, "Please Enter Mandatory fields", Toast.LENGTH_SHORT).show();
           }
        }
    }

    private boolean validationTest(){
        if (binding.etSiteName.getText().equals(null)){
            Toast.makeText(this,"Please enter Site Name",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etSiteAddress.getText().equals(null)){
            Toast.makeText(this,"Please enter Site Address",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etSuburb.getText().equals(null)){
            Toast.makeText(this,"Please enter Suburb",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etPostCode.getText().equals(null)){
            Toast.makeText(this,"Please enter PostCode",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etContactEmail1.getText().equals(null)){
            Toast.makeText(this,"Please enter EmailId",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etSiteBriefing.getText().equals(null)){
            Toast.makeText(this,"Please enter Site Briefing",Toast.LENGTH_LONG).show();
            return false;
        }
        if (binding.etSuburb.getText().equals(null)){
            Toast.makeText(this,"Please enter Suburb",Toast.LENGTH_LONG).show();
            return false;
        }
       /* if(!Utility.isEmailValid(binding.etContactEmail1.getText().toString())){
            return false;
        }*/
       /* if (binding.radioGroupInvoiceSchedule){
            Toast.makeText(this,"Please enter Suburb",Toast.LENGTH_LONG).show();
            return false;
        }*/
        return true;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSiteActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", AddSiteActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] + grantResults[1] + grantResults[2]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddSiteActivity.this, "Permission Granted....", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AddSiteActivity.this, "Permission Denied....", Toast.LENGTH_SHORT).show();
                showSettingsDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    String file1=data.getData().getPath();
                    FilePath= FilePath.replaceAll("external_files/","");
                    String root = Environment.getExternalStorageDirectory().toString();
                    Uri uri = data.getData();
                    File myDir = new File(Environment.getExternalStorageDirectory() + FilePath);
                    docFileName = myDir.getName();

                    File myFile = new File(FilePath);
                    //myFile = new File(myFile.getPath());
                    File dirAsFile = myFile.getParentFile();
                    String encodeFileToBase64Binary = null;
                    try {
                        encodeFileToBase64Binary = getBase64String(myDir, uri);

                    } finally {

                        if (encodeFileToBase64Binary.equalsIgnoreCase("")){
                            Toast.makeText(this, "Unable to add document... please try again", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(this,"Document added",Toast.LENGTH_LONG).show();
                            documentDetail = new AddSiteRequest.DocumentsAttached("","",encodeFileToBase64Binary,docFileName);
                            documentsAttachedArrayList.add(documentDetail);
                        }

                        callRecyclerView();
                        //base64File.setText(encodeFileToBase64Binary);
                        //filePAth.setText(FilePath);
                    }

                }
                break;

        }
    }




    public String getBase64String(File fileName, Uri uri)
    {
        if (fileName == null) {
            return "";
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            /* Uri uri = Uri.parse(fileName.getAbsolutePath());*/
            if (isSelectedFileImage(String.valueOf(fileName))) {
                String path = getRealPathFromURI(this,uri, this.getContentResolver());
                docFileName = path.substring(path.lastIndexOf("/")+1);


                Bitmap bm = BitmapFactory.decodeFile(path);
                if (bm != null) {
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteArrayImage = baos.toByteArray();
                    return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                }
                return "";
            } else {
                InputStream inputStream = getContentResolver().openInputStream(uri);//You can get an inputStream using any IO API
                //inputStream = new FileInputStream(fileName);
                byte[] buffer = new byte[10240];
                Base64OutputStream output64 = new Base64OutputStream(baos, Base64.NO_WRAP);
                int bytesRead = inputStream.read(buffer);
                while (bytesRead != -1) {
                    output64.write(buffer, 0, bytesRead);
                    bytesRead = inputStream.read(buffer);
                }

                output64.close();
                Log.e("Encode", baos.toString());
                return baos.toString();
            }

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            return "";
        } catch (OutOfMemoryError error) {
            Log.e("error", error.getMessage());
            return "";
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri, ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

    }

    private boolean isSelectedFileImage(String path) {
        String mimeType =path;
        return mimeType != null && mimeType.contains("image");
    }
}