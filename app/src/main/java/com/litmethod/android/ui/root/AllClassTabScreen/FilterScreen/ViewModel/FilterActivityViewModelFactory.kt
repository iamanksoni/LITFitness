package com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.FilterActivityRepository
import com.litmethod.android.network.TrainerProfileScreenRepository
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen.ViewModel.TrainerProfileViewModel

class FilterActivityViewModelFactory constructor(private val repository: FilterActivityRepository, var context: Context): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FilterActivityViewModel::class.java)) {
            FilterActivityViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}