package com.litmethod.android.ui.Dashboard.AllClassTabScreen.SettingScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.EditProfileRepository
import com.litmethod.android.network.SettingFragmentRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.EditProfile.ViewModel.EditProfileViewModel

class SettingScreenViewModelFactory constructor(private val repository: SettingFragmentRepository, var context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingScreenViewModel::class.java)) {
            SettingScreenViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}