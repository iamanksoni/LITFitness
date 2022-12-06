package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.TrainerProfileScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.TrainerProfileScreenRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassCoverActvityViewModel

class TrainerProfileViewModelFactory  constructor(private val repository: TrainerProfileScreenRepository, var context: Context): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrainerProfileViewModel::class.java)) {
            TrainerProfileViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}