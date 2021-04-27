package com.example.pssmobile.ui.login.writer;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.pssmobile.R;
import com.example.pssmobile.data.model.AddCheckpointRequest;
import com.example.pssmobile.data.model.BureauListResponse;
import com.example.pssmobile.data.model.SiteCheckpointResponse;
import com.example.pssmobile.databinding.ActivityAddCheckPointBinding;
import com.example.pssmobile.databinding.ActivityAddSiteBinding;
import com.example.pssmobile.retrofit.ApiClientWriter;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.pssmobile.ui.login.writer.Utility.encodeImageToBase64;

public class AddCheckPointActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddCheckPointBinding binding;
    ArrayList<SiteCheckpointResponse.SiteCheckpoint> siteCheckpointArrayList = new ArrayList<>();
    ArrayList<String> siteCheckPointName = new ArrayList<>();
    String siteId ="";
    public String base64Image = "";
    private static final int PICKFILE_RESULT_CODE = 1;
    static final int REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCheckPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSubmit.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.btnAddImage.setOnClickListener(this);
        binding.autoCompleteSite.setOnClickListener(this);
        binding.rlAutoComplete.setOnClickListener(this);
        Utility.showLoader(this,false);
        callCheckpointList();
        ArrayAdapter myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, siteCheckPointName);
        binding.autoCompleteSite.setAdapter(myAdapter);


    }

    private void callCheckpointList() {
        final Call<SiteCheckpointResponse> call = ApiClientWriter.loadInterfaceWriter().getSiteCheckpointList();
        call.enqueue(new Callback<SiteCheckpointResponse>() {
            @Override
            public void onResponse(Call<SiteCheckpointResponse> call, Response<SiteCheckpointResponse> response) {
                if (response.isSuccessful()){
                    for (int i=0; i<response.body().detail.siteCheckpoint.size(); i++){
                        siteCheckpointArrayList.add(response.body().detail.siteCheckpoint.get(i));
                        siteCheckPointName.add(response.body().detail.siteCheckpoint.get(i).pSSJobID+", " +response.body().detail.siteCheckpoint.get(i).jobName);
                    }
                   // setSpinnerValue(siteCheckpointArrayList, siteCheckPointName);
                    Utility.hideLoader();
                }
            }
            @Override
            public void onFailure(Call<SiteCheckpointResponse> call, Throwable t) {
            }
        });
    }

   /* private void setSpinnerValue(ArrayList<SiteCheckpointResponse.SiteCheckpoint> spinnerValue, ArrayList<String> siteCheckPointName) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddCheckPointActivity.this,
                android.R.layout.simple_spinner_item, siteCheckPointName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spSite.setAdapter(arrayAdapter);
        binding.spSite.setSelection(0);


        binding.spSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0; i<spinnerValue.size();i++){
                    if (siteCheckPointName.get(position).contains(spinnerValue.get(i).jobName)){
                        siteId = spinnerValue.get(i).id;
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
*/
    private void submitSiteCheckpoint(){
        if (siteId.equals("")){
            Toast.makeText(this, "Please select the site", Toast.LENGTH_LONG).show();
        }
        if (validationCheck() && !siteId.equals("")){
            Utility.showLoader(this,false);
            AddCheckpointRequest request = new AddCheckpointRequest();
            request.selectJob = siteId;
            request.checkpointID = binding.etCheckPointId.getText().toString().trim();
            request.checkpointName = binding.etCheckpointName.getText().toString().trim();
            request.checkpointDescription = binding.etCheckpointDescription.getText().toString().trim();
            request.active = binding.checkBoxIsActive.isChecked();
            request.checkpointPicture = base64Image;
            request.mobileInput = binding.etMobileInput.getText().toString().trim();
            request.howManyScan = Integer.parseInt(binding.etScannedNoOfTimes.getText().toString().trim());

            Call<ResponseBody> callSiteCheckpoint = ApiClientWriter.loadInterfaceWriter().addCheckpoint(request);
            callSiteCheckpoint.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        try {
                            Toast.makeText(AddCheckPointActivity.this, "SitCheckpoint added successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddCheckPointActivity.this, LocationList.class);
                            startActivity(intent);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utility.hideLoader();
                    Toast.makeText(AddCheckPointActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_image){
            getImage();

        }
        else if (v.getId() == R.id.btn_submit){
            validationCheck();
            submitSiteCheckpoint();
        }
        else if (v.getId() == R.id.btn_reset){
            resetFields();
        }
        else if (v.getId() == R.id.autoComplete_site){
            binding.autoCompleteSite.showDropDown();
        }

    }

    private boolean validationCheck() {
        if (binding.autoCompleteSite.getText().toString().equals("")){
            Toast.makeText(this, "Please select Site", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (binding.etCheckpointName.getText().toString().equals("")){
            Toast.makeText(this, "Please enter checkpoint name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (binding.etCheckpointDescription.getText().toString().equals("")){
            Toast.makeText(this, "Please enter checkpoint description",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (binding.etCheckPointId.getText().toString().equals("")){
            Toast.makeText(this,"Please enter checkppoint id",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (binding.etScannedNoOfTimes.getText().toString().equals("")){
            Toast.makeText(this,"Please enter no. of time of Scanning", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (base64Image.equals("")){
            Toast.makeText(this,"Please select the image", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (binding.etMobileInput.getText().toString().equals("")){
            Toast.makeText(this,"Please enter mobile input", Toast.LENGTH_LONG).show();
            return false;
        }

        for (int i=0; i< siteCheckpointArrayList.size(); i++){
            if (binding.autoCompleteSite.getText().toString().contains(siteCheckpointArrayList.get(i).jobName)
                    && binding.autoCompleteSite.getText().toString().contains(siteCheckpointArrayList.get(i).pSSJobID)){
                siteId = siteCheckpointArrayList.get(i).id;
                return true;
            }
        }

        return true;
    }

    private void getImage() {
        if (ContextCompat.checkSelfPermission(AddCheckPointActivity.this, Manifest.permission.CAMERA) + (ContextCompat.checkSelfPermission(AddCheckPointActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED)


            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCheckPointActivity.this, Manifest.permission.CAMERA) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(AddCheckPointActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddCheckPointActivity.this);
                builder.setTitle("Grant Permission");
                builder.setMessage("Camera read contact and read external storage ");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(AddCheckPointActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddCheckPointActivity.this, "Please allow  permission to upload file.", Toast.LENGTH_SHORT).show();
                        showSettingsDialog();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                ActivityCompat.requestPermissions(AddCheckPointActivity.this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            } else {
            ImagePicker.Companion.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .saveDir(new File(Environment.getExternalStorageDirectory(), "ImagePicker"))
                    .start();
        }
    }

    private void resetFields() {
        binding.etCheckPointId.setText("");
        binding.etCheckpointName.setText("");
        binding.etCheckpointDescription.setText("");
        binding.etMobileInput.setText("");
        binding.etScannedNoOfTimes.setText("");
        binding.checkBoxIsActive.setChecked(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            //userInfoBinding.ivUserImage.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);
            String filename=filePath.substring(filePath.lastIndexOf("/")+1);
            String encodedImage = encodeImageToBase64(filePath);
            base64Image = encodedImage;
            //isProfileUpdated = true;
            Log.d("upload", " ====> " + encodedImage);
            binding.etCheckpointPicture.setText(filename);
            Toast.makeText(this, "image saved",Toast.LENGTH_LONG).show();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCheckPointActivity.this);
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
        Uri uri = Uri.fromParts("package", AddCheckPointActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @ NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] + grantResults[1] + grantResults[2]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddCheckPointActivity.this, "Permission Granted....", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AddCheckPointActivity.this, "Permission Denied....", Toast.LENGTH_SHORT).show();
                showSettingsDialog();
            }
        }
    }
}