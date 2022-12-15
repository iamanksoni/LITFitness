package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ViewModel.ClassesFragmentViewModel

class ClassesCoverActivityViewModelFactory constructor(private val repository: ClassesCoverActivityRepository, var context: Context): ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ClassCoverActvityViewModel::class.java)) {
            ClassCoverActvityViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}