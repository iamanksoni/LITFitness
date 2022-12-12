package com.litmethod.android.ui.Onboarding.MeasureScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityMeasureBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.InjuryScreen.InjuryActivity
import com.litmethod.android.ui.Onboarding.MeasureScreen.Util.UntiConvert
import com.litmethod.android.utlis.UiDataObject

class MeasureActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityMeasureBinding
    var gender2: String = "Male"
    val convert = UntiConvert()
    var meter: String = ""
    var meter2: String = ""
    var lbs: String = ""
    var weightUnit = "lbs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_measure)
        setUpUi()
        clickListener()
    }


    private fun setUpUi() {
        val typeFacebold = Typeface.createFromAsset(assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(assets, "futura_std_condensed.otf")
        binding.rgGender.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_male -> {
                        binding.rbMale.typeface = typeFacebold
                        binding.rbFemale.typeface = typeFace
                        binding.rbOther.typeface = typeFace
                        gender2 = "Male"
                    }
                    R.id.rb_female -> {
                        binding.rbMale.typeface = typeFace
                        binding.rbFemale.typeface = typeFacebold
                        binding.rbOther.typeface = typeFace
                        gender2 = "Female"
                    }
                    R.id.rb_other -> {
                        binding.rbMale.typeface = typeFace
                        binding.rbFemale.typeface = typeFace
                        binding.rbOther.typeface = typeFacebold
                        gender2 = "Other"
                    }
                }
            }
        }
        binding.rbMale.isChecked = true
        binding.rgFtCm.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_ft -> {
                        binding.rbFt.typeface = typeFacebold
                        binding.rbCm.typeface = typeFace
                        binding.etHightCm.visibility = View.GONE
                        binding.llHightFeetInch.visibility = View.VISIBLE
                        binding.etHightFt.requestFocus()
                        fromValidate()
                        setDataMeterToFeet()
                    }
                    R.id.rb_cm -> {
                        binding.rbFt.typeface = typeFace
                        binding.rbCm.typeface = typeFacebold
                        binding.etHightCm.visibility = View.VISIBLE
                        binding.llHightFeetInch.visibility = View.GONE
                        binding.etHightCm.requestFocus()
                        fromValidate()
                        setDataFeetToMeter()
                    }
                }
            }
        }
        binding.rbFt.isChecked = true
        binding.rgLbsKg.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_lbs -> {
                        binding.rbLbs.typeface = typeFacebold
                        binding.rbKg.typeface = typeFace
                        setDataKgTLbsUi()
                        weightUnit = "lbs"
                        Log.d("thekg", "the kf $lbs")
                    }
                    R.id.rb_kg -> {
                        binding.rbLbs.typeface = typeFace
                        binding.rbKg.typeface = typeFacebold
                        setDatalbsTokg()
                        weightUnit = "kgs"
                    }
                }
            }
        }
        binding.rbLbs.isChecked = true

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

        binding.etHightFt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                fromValidate()
            }
        })
        binding.etHightIn.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                fromValidate()
            }
        })
        binding.etHightCm.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                meter = binding.etHightCm.text.toString().trim()
                Log.d("textChnged", "metr is $meter")
                fromValidate()
            }
        })
        binding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                fromValidate()
            }
        })
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
    }

    private fun fromValidate() {
        editTextValueCheckIng(
            binding.etHightFt.text.toString(),
            binding.etHightIn.text.toString(),
            binding.etHightCm.text.toString(),
            binding.etWeight.text.toString()
        )
    }

    private fun editTextValueCheckIng(
        et_hight_ft: String,
        et_hight_in: String,
        et_hight_cm: String,
        et_weight: String
    ) {
        if (binding.rbFt.isChecked) {
            when {
                et_hight_ft.isEmpty() -> {
                    nextButtonInactive()
                }
                et_hight_in.isEmpty() -> {
                    nextButtonInactive()
                }
                et_weight.isEmpty() -> {
                    nextButtonInactive()
                }

                else -> {
                    nextButtonactive()
                }
            }
        } else if (binding.rbCm.isChecked) {
            when {
                et_hight_cm.isEmpty() -> {
                    nextButtonInactive()
                }

                et_weight.isEmpty() -> {
                    nextButtonInactive()
                }
                else -> {
                    nextButtonactive()
                }
            }
        }

    }

    private fun checkForAllValues(
        et_hight_ft: String,
        et_hight_in: String,
        et_hight_cm: String,
        et_weight: String
    ): Boolean {
        if (binding.rbFt.isChecked) {
            when {
                et_hight_ft.isEmpty() -> {
                    toastMessageShow("Please enter height in feet")
                    binding.etHightFt.requestFocus()
                    return false
                }
                et_hight_in.isEmpty() -> {
                    toastMessageShow("Please enter height in inch")
                    binding.etHightIn.requestFocus()
                    return false
                }
                et_weight.isEmpty() -> {
                    toastMessageShow("Please enter weight")
                    binding.etWeight.requestFocus()
                    return false
                }
                else -> {
                    // Do Nothing
                }
            }
        } else if (binding.rbCm.isChecked) {
            when {
                et_hight_cm.isEmpty() -> {
                    toastMessageShow("Please enter height in cm")
                    binding.etHightCm.requestFocus()
                    return false
                }

                et_weight.isEmpty() -> {
                    toastMessageShow("Please enter weight")
                    binding.etWeight.requestFocus()
                    return false
                }
                else -> {
                    // Do Nothing
                }
            }
        }
        return true
    }

    private fun nextButtonInactive() {
        binding.btnNext.backgroundTintList = null
//        binding.btnNext.isEnabled = false
//        binding.btnNext.isClickable = false
    }

    private fun nextButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
//        binding.btnNext.isEnabled = true
//        binding.btnNext.isClickable = true
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_next -> {
                binding.etHightFt.clearFocus()
                binding.etHightIn.clearFocus()
                binding.etHightCm.clearFocus()
                binding.etWeight.clearFocus()
                if (checkForAllValues(
                        binding.etHightFt.text.toString(),
                        binding.etHightIn.text.toString(),
                        binding.etHightCm.text.toString(),
                        binding.etWeight.text.toString()
                    )
                ) {
                    nextScreen()
                }
//                intentActivity(this@MeasureActivity, InjuryActivity::class.java,"")
            }
        }
    }


    fun nextScreen() {
        if (weightUnit == "kgs") {
            setDataKgTLbs()
        } else {
            lbs = binding.etWeight.text.toString()
        }
        UiDataObject.gender = gender2
        UiDataObject.HightFt = binding.etHightFt.text.toString().trim().toFloat().toInt()
        UiDataObject.HightIn = binding.etHightIn.text.toString().trim().toFloat().toInt()
        UiDataObject.unitHeight = "Feet"
        UiDataObject.unitWeight = weightUnit
        UiDataObject.Weight = lbs.toFloat().toInt()


        val intent = Intent(this@MeasureActivity, InjuryActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    fun setDataFeetToMeter() {

        if (binding.etHightFt.text.toString().trim()
                .isNotEmpty() || binding.etHightIn.text.toString().trim().isNotEmpty()
        ) {
            var heightInMeter = convert.convertFeetToMeters(
                binding.etHightFt.text.toString().trim(),
                binding.etHightIn.text.toString().trim()
            )

            meter2 = heightInMeter
            binding.etHightCm.text = heightInMeter
        }
    }

    fun setDataMeterToFeet() {
        Log.d("meterData", "meter is $meter and meter2 is $meter2")
        if (meter != meter2) {
            if (binding.etHightCm.text.toString().trim().isNotEmpty()) {
                val feetAndInches =
                    convert.convertMetersToFeet(binding.etHightCm.text.toString().toDouble())
                binding.etHightFt.text = feetAndInches.feet
                binding.etHightIn.text = feetAndInches.inches

            }
        }

    }

    private fun setDatalbsTokg() {
        if (binding.etWeight.text.toString().trim().isNotEmpty()) {
            var kg = convert.converlbsTokg(binding.etWeight.text.toString().trim())
            binding.etWeight.text = kg

        }
    }

    private fun setDataKgTLbsUi() {
        if (binding.etWeight.text.toString().trim().isNotEmpty()) {
            var tempLbs = convert.convertkgTolbs(binding.etWeight.text.toString().trim())
            binding.etWeight.text = tempLbs
            lbs = tempLbs
        }
    }

    private fun setDataKgTLbs() {
        if (binding.etWeight.text.toString().trim().isNotEmpty()) {
            var tempLbs = convert.convertkgTolbs(binding.etWeight.text.toString().trim())
            lbs = tempLbs
        }
    }
}