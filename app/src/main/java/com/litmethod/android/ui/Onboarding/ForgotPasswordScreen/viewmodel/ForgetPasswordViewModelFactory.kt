package com.litmethod.android.ui.Onboarding.ForgotPasswordScreen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ForgetPasswordRepository

class ForgetPasswordViewModelFactory constructor(private val repository: ForgetPasswordRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            ForgetPasswordViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}