package com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.ui.Onboarding.SignUpScreen.SignUpViewModell

class InjuryViewModelFactory constructor(private val repository: InjuryRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(InjuryViewModel::class.java)) {
            InjuryViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}