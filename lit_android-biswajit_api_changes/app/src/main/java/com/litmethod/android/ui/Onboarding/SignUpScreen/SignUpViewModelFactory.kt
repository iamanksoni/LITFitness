package com.litmethod.android.ui.Onboarding.SignUpScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.SignInRepository
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginViewModel

class SignUpViewModelFactory constructor(private val repository: SignUpRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignUpViewModell::class.java)) {
            SignUpViewModell(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}