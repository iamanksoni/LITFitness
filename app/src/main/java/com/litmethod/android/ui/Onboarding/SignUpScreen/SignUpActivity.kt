package com.litmethod.android.ui.Onboarding.SignUpScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.litmethod.android.R
import com.litmethod.android.Webview.WebViewActivity
import com.litmethod.android.databinding.ActivitySignUpBinding
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.ui.Onboarding.ProfileScreen.ProfileActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.AppConstants.Companion.URL_PRIVACY_POLICY
import com.litmethod.android.utlis.AppConstants.Companion.URL_TERMS_CONDITION
import com.litmethod.android.utlis.AppConstants.Companion.WEB_URL
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.launch


class SignUpActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: SignUpViewModell
    private val retrofitService = RetrofitDataSourceService.getInstance()
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
                binding.signupEtEmail.strokeWidth = 3.0f
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
                binding.signupEtPassword.strokeWidth = 3.0f
                val colorInt = resources.getColor(R.color.red)
                binding.signupEtPassword.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.signupEtPassword.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.signupEtPassword.stroke = ColorStateList.valueOf(colorInt)
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
                    binding.signupEtPassword.text.toString()
                )
            }
        })
        var spannable =
            SpannableString(getString(R.string.privacy_policy_tnc_message))

        val privacyPolicy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                var privacyPolicyIntent = Intent(this@SignUpActivity, WebViewActivity::class.java)
                privacyPolicyIntent.putExtra(WEB_URL, URL_PRIVACY_POLICY)
                startActivity(privacyPolicyIntent)
            }
        }

        val terms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                var terms = Intent(this@SignUpActivity, WebViewActivity::class.java)
                terms.putExtra(WEB_URL, URL_TERMS_CONDITION)
                startActivity(terms)
            }
        }
        spannable.setSpan(privacyPolicy, 41, 55, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(terms, 60, 72, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvSubHeader2.text = spannable
        binding.tvSubHeader2.movementMethod = LinkMovementMethod.getInstance();

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
                    binding.signupEtPassword.text.toString()
                )
            }
        })


        binding.cbSignup.setOnClickListener {
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
                BaseResponseDataObject.accessToken = it.result.profileDetails.accessToken.accessToken
                lifecycleScope.launch {
                    dataPereREnceObject.save(
                        AppConstants.USER_TOKEN,
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
                        binding.signupEtPassword.text.toString()
                    )
                ) {
                    binding.signupEtEmail.clearFocus()
                    binding.signupEtPassword.clearFocus()
                    binding.spLoading.visibility = View.VISIBLE
                    viewModel.checkLogin(
                        binding.signupEtEmail.text.toString().trim(),
                        binding.signupEtPassword.text.toString().trim()
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


    private fun checkForAllValues(email: String, pass: String): Boolean {
        if (email.isEmpty() || !checkEmail(email)) {
            binding.errorEmail.visibility = View.VISIBLE
        }
        if (pass.isEmpty()) {
            binding.errorPassword.visibility = View.VISIBLE
        }
        if (!binding.cbSignup.isChecked) {
            toastMessageShow(getString(R.string.accept_email_updates_message))
        }
        if (email.isNotEmpty() && checkEmail(email) && pass.isNotEmpty() && binding.cbSignup.isChecked) {
            return true
        }
        return false
    }

    private fun editTextValueCheckIng(email: String, pass: String): Boolean {
        if (checkEmail(email)) {
            binding.errorEmail.visibility = View.GONE
        }
        if (pass.isNotEmpty()) {
            binding.errorPassword.visibility = View.GONE
        }
        if (email.isNotEmpty() && checkEmail(email) && pass.isNotEmpty() && binding.cbSignup.isChecked) {
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