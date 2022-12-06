package com.litmethod.android.ui.Onboarding.SplashScreen.ViewmModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.SignInRepository
import com.litmethod.android.network.SplashScreenRepository
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginViewModel

class SpalshScreenViewModelFactory constructor(private val repository: SplashScreenRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            SplashScreenViewModel(this.repository,context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}