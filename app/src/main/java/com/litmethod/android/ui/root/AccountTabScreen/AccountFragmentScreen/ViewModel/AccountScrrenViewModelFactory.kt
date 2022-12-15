package com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.AcountScreenFragmentRepository
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ViewModel.ClassesFragmentViewModel

class AccountScrrenViewModelFactory constructor(private val repository: AcountScreenFragmentRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AccountScreenViewModel::class.java)) {
            AccountScreenViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}