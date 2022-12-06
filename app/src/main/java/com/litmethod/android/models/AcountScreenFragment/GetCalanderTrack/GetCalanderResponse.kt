package com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack

import com.litmethod.android.models.GetLevel.Result
import com.litmethod.android.models.GetLevel.ServerResponse
import com.litmethod.android.models.InjuryResponse.SubscribtionStatus

data class GetCalanderResponse(
    val result: CalanderResponseModel,
    val serverResponse: ServerResponse,
)
