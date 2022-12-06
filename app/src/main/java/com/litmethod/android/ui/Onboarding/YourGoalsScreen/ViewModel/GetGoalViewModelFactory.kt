package com.litmethod.android.ui.Onboarding.YourGoalsScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.GetGoalRepository
import com.litmethod.android.network.SignUpRepository
import com.litmethod.android.ui.Onboarding.SignUpScreen.SignUpViewModell

class GetGoalViewModelFactory constructor(private val repository: GetGoalRepository, var context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetGoalViewModel::class.java)) {
            GetGoalViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}