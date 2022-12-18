package com.litmethod.android.ui.root.AllClassTabScreen.EditProfile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.litmethod.android.R
import com.litmethod.android.databinding.FragmentEditProfileBinding
import com.litmethod.android.models.EditProfileRequest
import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import com.litmethod.android.models.GetCountries.Result
import com.litmethod.android.network.EditProfileRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.AccountScreenFragment
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.EditProfile.ViewModel.EditProfileViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.EditProfile.ViewModel.EditProfileViewModelFactory
import com.litmethod.android.ui.root.DashBoardActivity
import com.litmethod.android.ui.Onboarding.MeasureScreen.Util.UntiConvert
import com.litmethod.android.ui.Onboarding.ProfileScreen.Util.RealPathUtil
import com.litmethod.android.utlis.AppConstants.Companion.AVATAR_IMAGE
import com.litmethod.android.utlis.AppConstants.Companion.IMAGE
import com.litmethod.android.utlis.AppConstants.Companion.MULTIPART_FORM_DATA
import com.litmethod.android.utlis.AppConstants.Companion.TEXT_PLAIN
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class EditProfileFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentEditProfileBinding
    val TAG=DashBoardActivity::class.java.simpleName
    var gender2:String = "Male"
    val convert=  UntiConvert()
    var meter:String = ""
    var meter2:String = ""
    var lbs:String = ""
    var weightUnit="lbs"
    lateinit var viewModel: EditProfileViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var getCountries: List<Result> = ArrayList<Result>()
    var action:String? = null
    var billing_city: String =""
    var billing_state: String=""
    var country: String=""
    var dob: String=""
    var first_name: String=""
    var gender: String=""
    var heightUnit: String=""
    var heightValueFeet: String=""
    var heightValueInches: Any=""
    var last_name: String=""
    var phone: String=""
    var unit: String="lbs"
    var weight: String=""
    var countryCode:String= ""
    var dialCode:String=""
    lateinit var datePickerDialog:DatePickerDialog
    var firstTimeRunWeight = true
    var firstTimeRunHeight = true
    var path:String? = null
    var uri: Uri? = null

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clicKlistner()
        setupUi()
        clickListener()
        viewModelSetup()
        variableDataSetForUpdate()

//        binding.spLoading.visibility = View.VISIBLE

    }

    private fun variableDataSetForUpdate() {

        Log.d(TAG,"the time in millis ${System.currentTimeMillis()}")
        Log.d(TAG,"the cities are ${BaseResponseDataObject.profilePageData.countryCode}")
        countryCode = BaseResponseDataObject.profilePageData.countryCode
        dialCode = BaseResponseDataObject.profilePageData.dialCode
        last_name = BaseResponseDataObject.profilePageData.lastName
        billing_city = BaseResponseDataObject.profilePageData.billing_city
        billing_state = BaseResponseDataObject.profilePageData.billing_state
        country = BaseResponseDataObject.profilePageData.billing_country
        dob = BaseResponseDataObject.profilePageData.dob
        first_name = BaseResponseDataObject.profilePageData.firstName
        gender = BaseResponseDataObject.profilePageData.gender
        heightUnit = BaseResponseDataObject.profilePageData.heightUnit
        heightValueFeet = BaseResponseDataObject.profilePageData.heightValueFeet.toString()
        heightValueInches = BaseResponseDataObject.profilePageData.heightValueInches
//        phone = BaseResponseDataObject.profilePageData.phone
        unit = BaseResponseDataObject.profilePageData.weightUnit
        weight = BaseResponseDataObject.profilePageData.weight.toString()
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, EditProfileViewModelFactory(EditProfileRepository(retrofitService),requireContext())).get(
                EditProfileViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.editUserRequestNullableResponse.observe(viewLifecycleOwner, Observer {

           BaseResponseDataObject.profilePageData= it.result.profileDetails
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            ft.replace(R.id.container, AccountScreenFragment(), "NewFragmentTag")
            ft.commit()
        })

    }




    private fun clicKlistner() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        Log.d("theProfileData","the profile data is ${BaseResponseDataObject.profilePageData}")
        binding.firstName.text = BaseResponseDataObject.profilePageData.firstName
        binding.lastName.text = BaseResponseDataObject.profilePageData.lastName
        binding.userName.text = BaseResponseDataObject.profilePageData.username
        binding.etDob.text = BaseResponseDataObject.profilePageData.dob
        binding.etWeight.text = BaseResponseDataObject.profilePageData.weight.toString()
        binding.etHightFt.text = BaseResponseDataObject.profilePageData.heightValueFeet.toString()
        binding.etHightIn.text = BaseResponseDataObject.profilePageData.heightValueInches.toString()
        binding.signupEtEmail.text = BaseResponseDataObject.profilePageData.email

        context?.let {
            Glide
                .with(it)
                .load(BaseResponseDataObject.profilePageData.profileImage)
                .centerCrop()
                .into(binding.ivChooseimg)
        }
    }

    private fun setupUi() {
        val typeFacebold = Typeface.createFromAsset(getActivity()?.getAssets(), "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(getActivity()?.getAssets(), "futura_std_condensed.otf")
        binding.firstName.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.firstName.strokeWidth = 1.0f
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
                binding.lastName.strokeWidth = 1.0f
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
                binding.userName.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.userName.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.userName.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.userName.stroke = ColorStateList.valueOf(colorInt)
            }
        }

        binding.etHightCm.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.etHightCm.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etHightCm.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.etHightCm.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.etHightCm.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.etHightFt.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.etHightFt.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etHightFt.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.etHightFt.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.etHightFt.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.etWeight.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.etWeight.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etWeight.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.etWeight.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.etWeight.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.etHightIn.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.etHightIn.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etHightIn.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.etHightIn.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.etHightIn.stroke = ColorStateList.valueOf(colorInt)
            }
        }



        binding.rgGender.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_male -> {
                        binding.rbMale.typeface = typeFacebold
                        binding.rbFemale.typeface = typeFace
                        binding.rbOther.typeface = typeFace
                        gender2 ="Male"
                    }
                    R.id.rb_female -> {
                        binding.rbMale.typeface = typeFace
                        binding.rbFemale.typeface = typeFacebold
                        binding.rbOther.typeface = typeFace
                        gender2 ="Female"
                    }
                    R.id.rb_other -> {
                        binding.rbMale.typeface = typeFace
                        binding.rbFemale.typeface = typeFace
                        binding.rbOther.typeface = typeFacebold
                        gender2 ="Other"
                    }
                }
            }
        }
        when(BaseResponseDataObject.profilePageData.gender){
             "Male" -> binding.rbMale.isChecked = true
            "Female" ->binding.rbFemale.isChecked = true
            "Other" -> binding.rbOther.isChecked = true
        }

        binding.rgFtCm.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_ft -> {
                        binding.rbFt.typeface = typeFacebold
                        binding.rbCm.typeface = typeFace
                        binding.etHightCm.visibility = View.GONE
                        binding.llHightFeetInch.visibility = View.VISIBLE
                        binding.etHightFt.requestFocus()
                        heightUnit="Feet"
                        editTextValueCheckIng(
                            binding.etDob.text.toString(),
                            binding.firstName.text.toString(),
                            binding.lastName.text.toString(),
                            binding.etHightFt.text.toString(),
                            binding.etHightIn.text.toString(),
                            binding.etHightCm.text.toString(),
                            binding.etWeight.text.toString()

                        )
                        if (firstTimeRunHeight){
                            firstTimeRunHeight = false
                        }else{
                            setDataMeterToFeet()
                        }

                    }
                    R.id.rb_cm -> {
                        binding.rbFt.typeface = typeFace
                        binding.rbCm.typeface = typeFacebold
                        binding.etHightCm.visibility = View.VISIBLE
                        binding.llHightFeetInch.visibility = View.GONE
                        binding.etHightCm.requestFocus()
                        heightUnit = "meter"
                        firstTimeRunHeight = false
                        editTextValueCheckIng(
                            binding.etDob.text.toString(),
                            binding.firstName.text.toString(),
                            binding.lastName.text.toString(),
                            binding.etHightFt.text.toString(),
                            binding.etHightIn.text.toString(),
                            binding.etHightCm.text.toString(),
                            binding.etWeight.text.toString()

                        )
                        setDataFeetToMeter()
                    }
                }
            }
        }

        when(BaseResponseDataObject.profilePageData.heightUnit){
            "meter" -> binding.rbCm.isChecked = true
            "Feet" ->  binding.rbFt.isChecked = true
            else ->binding.rbFt.isChecked = true
        }

        binding.rgLbsKg.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_lbs -> {
                        binding.rbLbs.typeface = typeFacebold
                        binding.rbKg.typeface = typeFace
                        if (firstTimeRunWeight){
                            firstTimeRunWeight =false
                        }else{
//                            binding.etWeight.text = kgconverToLbs
                            setDataKgTLbsUi()
                        }

                        unit="lbs"

                    }
                    R.id.rb_kg -> {
                        binding.rbLbs.typeface = typeFace
                        binding.rbKg.typeface = typeFacebold
//                        binding.etWeight.text = lbsConverToKg
                        firstTimeRunWeight =false
                        setDatalbsTokg()
                        unit="kgs"
//                        Log.d("thekg","the weight unit $weightUnit")
                    }
                }
            }
        }
        when(BaseResponseDataObject.profilePageData.weightUnit){
            "kgs" -> binding.rbKg.isChecked = true
            "lbs" ->  binding.rbLbs.isChecked = true
            else ->binding.rbLbs.isChecked = true
        }



        binding.etDob.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(

                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()


                )
            }
        })

        binding.firstName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(

                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()


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

                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()

                )
            }
        })

        binding.etHightFt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()

                )


            }
        })
        binding.etHightIn.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(

                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()

                )
            }
        })
        binding.etHightCm.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                editTextValueCheckIng(

                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()

                )
            }
        })
        binding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.etDob.text.toString(),
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.etHightFt.text.toString(),
                    binding.etHightIn.text.toString(),
                    binding.etHightCm.text.toString(),
                    binding.etWeight.text.toString()


                )

            }
        })


    }

    private fun editTextValueCheckIng(dob:String,firstName:String,lastName:String,etHightFt:String,etHightIn:String,etHightCm:String,etWeight:String) {



        if (binding.rbFt.isChecked) {
            when {

                dob.isEmpty() ->{
                    joinUsButtonInactive()
                }
                firstName.isEmpty() ->{
                    joinUsButtonInactive()
                }
                lastName.isEmpty() ->{
                    joinUsButtonInactive()
                }


                etHightFt.isEmpty() ->{
                    joinUsButtonInactive()
                }
                etHightIn.isEmpty() ->{
                    joinUsButtonInactive()
                }

                etWeight.isEmpty() ->{
                    joinUsButtonInactive()
                }
                else -> {
                    joinUsButtonactive()
                }
            }
        } else if (binding.rbCm.isChecked) {
            when {
                dob.isEmpty() ->{
                    joinUsButtonInactive()
                }
                firstName.isEmpty() ->{
                    joinUsButtonInactive()
                }
                lastName.isEmpty() ->{
                    joinUsButtonInactive()
                }



                etHightCm.isEmpty() ->{
                    joinUsButtonInactive()
                }
                etWeight.isEmpty() ->{
                    joinUsButtonInactive()
                }
                else -> {
                    joinUsButtonactive()
                }
            }
        }
    }

    private fun joinUsButtonInactive() {
        binding.btnNext.backgroundTintList = null
        binding.btnNext.isEnabled = false
        binding.btnNext.isClickable = false
    }

    private fun joinUsButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnNext.isEnabled = true
        binding.btnNext.isClickable = true
    }

    private fun clickListener(){
        binding.ibBackButton.setOnClickListener(this)
        binding.etDob.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.llImage.setOnClickListener(this)
        binding.ivChooseimg.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {

                Log.d("imgbutt","button clicked")
                val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
                ft.replace(R.id.container, AccountScreenFragment(), "NewFragmentTag")
                ft.commit()
            }
            R.id.et_dob ->{
                binding.firstName.clearFocus()
                binding.lastName.clearFocus()
                binding.userName.clearFocus()
                binding.etDob.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.etDob.stroke = ColorStateList.valueOf(colorInt)
                CalenderDialog(requireContext(),binding.etDob)



            }
            R.id.btn_next ->{
                binding.firstName.clearFocus()
                binding.lastName.clearFocus()
                binding.userName.clearFocus()
                binding.etDob.clearFocus()
                Log.d("thecountrycode", "the country code is $countryCode and dialcode is $dialCode")
                first_name= binding.firstName.text.toString().trim()
                last_name = binding.lastName.text.toString().trim()

                weight = binding.etWeight.text.toString().trim()
                dob=binding.etDob.text.toString().trim()

                if (heightUnit=="meter"){
                 val feetObj=   convert.convertMetersToFeet(binding.etHightCm.text.toString().trim().toDouble())
                    heightValueFeet = feetObj.feet.toFloat().toInt().toString()
                    heightValueInches = feetObj.inches
                }else{
                    heightValueFeet = binding.etHightFt.text.toString().trim()
                    heightValueInches = binding.etHightIn.text.toString().trim()
                }

                if (unit=="kgs"){
                    setDataKgTLbs()
                }else{
                    weight = binding.etWeight.text.toString()
                }

                Log.d("weghtis","the height is in feet $heightValueFeet and feet ${heightValueInches.toString().toFloat().toInt()} and height unit $heightUnit and weight $unit")
                viewModel.checkeeditUserRequestNullable(BaseResponseDataObject.accessToken,
                    EditUserRequestNullable("editUser", dob = dob,first_name,gender2,heightUnit,heightValueFeet.toInt(),heightValueInches.toString().toFloat().toInt(),last_name, unit = unit
                        , weight.toFloat().toInt())

                )

            }
            R.id.ll_image ->{


            }
            R.id.iv_chooseimg ->{
                Log.d(TAG," image button was clicked")
                getImage()
            }
        }
    }

    fun setDataFeetToMeter(){

        if (binding.etHightFt.text.toString().trim().isNotEmpty() || binding.etHightIn.text.toString().trim().isNotEmpty() ) {
            var heightInMeter =   convert.convertFeetToMeters(
                binding.etHightFt.text.toString().trim(),
                binding.etHightIn.text.toString().trim()
            )

            meter2 = heightInMeter
            binding.etHightCm.text = "${heightInMeter.toFloat().toInt()}"
        }
    }

    fun setDataMeterToFeet(){
        Log.d("meterData","meter is $meter and meter2 is $meter2")

            if (binding.etHightCm.text.toString().trim().isNotEmpty() ) {
                val feetAndInches = convert.convertMetersToFeet(binding.etHightCm.text.toString().toDouble())
                binding.etHightFt.text = feetAndInches.feet
                binding.etHightIn.text = "${feetAndInches.inches.toFloat().toInt()}"

            }
//        floor(feetAndInches.feet.toFloat()).toString()

    }

    private fun setDatalbsTokg(){
        if (binding.etWeight.text.toString().trim().isNotEmpty()) {
            var kg = convert.converlbsTokg(binding.etWeight.text.toString().trim())
            binding.etWeight.text = "${kg.toFloat().toInt()}"

        }
    }

    private fun setDataKgTLbsUi(){
        if ( binding.etWeight.text.toString().trim().isNotEmpty()) {
            var tempLbs = convert.convertkgTolbs(binding.etWeight.text.toString().trim())
            binding.etWeight.text = "${tempLbs.toFloat().toInt()}"
            lbs = tempLbs
        }
    }

    private fun setDataKgTLbs(){
        if ( binding.etWeight.text.toString().trim().isNotEmpty()) {
            var tempLbs = convert.convertkgTolbs(binding.etWeight.text.toString().trim())
            weight = tempLbs
        }
    }




    fun CalenderDialog(thisActivity: Context, dateTv: EditText): DatePickerDialog {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(thisActivity,R.style.MyDatePickerStyle,
            DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
            dateTv.setText("$mdayOfMonth/$mmonth/$myear")
        }, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
        return datePickerDialog
    }

    private  fun getImage(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery()
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
          PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            uri = data?.data
            path = uri?.let { RealPathUtil.getRealPath(requireContext(), it) }
            val bitmap = BitmapFactory.decodeFile(path)
            binding.ivChooseimg.setImageBitmap(bitmap)
            calltoChangeAvatar()
        }
    }

    // Api call to changing the USerImage
    private fun calltoChangeAvatar() {
        val file = File(path)
        val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
        val body = MultipartBody.Part.createFormData("image",file.name,reqFile)
        val action: RequestBody =    RequestBody.create("text/plain".toMediaTypeOrNull(), "avtarImage")
//        val action2: RequestBody = create("multipart/form-data".toMediaTypeOrNull(), "avtarImage")
        Log.d("check","the action data is $action")

//        val editProfileRequest = EditProfileRequest("avtarImage")
//        val gson = Gson()
//        val data = MultipartBody.Part
//            .createFormData(
//                "action",
//                gson.toJson(editProfileRequest)
//            )
        viewModel.checkSetImage(BaseResponseDataObject.accessToken, body, action)
        Log.d("SighUpResponse37","body is ${body.body} and req file is $action")
    }

}