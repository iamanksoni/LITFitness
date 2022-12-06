package com.litmethod.android.ui.Onboarding.PasswordResetScreen

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityPasswordResetBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity

class PasswordResetActivity : BaseActivity(), View.OnClickListener{
    lateinit var binding: ActivityPasswordResetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset)

        clickListener()
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.btnBackToLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_back_to_login ->{
                intentActivityWithFinish(this@PasswordResetActivity, LoginActivity::class.java)
            }
        }
    }
}