package com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModel

class GetInterestViewModelFactory constructor(private val repository: GetInterestRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetInterestViewModel::class.java)) {
            GetInterestViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}