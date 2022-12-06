package com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.ProgramsCoverActivityRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassCoverActvityViewModel

class ProgramsCoverActivityViewModelFactory constructor(private val repository: ProgramsCoverActivityRepository, var context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProgramsCoverAcivityViewModel::class.java)) {
            ProgramsCoverAcivityViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}