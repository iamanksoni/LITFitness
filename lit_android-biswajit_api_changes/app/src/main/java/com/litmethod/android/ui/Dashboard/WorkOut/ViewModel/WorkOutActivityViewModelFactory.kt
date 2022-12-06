package com.litmethod.android.ui.Dashboard.WorkOut.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.LiveClassFragmentRepository
import com.litmethod.android.network.WorkOutActivityRepository
import com.litmethod.android.ui.Dashboard.LiveClassTabScreen.LiveClassFragmentScreen.ViewModel.LiveScreenFragmentViewModel

class WorkOutActivityViewModelFactory constructor(private val repository: WorkOutActivityRepository, var context: Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WorkOutActivityViewModel::class.java)) {
            WorkOutActivityViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}