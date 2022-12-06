package com.litmethod.android.ui.Dashboard.LiveClassTabScreen.LiveClassFragmentScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.LiveClassFragmentRepository
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassCoverActvityViewModel

class LiveScreenFragmentViewModelfactory constructor(private val repository:LiveClassFragmentRepository , var context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LiveScreenFragmentViewModel::class.java)) {
            LiveScreenFragmentViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}