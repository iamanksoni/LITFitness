package com.litmethod.android.ui.Onboarding.LoginScreen

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityLoginBinding
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.SignInRepository
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.DashBoardActivity
import com.litmethod.android.ui.Onboarding.ForgotPasswordScreen.ForgotPasswordActivity
import com.litmethod.android.ui.Onboarding.SignUpScreen.SignUpActivity
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var dataPereREnceObject: DataPreferenceObject
    var hidePass: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        checkIntentData()
        setUpUi()
        clickListener()
        viewModelSetup()
    }

    private fun checkIntentData() {
        val checkbackButton = intent.getStringExtra("showBackButton")
        if (checkbackButton != null) {
            if (checkbackButton == "showbackbutton") {
                binding.ibBackButton.visibility = View.GONE
            }
        }
    }


    private fun setUpUi() {
        dataPereREnceObject = DataPreferenceObject(this)
        binding.loginEtEmail.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.loginEtEmail.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.loginEtEmail.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.loginEtEmail.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.loginEtEmail.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.loginEtPassword.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.loginEtPassword.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.loginEtPassword.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.loginEtPassword.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.loginEtPassword.stroke = ColorStateList.valueOf(colorInt)
            }
        }
        binding.loginEtPassword.transformationMethod = PasswordTransformationMethod()
        binding.loginEtEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.loginEtEmail.text.toString(),
                    binding.loginEtPassword.text.toString()
                )
            }
        })
        binding.loginEtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.loginEtEmail.text.toString(),
                    binding.loginEtPassword.text.toString()
                )
            }
        })
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.ibPasswordIcon.setOnClickListener {

            if (hidePass) {
                binding.ibPasswordIcon.setImageResource(R.drawable.ic_show)
                binding.loginEtPassword.setTransformationMethod(null)
                binding.loginEtPassword.setSelection(binding.loginEtPassword.getText().length)
                hidePass = false
            } else {
                binding.ibPasswordIcon.setImageResource(R.drawable.eye_slash)

                binding.loginEtPassword.setTransformationMethod(PasswordTransformationMethod())
                binding.loginEtPassword.setSelection(binding.loginEtPassword.getText().length)

                hidePass = true
            }
        }
        binding.mbForgotPassword.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.btnLogIn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.mb_forgot_password -> {
                binding.loginEtEmail.clearFocus()
                binding.loginRlPassword.clearFocus()
                intentActivity(this@LoginActivity, ForgotPasswordActivity::class.java, "")
            }
            R.id.tv_signup -> {
                binding.loginEtEmail.clearFocus()
                binding.loginRlPassword.clearFocus()
                intentActivity(this@LoginActivity, SignUpActivity::class.java, "")
            }
            R.id.btn_log_in -> {
                closeKeyBoard()
                if (checkForAllValues(
                        binding.loginEtEmail.text.toString(),
                        binding.loginEtPassword.text.toString()
                    )
                ) {
                    binding.loginEtEmail.clearFocus()
                    binding.loginRlPassword.clearFocus()
                    binding.spLoading.visibility = View.VISIBLE
                    viewModel.checkLogin(
                        binding.loginEtEmail.text.toString().trim(),
                        binding.loginEtPassword.text.toString().trim()
                    )
                }
            }
        }
    }

    private fun checkForAllValues(email: String, pass: String): Boolean {
        if (email.isEmpty() || !checkEmail(email)) {
            binding.errorEmailLogin.visibility = View.VISIBLE
        }
        if (pass.isEmpty()) {
            binding.errorPasswordLogin.visibility = View.VISIBLE
        }
        if (email.isNotEmpty() && checkEmail(email) && pass.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun editTextValueCheckIng(email: String, pass: String): Boolean {
        if (checkEmail(email)) {
            binding.errorEmailLogin.visibility = View.GONE
        }
        if (pass.isNotEmpty()) {
            binding.errorPasswordLogin.visibility = View.GONE
        }
        if (email.isNotEmpty() && checkEmail(email) && !pass.isEmpty()) {
            loginButtonActive()
            return true
        }
        loginButtonInactive()
        return false
    }

    private fun loginButtonInactive() {
        binding.btnLogIn.backgroundTintList = null
    }

    private fun loginButtonActive() {
        val colorInt = ContextCompat.getColor(this, R.color.red)
        binding.btnLogIn.backgroundTintList = ColorStateList.valueOf(colorInt)
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                LoginViewModelFactory(SignInRepository(retrofitService), this)
            ).get(
                LoginViewModel::class.java
            )
        loginResponse()
    }


    private fun loginResponse() {
        viewModel.signInUserData.observe(this, Observer {
            if (it.serverResponse.statusCode == 200) {
                lifecycleScope.launch {
                    dataPereREnceObject.save(
                        "userToken",
                        it.result.profileDetails.accessToken.accessToken
                    )
                }
                BaseResponseDataObject.accessToken =
                    it.result.profileDetails.accessToken.accessToken
                BaseResponseDataObject.token = it.result.profileDetails.accessToken.accessToken
                BaseResponseDataObject.profilePageData = it.result.profileDetails
                binding.spLoading.visibility = View.GONE
                intentActivityWithFinish(this@LoginActivity, DashBoardActivity::class.java)
            } else {
                binding.spLoading.visibility = View.GONE
                toastMessageShow(it.serverResponse.message.toString())
            }


        })

        viewModel.errorMessage.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }


}

