package com.litmethod.android.ui.Onboarding.LevelScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.GetLevelRepository


class GetLevelFactory constructor(private val repository:  GetLevelRepository, val context: Context): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetLevelViewModel::class.java)) {
            GetLevelViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}