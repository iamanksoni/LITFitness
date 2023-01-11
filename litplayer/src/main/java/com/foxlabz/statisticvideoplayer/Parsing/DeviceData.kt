package com.foxlabz.statisticvideoplayer.Parsing

data class DeviceData(
    val description: String,
    val hexcode: String,
    val id: String,
    val image: String,
    val title: String,
    var selectedItem:Boolean? = false,
    var connectionStatus:Boolean? = false
)