package com.litmethod.android.ui.Onboarding.SignUpScreen

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivitySignUpBinding
import com.litmethod.android.network.RetrofitService
import com.litmethod.android.network.RetrofitService.Companion.retrofitService
import com.litmethod.android.network.SignInRepository
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.Util.AllClassesDataObject
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginViewModel
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginViewModelFactory
import com.litmethod.android.ui.Onboarding.ProfileScreen.ProfileActivity
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.launch


class SignUpActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: SignUpViewModell
    private val retrofitService = RetrofitService.getInstance()
    lateinit var dataPereREnceObject: DataPreferenceObject
    var hidePass: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        setUpUi()
        clickListener()
        viewModelSetup()
    }

    private fun setUpUi() {
        dataPereREnceObject = DataPreferenceObject(this)
        binding.signupEtPassword.transformationMethod = PasswordTransformationMethod()
        binding.signupEtEmail.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.signupEtEmail.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.signupEtEmail.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.signupEtEmail.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.signupEtEmail.stroke = ColorStateList.valueOf(colorInt)
            }
        }

        binding.signupEtPassword.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.signupEtPassword.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.signupEtPassword.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.signupEtPassword.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.signupEtPassword.stroke = ColorStateList.valueOf(colorInt)
            }
        }

        binding.signupEtPhoneNo.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.signupRlPhoneNo.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.signupRlPhoneNo.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.signupRlPhoneNo.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.signupRlPhoneNo.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.signupEtEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.signupEtEmail.text.toString(),
                    binding.signupEtPassword.text.toString(),
                    binding.signupEtPhoneNo.text.toString()
                )
            }
        })

        binding.signupEtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.signupEtEmail.text.toString(),
                    binding.signupEtPassword.text.toString(),
                    binding.signupEtPhoneNo.text.toString()
                )
            }
        })

        binding.signupEtPhoneNo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.signupEtEmail.text.toString(),
                    binding.signupEtPassword.text.toString(),
                    binding.signupEtPhoneNo.text.toString()
                )
            }
        })
        binding.cbSignup.setOnClickListener {
            binding.signupEtEmail.clearFocus()
            binding.signupEtPassword.clearFocus()
            binding.signupRlPhoneNo.clearFocus()
            editTextValueCheckIng(
                binding.signupEtEmail.text.toString(),
                binding.signupEtPassword.text.toString(),
                binding.signupEtPhoneNo.text.toString()
            )
        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                SignUpViewModelFactory(SignUpRepository(retrofitService), this)
            ).get(
                SignUpViewModell::class.java
            )
        loginResponse()
    }

    private fun loginResponse() {
        viewModel.signUpUserData.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            if (it.serverResponse.statusCode == 200) {
                AllClassesDataObject.accessToken = it.result.profileDetails.accessToken.accessToken
                lifecycleScope.launch {
                    dataPereREnceObject.save(
                        "userToken",
                        it.result.profileDetails.accessToken.accessToken
                    )
                }
                intentActivityWithFinish(this@SignUpActivity, ProfileActivity::class.java)
            } else {
                toastMessageShow(it.serverResponse.message)
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.ibBackButton.setOnClickListener(this)
        binding.ibPasswordIcon.setOnClickListener {

            if (hidePass) {
                binding.ibPasswordIcon.setImageResource(R.drawable.ic_show)
                binding.signupEtPassword.setTransformationMethod(null)
                binding.signupEtPassword.setSelection(binding.signupEtPassword.getText().length)
                hidePass = false
            } else {
                binding.ibPasswordIcon.setImageResource(R.drawable.eye_slash)
                binding.signupEtPassword.setTransformationMethod(PasswordTransformationMethod())
                binding.signupEtPassword.setSelection(binding.signupEtPassword.getText().length)
                hidePass = true
            }
        }

        binding.btnJoinUs.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_join_us -> {
                if (checkForAllValues(
                        binding.signupEtEmail.text.toString(),
                        binding.signupEtPassword.text.toString(),
                        binding.signupEtPhoneNo.text.toString()
                    )
                ) {
                    binding.signupEtEmail.clearFocus()
                    binding.signupEtPassword.clearFocus()
                    binding.signupEtPhoneNo.clearFocus()
                    binding.spLoading.visibility = View.VISIBLE

                    viewModel.checkLogin(
                        binding.signupEtEmail.text.toString().trim(),
                        binding.signupEtPassword.text.toString().trim(),
                        binding.signupEtPhoneNo.text.toString().trim()
                    )
                }
            }
            R.id.tv_login -> {
                binding.signupEtEmail.clearFocus()
                binding.signupEtPassword.clearFocus()
                intentActivityWithFinish(this@SignUpActivity, LoginActivity::class.java)
            }
        }
    }


    private fun checkForAllValues(email: String, pass: String, phoneNo: String): Boolean {
        if (email.isEmpty() || !checkEmail(email)) {
            binding.errorEmail.visibility = View.VISIBLE
        }
        if (pass.isEmpty()) {
            binding.errorPassword.visibility = View.VISIBLE
        }
        if (phoneNo.isEmpty()) {
            binding.errorPhoneNo.visibility = View.VISIBLE
        }
        if (!binding.cbSignup.isChecked) {
            toastMessageShow("Please accept the email updates, news")
        }
        if (!email.isEmpty() && checkEmail(email) && !pass.isEmpty() && !phoneNo.isEmpty() && binding.cbSignup.isChecked) {
            return true
        }
        return false
    }

    private fun editTextValueCheckIng(email: String, pass: String, phoneNo: String): Boolean {
        if (checkEmail(email)) {
            binding.errorEmail.visibility = View.GONE
        }
        if (!pass.isEmpty()) {
            binding.errorPassword.visibility = View.GONE
        }
        if (!phoneNo.isEmpty()) {
            binding.errorPhoneNo.visibility = View.GONE
        }
        if (!email.isEmpty() && checkEmail(email) && !pass.isEmpty() && !phoneNo.isEmpty() && binding.cbSignup.isChecked) {
            joinUsButtonactive()
            return true
        }
        joinUsButtonInactive()
        return false
    }

    private fun joinUsButtonInactive() {
        binding.btnJoinUs.backgroundTintList = null
    }

    private fun joinUsButtonactive() {
        val colorInt = ContextCompat.getColor(this, R.color.red)
        binding.btnJoinUs.backgroundTintList = ColorStateList.valueOf(colorInt)
    }
}