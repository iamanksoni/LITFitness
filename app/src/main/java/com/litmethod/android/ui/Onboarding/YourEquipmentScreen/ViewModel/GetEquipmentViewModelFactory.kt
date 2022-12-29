package com.litmethod.android.ui.Onboarding.YourEquipmentScreen.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.network.GetEquipmentRepository
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel.GetInterestViewModel

class GetEquipmentViewModelFactory constructor(private val repository: GetEquipmentRepository, var context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GetEquipmentViewModel::class.java)) {
            GetEquipmentViewModel(this.repository, context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}