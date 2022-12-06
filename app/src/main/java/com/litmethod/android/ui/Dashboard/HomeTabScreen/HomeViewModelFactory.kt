package com.litmethod.android.ui.Dashboard.HomeTabScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.HomeTabFragmentRepository

class HomeViewModelFactory(private val repository: HomeTabFragmentRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(this.repository,context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}