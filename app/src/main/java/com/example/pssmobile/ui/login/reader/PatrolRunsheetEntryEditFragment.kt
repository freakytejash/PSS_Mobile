package com.example.pssmobile.ui.login.reader

import Data
import Pictures_Section
import Stebrief
import UpdateJobRequestModel
import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import okhttp3.internal.wait
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.util.Base64
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.pssmobile.retrofit.Resource
import com.example.pssmobile.ui.login.handleApiError
import com.example.pssmobile.ui.login.home.AddUserDirections
import java.io.ByteArrayOutputStream

class PatrolRunsheetEntryEditFragment : BaseFragment<ZohoViewModel, FragmentPatrolRunsheetEntryEditBinding, ZohoRepository>(), AdapterView.OnItemSelectedListener {

    private lateinit var mPhotoFile: File
    private lateinit var mCompressor: FileCompressor
    private lateinit var imageGridAdapter: ImageGridAdapter
    private lateinit var model: Data
    private var isJobClosed: Boolean = false
    private var imageFileList: ArrayList<File> = ArrayList()
    private var jobResultsValues: ArrayList<String> = ArrayList()
    private var incidentTypesValues: ArrayList<String> = ArrayList()
    private var severityValues: ArrayList<String> = ArrayList()
    private var selectedJobResult: String = ""
    private var selectedincidentType: String = ""
    private var selectedseverity: String = ""
    val args: PatrolRunsheetEntryEditFragmentArgs by navArgs()

    companion object {
        const val REQUEST_TAKE_PHOTO: Int = 1;
        const val REQUEST_GALLERY_PHOTO: Int = 2;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCompressor = FileCompressor(requireContext())
        model = args.dataModel
        Log.d("App", "Data model in patrol edit: " + model.toString())

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
            } else {
                selectImage()
            }
        }

        binding.spJobResults.onItemSelectedListener = this
        binding.spIncidentType.onItemSelectedListener = this
        binding.spSeverity.onItemSelectedListener = this

        /*jobResultsValues = arrayListOf<String>("Nothing to report - all good", "I need to report an incident", "Lock/Unlock job",
                "Escort details", "Random patrol with pictures")
        incidentTypesValues = arrayListOf<String>("Break in (incident)","Property damage (incident)", "Person on site (incident)",
                "Violent behaviour (incident)", "Medical condition (incident)", "Other (describe in comments)")
        severityValues = arrayListOf("High","Medium","Low")

        val ad_jobresults: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, jobResultsValues as List<Any?>)
        ad_jobresults.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spJobResults.adapter = ad_jobresults

        val ad_incidentTypes: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, incidentTypesValues as List<Any?>)
        ad_incidentTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spIncidentType.adapter = ad_incidentTypes

        val ad_severity: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, severityValues as List<Any?>)
        ad_severity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSeverity.adapter = ad_severity*/

        val jobResultsValues = resources.getStringArray(R.array.jobResults_array)
        val ad_jobresults: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, jobResultsValues)
        ad_jobresults.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spJobResults.adapter = ad_jobresults

        val incidentTypesValues = resources.getStringArray(R.array.incidentTypes_array)
        val ad_incidentType= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, incidentTypesValues) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0
            }

            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    //here is it possible to define color for other items by
                    view.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        ad_incidentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spIncidentType.adapter = ad_incidentType

        val severityValues = resources.getStringArray(R.array.severity_array)
        val ad_severity= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, severityValues) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0
            }

            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    //here is it possible to define color for other items by
                    view.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        ad_severity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSeverity.adapter = ad_severity
        binding.switchJobclosed.setOnCheckedChangeListener(null)
        binding.switchJobclosed.isChecked = model.job_closed.toBoolean()
        binding.switchJobclosed.setOnCheckedChangeListener({_, isChecked ->
            isJobClosed = isChecked
            //Toast.makeText(requireContext(), "Job closed status is ${isChecked}", Toast.LENGTH_SHORT).show()
        })
        btn_update.setOnClickListener {
            binding.progressBarupdate.visibility = View.VISIBLE
            updateDailyRunsheetJob()
        }

        viewModel.updateJobResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("App", "Update job response " + it.toString())
            binding.progressBarupdate.visibility = View.GONE
            when (it) {
                is Resource.Success -> {
                    if (it.value.message.equals("Success")) {
                        val action: NavDirections = PatrolRunsheetEntryEditFragmentDirections.actionPatrolRunsheetEntryEditFragmentToPatrolRunsheetFragment()
                        view?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateDailyRunsheetJob() {
        val selectajob1 = model.select_a_job1.iD
        val location1 = model.location1
        val address = ""
        val daydue = model.day_due
        val jobdescription  = binding.etJobdescription.text.toString()
        val jobDate = model.job_date
        val startdatetime = model.start_date_time
        val enddatetime = model.end_date_time
        val startTime = ""
        val endTime = ""
        val jobType = model.job_type
        val allocatedTo = model.allocated_to.iD
        val active = model.active
        val jobClosed = isJobClosed
        val checkpointId2: List<String> = ArrayList()
        val howmanyCheckpoints = binding.etCheckpointcount.text.toString()
        val checkpoints = model.checkpoints
        val keystatus = ""
        val datetimejobcompleted = binding.etJobcompletiondatetime.text.toString()
        val patrolofficer = model.patrol_Officer
        val quickcomment = ""
        val whatdoyouneedtoreport = selectedJobResult
        val typeofincident = selectedincidentType
        val comments = binding.etComments.text.toString()
        val severity = selectedseverity
        val steid = ""
        val stebrief:Stebrief = Stebrief(binding.etStebrief.text.toString())
        val evidence1 = model.evidence_1
        val evidence2 = model.evidence_2
        val evidence3 = model.evidence_3
        val id = model.iD

        val imageStringList: ArrayList<String> = ArrayList()
        if(imageFileList.isNotEmpty()){
            for (item in imageFileList){
                val bytes = item.readBytes()
                /*val filepath: String = item.path
                val bitmap: Bitmap = BitmapFactory.decodeFile(filepath)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()*/
                val imageString: String = Base64.encodeToString(bytes, Base64.DEFAULT)
                imageStringList.add(imageString)
            }
        }

        var picturesSectionList: ArrayList<Pictures_Section> = ArrayList()
        for (item in imageStringList){
            val picturesSection: Pictures_Section = Pictures_Section(item,"","")
            picturesSectionList.add(picturesSection)
        }

        var jobRequestModel: UpdateJobRequestModel = UpdateJobRequestModel(selectajob1, location1, address, daydue, jobdescription, jobDate, startdatetime, enddatetime,
            startTime, endTime, jobType, allocatedTo, active.toBoolean(), jobClosed, checkpointId2, howmanyCheckpoints, checkpoints, keystatus, datetimejobcompleted, patrolofficer,
            quickcomment, whatdoyouneedtoreport, typeofincident, comments, severity, steid, stebrief, picturesSectionList, evidence1, evidence2, evidence3, id)

        viewModel.updateDailyRunsheetJob(jobRequestModel)
        //Log.d("App","Image string list: " + imageStringList.toString())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_jobResults -> {
                val value = parent.getItemAtPosition(position)
                selectedJobResult = value as String
                //Toast.makeText(requireContext(), "Selected result ${value}", Toast.LENGTH_SHORT).show()
                if (position == 1){
                    binding.llSpinnerLayout.visibility = View.VISIBLE
                }else{
                    //In case of other selected job results except: I need to report an incident
                    //reset other spinners incidenttype and severity to initial state
                    binding.llSpinnerLayout.visibility = View.GONE
                    sp_incidentType.setSelection(0)
                    sp_severity.setSelection(0)
                    selectedincidentType = ""
                    selectedseverity = ""
                }

                if (position == 0){
                    binding.llPictureSection.visibility = View.GONE
                    imageFileList.clear()
                    setImageGridAdapter(imageFileList)
                }else{
                    binding.llPictureSection.visibility = View.VISIBLE
                }
            }
            R.id.sp_incidentType ->{
                val value = parent.getItemAtPosition(position).toString()
                selectedincidentType = value
                if(position == 0){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
            R.id.sp_severity ->{
                val value = parent.getItemAtPosition(position).toString()
                selectedseverity = value
                if(position == 0){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }

        binding.etJobcompletiondatetime.setOnClickListener {
            pickDateTime()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)

                val myFormat = "dd/MM/yyyy HH:mm:ss" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                binding.etJobcompletiondatetime.setText(sdf.format(pickedDateTime.time))

            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setImageGridAdapter(imageList: ArrayList<File>) {
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

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentPatrolRunsheetEntryEditBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
            ZohoRepository(remoteDataSource.buildApi(ZohoApi::class.java))

}