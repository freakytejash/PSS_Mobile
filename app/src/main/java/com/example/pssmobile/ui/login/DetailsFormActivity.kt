package com.example.pssmobile.ui.login

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pssmobile.BuildConfig
import com.example.pssmobile.R
import com.example.pssmobile.adapter.ImageGridAdapter
import com.example.pssmobile.utils.FileCompressor
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailsFormActivity : AppCompatActivity() {
    private lateinit var mPhotoFile: File
    private lateinit var mCompressor: FileCompressor
    private lateinit var btn_choseImage: Button
    private lateinit var rv_imageList: RecyclerView
    private lateinit var imageGridAdapter: ImageGridAdapter
    private var imageFileList: ArrayList<File> = ArrayList()

    companion object {
        const val REQUEST_TAKE_PHOTO: Int = 1;
        const val REQUEST_GALLERY_PHOTO: Int = 2;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_form)
        mCompressor = FileCompressor(this)
        btn_choseImage = findViewById(R.id.btn_choseImage)
        rv_imageList = findViewById(R.id.gv_SelectedImages)

        //setImageGridAdapter(imageFileList)

        btn_choseImage.setOnClickListener {
            if (imageFileList.size >= 4) {
                Toast.makeText(this, "You have already selected 4 images", Toast.LENGTH_SHORT)
                    .show()
            }else{
                selectImage()
            }

        }
    }

    private fun setImageGridAdapter(imageList: ArrayList<File>){
        rv_imageList.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        imageGridAdapter = ImageGridAdapter(this, imageList)
        rv_imageList.adapter = imageGridAdapter
        rv_imageList.setHasFixedSize(true)
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            "Take Photo", "Choose from Gallery", "Cancel"
        )
        val builder = AlertDialog.Builder(this@DetailsFormActivity)
        builder.setTitle("Choose Image")
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                requestStoragePermission(true)
            } else if (items[item] == "Choose from Gallery") {
                requestStoragePermission(false)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun requestStoragePermission(isCamera: Boolean) {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            dispatchTakePictureIntent()
                        } else {
                            dispatchGalleryIntent()
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { error: DexterError? ->
                Toast.makeText(
                    applicationContext,
                    "Error occurred! ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }

    /**
     * Capture image from camera
     */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@DetailsFormActivity, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                    takePictureIntent,
                    REQUEST_TAKE_PHOTO
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    /**
     * Select image fro gallery
     */
    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            pickPhoto,
            REQUEST_GALLERY_PHOTO
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                    try {
                        mPhotoFile = mCompressor.compressToFile(mPhotoFile)
                        imageFileList.add(mPhotoFile)
                        //imageGridAdapter.updateImageList(imageFileList as ArrayList<File>)
                        setImageGridAdapter(imageFileList)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                /*Glide.with(this@DetailsFormActivity).load(mPhotoFile).apply(
                    RequestOptions().centerCrop()
                        .placeholder(R.drawable.ic_photo)
                ).into(imageView)*/
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {

                val clipData: ClipData? = data?.clipData

                if (clipData?.itemCount!! > 4) {
                    Toast.makeText(this, "Please select maximum 4 images only", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if ((imageFileList.size + clipData.itemCount) > 4) {
                        Toast.makeText(this, "Please select maximum 4 images only", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        for (i in 0 until clipData.itemCount) {
                            try {
                                val imageUri = mCompressor.compressToFile(File(getRealPathFromUri(clipData.getItemAt(i).uri)))
                                imageFileList.add(imageUri)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        setImageGridAdapter(imageFileList)
                        //imageGridAdapter.updateImageList(imageFileList)
                    }
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }
}