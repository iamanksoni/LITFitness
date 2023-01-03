package com.litmethod.android.ui.Onboarding.ProfileScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityProfileBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.MeasureScreen.MeasureActivity
import com.litmethod.android.ui.Onboarding.ProfileScreen.Util.RealPathUtil
import com.litmethod.android.utlis.UiDataObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*


class ProfileActivity : BaseActivity(), View.OnClickListener {
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
    }

    lateinit var binding: ActivityProfileBinding
    var path: String? = null
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        setUpUi()
        clickListener()
    }

    private fun getAgeInYears(dayOfMonth: Int, month: Int, year: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

    private fun setUpUi() {
        binding.firstName.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.firstName.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.firstName.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.firstName.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.firstName.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.lastName.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.lastName.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.lastName.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.lastName.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.lastName.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.userName.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.userName.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.userName.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.userName.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.userName.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.firstName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.firstName.text.toString(),
                    binding.userName.text.toString(),
                    binding.etDob.text.toString(),
                    binding.lastName.text.toString()
                )
            }
        })
        binding.lastName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.firstName.text.toString(),
                    binding.userName.text.toString(),
                    binding.etDob.text.toString(),
                    binding.lastName.text.toString()
                )
            }
        })
        binding.userName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.firstName.text.toString(),
                    binding.userName.text.toString(),
                    binding.etDob.text.toString(),
                    binding.lastName.text.toString()
                )
            }
        })
        binding.etDob.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                datePickClearFocus()
                editTextValueCheckIng(
                    binding.firstName.text.toString(),
                    binding.userName.text.toString(),
                    binding.etDob.text.toString(),
                    binding.lastName.text.toString()
                )
            }
        })
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.etDob.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.llImage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.et_dob -> {
                binding.firstName.clearFocus()
                binding.lastName.clearFocus()
                binding.userName.clearFocus()
                binding.etDob.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etDob.stroke = ColorStateList.valueOf(colorInt)
                CalenderDialog(this@ProfileActivity, binding.etDob)


            }
            R.id.btn_next -> {
                binding.firstName.clearFocus()
                binding.lastName.clearFocus()
                binding.userName.clearFocus()
                binding.etDob.clearFocus()
                Log.d("thedateis", "the date is ${binding.etDob.text}")

                val splittedDate = binding.etDob.text.toString().split("/")
                if (getAgeInYears(
                        splittedDate[0].toInt(),
                        splittedDate[1].toInt(),
                        splittedDate[2].toInt()
                    ) > 10
                ) {
                    nextScreen()
                }else{
                    toastMessageShow("Age should be greater than 10 years.")
                }
            }
            R.id.ll_image -> {
                getImage()
            }
        }
    }

    private fun editTextValueCheckIng(firstName: String, userName: String, dob: String, lastName: String): Boolean {
        if (firstName.isNotEmpty()) {
            binding.errorFirstName.visibility = View.GONE
        }
        if (lastName.isNotEmpty()) {
            binding.errorLastName.visibility = View.GONE
        }
        if (userName.isNotEmpty()) {
            binding.errorUserName.visibility = View.GONE
        }
        if (dob.isNotEmpty()) {
            binding.errorDateOfBirth.visibility = View.GONE
        }
        if (firstName.isNotEmpty() && userName.isNotEmpty() && dob.isNotEmpty()) {
            nextButtonactive()
            return true
        }
        nextButtonInactive()
        return false
    }

    private fun checkForAllValues(firstName: String, userName: String, dob: String, lastName: String): Boolean {
        if (firstName.isEmpty()) {
            binding.errorFirstName.visibility = View.VISIBLE
        }
        if (lastName.isEmpty()) {
            binding.errorLastName.visibility = View.VISIBLE
        }
        if (userName.isEmpty()) {
            binding.errorUserName.visibility = View.VISIBLE
        }
        if (dob.isEmpty()) {
            binding.errorDateOfBirth.visibility = View.VISIBLE
        }
        if (firstName.isNotEmpty() && userName.isNotEmpty() && dob.isNotEmpty()) {
            return true
        }
        return false
    }


    fun nextScreen() {


        if (checkForAllValues(
                binding.firstName.text.toString(),
                binding.userName.text.toString(),
                binding.etDob.text.toString(),
                binding.lastName.text.toString()
            )
        ) {
            UiDataObject.firstName = binding.firstName.text.toString()
            UiDataObject.lastName = binding.lastName.text.toString().ifEmpty { "" }
            UiDataObject.username = binding.userName.text.toString().trim()
            val originalFormat: DateFormat = SimpleDateFormat("d/m/yyyy")
            val targetFormat: DateFormat = SimpleDateFormat("dd-mm-yyyy")
            val date: Date = originalFormat.parse(binding.etDob.text.toString())
            val formattedDate: String = targetFormat.format(date)
            UiDataObject.etDob = formattedDate
            Log.d("SighUpResponse37", "body is $formattedDate")


            val intent = Intent(this@ProfileActivity, MeasureActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun datePickClearFocus() {
        binding.etDob.strokeWidth = 0.0f
        val colorInt = resources.getColor(R.color.mono_slate_10)
        binding.etDob.stroke = ColorStateList.valueOf(colorInt)
    }

    private fun nextButtonInactive() {
        binding.btnNext.backgroundTintList = null
    }

    private fun nextButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
    }

    private  fun getImage() {
        if (Build.VERSION.SDK_INT > 32) {
            // Handling the Version 13 ISSUE
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                //show popup to request runtime permission
                ActivityCompat.requestPermissions(
                    this, permissions,
                    PERMISSION_CODE
                );
            } else {
                pickImageFromGallery()
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else{
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            uri = data?.data
            path = uri?.let { RealPathUtil.getRealPath(this, it) }
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
            //val bitmap = BitmapFactory.decodeFile(path)
            binding.ivChooseimg.setImageBitmap(bitmap)
            calltoChangeAvatar()
        }
    }

    // Api call to changing the USerImage
    private fun calltoChangeAvatar() {
        val file = File(path)

        val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val action: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "avtarImage")
//        val action2: RequestBody = create("multipart/form-data".toMediaTypeOrNull(), "avtarImage")
        Log.d("check", "the action data is $action")

        UiDataObject.body = body
        UiDataObject.action = action
        Log.d("SighUpResponse37", "body is $body and req file is $action")
    }

}