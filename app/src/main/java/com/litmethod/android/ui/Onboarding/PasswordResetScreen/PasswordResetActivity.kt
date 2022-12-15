package com.litmethod.android.ui.Onboarding.PasswordResetScreen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityPasswordResetBinding
import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordRequest
import com.litmethod.android.network.ForgetPasswordRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.ForgotPasswordScreen.viewmodel.ForgetPasswordViewModel
import com.litmethod.android.ui.Onboarding.ForgotPasswordScreen.viewmodel.ForgetPasswordViewModelFactory
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.utlis.AppConstants

class PasswordResetActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityPasswordResetBinding
    lateinit var viewModel: ForgetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset)
        binding.loginEtEmail.text = intent.getStringExtra("pagename").toString()
        clickListener()
        viewModelSetup()
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.tvResendPasswordReset.setOnClickListener(this)
        binding.btnBackToLogin.setOnClickListener(this)

    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this, ForgetPasswordViewModelFactory(
                    ForgetPasswordRepository(
                        RetrofitDataSourceService.retrofitService!!
                    ), this
                )
            ).get(
                ForgetPasswordViewModel::class.java
            )

        viewModel.forgetPassword.observe(this, Observer {
            Log.d("Resent ForgetPasswordLink Response", "the resend response ${it}")
            toastMessageShow(it.serverResponse!!.message.toString())
        })
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_back_to_login -> {
                intentActivityWithFinish(this@PasswordResetActivity, LoginActivity::class.java)

            }
            R.id.tv_resend_password_reset -> {
                viewModel.getForgetPassword(
                    ForgetPasswordRequest(
                        binding.loginEtEmail.text.toString(),
                        AppConstants.ACTION_FORGOT_PASSWORD
                    )
                )
            }
        }
    }
}