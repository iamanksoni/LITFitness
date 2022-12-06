package com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModel

class ClassesFragementViewModelFactory constructor(private val repository: ClassesFragmentRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ClassesFragmentViewModel::class.java)) {
            ClassesFragmentViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}