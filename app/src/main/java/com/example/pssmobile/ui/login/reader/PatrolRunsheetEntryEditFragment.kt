package com.example.pssmobile.ui.login.reader

import android.Manifest
import android.app.AlertDialog
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pssmobile.BuildConfig
import com.example.pssmobile.R
import com.example.pssmobile.adapter.ImageGridAdapter
import com.example.pssmobile.databinding.FragmentPatrolRunsheetDetailsBinding
import com.example.pssmobile.databinding.FragmentPatrolRunsheetEntryEditBinding
import com.example.pssmobile.repository.ZohoRepository
import com.example.pssmobile.retrofit.ZohoApi
import com.example.pssmobile.ui.login.DetailsFormActivity
import com.example.pssmobile.ui.login.base.BaseFragment
import com.example.pssmobile.utils.FileCompressor
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_patrol_runsheet_entry_edit.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PatrolRunsheetEntryEditFragment : BaseFragment<ZohoViewModel, FragmentPatrolRunsheetEntryEditBinding, ZohoRepository>() {

    private lateinit var mPhotoFile: File
    private lateinit var mCompressor: FileCompressor
    private lateinit var imageGridAdapter: ImageGridAdapter
    private var imageFileList: ArrayList<File> = ArrayList()
    val args: PatrolRunsheetEntryEditFragmentArgs by navArgs()

    companion object {
        const val REQUEST_TAKE_PHOTO: Int = 1;
        const val REQUEST_GALLERY_PHOTO: Int = 2;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCompressor = FileCompressor(requireContext())
        val model = args.dataModel
        Log.d("App","Data model in patrol edit: " + model.toString())

        binding.etJobnamelocation.setText(model.select_a_job1.display_value)
        binding.etLocation.setText(model.location1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            et_jobdescription.setText(Html.fromHtml(model.job_description, HtmlCompat.FROM_HTML_MODE_LEGACY))
        } else {
            et_jobdescription.setText(Html.fromHtml(model.job_description))
        }
        binding.etStartdatetime.setText(model.start_date_time)
        binding.etEnddatetime.setText(model.end_date_time)
        binding.etJobtype.setText(model.job_type)
        binding.etJobcompletiondatetime.setText(model.date_time_job_completed)
        binding.etPatrolofficer.setText(model.patrol_Officer)

        binding.etPicturesection.setOnClickListener {
            if (imageFileList.size >= 4) {
                Toast.makeText(requireContext(), "You have already selected 4 images", Toast.LENGTH_SHORT)
                    .show()
            }else{
                selectImage()
            }
        }
    }

    private fun setImageGridAdapter(imageList: ArrayList<File>){
        binding.gvSelectedImages.layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        imageGridAdapter = ImageGridAdapter(requireContext(), imageList)
        binding.gvSelectedImages.adapter = imageGridAdapter
        binding.gvSelectedImages.setHasFixedSize(true)
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
                "Take Photo", "Choose from Gallery", "Cancel"
        )
        val builder = AlertDialog.Builder(requireContext())
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
        Dexter.withContext(requireContext()).withPermissions(
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
                            requireContext(),
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
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
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
                        requireContext(), BuildConfig.APPLICATION_ID.toString() + ".provider",
                        photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(
                        takePictureIntent,
                        DetailsFormActivity.REQUEST_TAKE_PHOTO
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
                DetailsFormActivity.REQUEST_GALLERY_PHOTO
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == DetailsFormActivity.REQUEST_TAKE_PHOTO) {
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
            } else if (requestCode == DetailsFormActivity.REQUEST_GALLERY_PHOTO) {

                val clipData: ClipData? = data?.clipData

                if (clipData?.itemCount!! > 4) {
                    Toast.makeText(requireContext(), "Please select maximum 4 images only", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    if ((imageFileList.size + clipData.itemCount) > 4) {
                        Toast.makeText(requireContext(), "Please select maximum 4 images only", Toast.LENGTH_SHORT)
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
        val builder = AlertDialog.Builder(requireContext())
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
        val uri = Uri.fromParts("package", requireContext().packageName, null)
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
            cursor = requireContext().contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    override fun getViewModel() = ZohoViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?)
            = FragmentPatrolRunsheetEntryEditBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
            ZohoRepository(remoteDataSource.buildApi(ZohoApi::class.java))
}