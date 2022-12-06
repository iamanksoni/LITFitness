package com.litmethod.android.ui.Dashboard.AllClassTabScreen.EditProfile.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.network.EditProfileRepository
import com.litmethod.android.network.FilterActivityRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.ViewModel.ClassesFragmentViewModel

class EditProfileViewModelFactory constructor(private val repository: EditProfileRepository, var context: Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            EditProfileViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }


}