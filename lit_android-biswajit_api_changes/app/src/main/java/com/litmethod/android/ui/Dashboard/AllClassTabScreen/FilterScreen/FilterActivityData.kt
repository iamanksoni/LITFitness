package com.litmethod.android.ui.Dashboard.AllClassTabScreen.FilterScreen

import com.litmethod.android.models.FilterList.Parameter

data class FilterActivityData(
    val title:String?=null,
    var subList : MutableList<ChildData> = ArrayList(),
    var isExpanded:Boolean = false
)

data class ChildData(
    val title:String,
    var isSelected:Boolean = false,
     var childData:Parameter
)