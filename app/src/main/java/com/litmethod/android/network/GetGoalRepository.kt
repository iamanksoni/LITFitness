package com.litmethod.android.network

import com.litmethod.android.models.AcountScreenFragment.EditUserGoal.EditUserGoalRequest
import com.litmethod.android.models.InjuryRequest

class GetGoalRepository constructor(private val retrofitService: RetrofitDataSourceService){
    fun getGoal(auth:String,injuryRequest: InjuryRequest) = retrofitService.getYourGoals(auth,injuryRequest)
    fun editUserForGoal(auth:String,editUserGoalRequest: EditUserGoalRequest) = retrofitService.editUserForGoal(auth,editUserGoalRequest)
}