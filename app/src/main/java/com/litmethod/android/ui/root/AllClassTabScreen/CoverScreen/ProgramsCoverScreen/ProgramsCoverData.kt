package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen

data class ProgramsCoverData(
    val title:String?=null,
    var subList : MutableList<ChildDataProgram> = ArrayList(),
)

data class ChildDataProgram(
    val title:String,
    var isSelected:Boolean = false
)