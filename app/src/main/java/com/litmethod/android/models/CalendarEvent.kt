package com.litmethod.android.models

import java.time.LocalDateTime
import java.util.*

data class CalendarEvent(
    var date: LocalDateTime,
    var difficulty: ArrayList<Int>
)