package com.foxlabz.statisticvideoplayer

data class DeviceDataCalculated(
    var deviceName :String?,
    var isPresent :Boolean?,
    var deviceValueLogged : Any?,
    var parameterType: String?,
    var parameterValue: String?
) {
}