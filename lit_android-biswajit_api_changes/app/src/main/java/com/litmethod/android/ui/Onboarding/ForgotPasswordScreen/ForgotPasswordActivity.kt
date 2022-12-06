package com.litmethod.android.ui.Onboarding.ForgotPasswordScreen

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityForgotPasswordBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.PasswordResetScreen.PasswordResetActivity

class ForgotPasswordActivity :BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        setUpUi()
        clickListener()
    }
    private fun setUpUi() {
        binding.loginEtEmail.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                binding.loginEtEmail.strokeWidth = 1.0f
                val colorInt = resources.getColor(R.color.red)
                binding.loginEtEmail.stroke = ColorStateList.valueOf(colorInt)
            } else {
                binding.loginEtEmail.strokeWidth = 0.0f
                val colorInt = resources.getColor(R.color.mono_slate_10)
                binding.loginEtEmail.stroke = ColorStateList.valueOf(colorInt)
            }
        }

        binding.loginEtEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextValueCheckIng(
                    binding.loginEtEmail.text.toString()
                )
            }
        })
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.btnPasswordReset.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
               finish()
            }
            R.id.btn_password_reset ->{
                binding.loginEtEmail.clearFocus()
                intentActivity(this@ForgotPasswordActivity, PasswordResetActivity::class.java,"")
            }
        }
    }

    private fun editTextValueCheckIng(email: String) {
        when {
            email.isEmpty() -> {
                loginButtonInactive()
            }
            !checkEmail(email) -> {
                loginButtonInactive()
            }
            else -> {
                loginButtonActive()
            }
        }
    }

    private fun loginButtonInactive() {
        binding.btnPasswordReset.backgroundTintList = null
        binding.btnPasswordReset.isEnabled = false
        binding.btnPasswordReset.isClickable = false
    }

    private fun loginButtonActive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnPasswordReset.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnPasswordReset.isEnabled = true
        binding.btnPasswordReset.isClickable = true
    }
}