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
    lateinit var binding:ActivitySignUpBinding
    lateinit var viewModel: SignUpViewModell
    private val retrofitService = RetrofitService.getInstance()
    lateinit var dataPereREnceObject: DataPreferenceObject
    var hidePass:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        setUpUi()
        clickListener()
        viewModelSetup()
    }

    private fun setUpUi() {
        dataPereREnceObject = DataPreferenceObject(this)
        val typeFace = Typeface.createFromAsset(assets, "futura_std_condensed.otf")

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
        binding.signupEtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.signupEtEmail.text.toString(),
                    binding.signupEtPassword.text.toString()
                )
            }
        })

        binding.signupEtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.signupEtEmail.text.toString(),
                    binding.signupEtPassword.text.toString()
                )
            }
        })
        binding.cbSignup.setOnClickListener{
            binding.signupEtEmail.clearFocus()
            binding.signupEtPassword.clearFocus()
            editTextValueCheckIng(
                binding.signupEtEmail.text.toString(),
                binding.signupEtPassword.text.toString()
            )
        }
    }
    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, SignUpViewModelFactory(SignUpRepository(retrofitService),this)).get(
                SignUpViewModell::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.signUpUserData.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            AllClassesDataObject.accessToken= it.result.profileDetails.accessToken.accessToken
            lifecycleScope.launch {
                dataPereREnceObject.save("userToken", it.result.profileDetails.accessToken.accessToken)
            }
            Log.d("signUpresponse","the signup response ${it}")
            intentActivityWithFinish(this@SignUpActivity, ProfileActivity::class.java)

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

            if(hidePass) {
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
            R.id.btn_join_us ->{
                binding.signupEtEmail.clearFocus()
                binding.signupEtPassword.clearFocus()
                binding.spLoading.visibility = View.VISIBLE

                viewModel.checkLogin(binding.signupEtEmail.text.toString().trim(),binding.signupEtPassword.text.toString().trim())

            }
            R.id.tv_login ->{
                binding.signupEtEmail.clearFocus()
                binding.signupEtPassword.clearFocus()
                intentActivityWithFinish(this@SignUpActivity, LoginActivity::class.java)
            }
        }
    }

    private fun editTextValueCheckIng(email: String,pass: String) {
        when {
            email.isEmpty() -> {
                joinUsButtonInactive()
            }
            !checkEmail(email) -> {
                joinUsButtonInactive()
            }
            pass.isEmpty() ->{
                joinUsButtonInactive()
            }
            !binding.cbSignup.isChecked ->{
                joinUsButtonInactive()
            }
            else -> {
                joinUsButtonactive()
            }
        }
    }

    private fun joinUsButtonInactive() {
        binding.btnJoinUs.backgroundTintList = null
        binding.btnJoinUs.isEnabled = false
        binding.btnJoinUs.isClickable = false
    }

    private fun joinUsButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnJoinUs.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnJoinUs.isEnabled = true
        binding.btnJoinUs.isClickable = true
    }
}