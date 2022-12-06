package com.litmethod.android.ui.Dashboard.HomeTabScreen.HomeTabFragmentScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomePageVideosModel(
    val title: String? = null,
    var selected: Boolean = false
):Parcelable