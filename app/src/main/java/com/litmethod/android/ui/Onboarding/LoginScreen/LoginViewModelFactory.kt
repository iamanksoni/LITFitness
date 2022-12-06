package com.litmethod.android.ui.Onboarding.LoginScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.SignInRepository

class LoginViewModelFactory constructor(private val repository: SignInRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.repository,context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}