package com.litmethod.android.models

import android.os.Parcelable
import com.litmethod.android.models.HomePageModels.ServerResponse
import kotlinx.parcelize.Parcelize

data class UserAnalyticsResponse(
    val result: ArrayList<ResultUserAnalytics>,
    val serverResponse: ServerResponse
)
@Parcelize
data class ResultUserAnalytics(
    var `data`: Int,
    val key: String,
    val leftArm: Int,
    val rightArm: Int,
    val subKey: String,
    var selected: Boolean = false
): Parcelable

data class SubscribtionStatus(
    val has_subscription: Boolean
)