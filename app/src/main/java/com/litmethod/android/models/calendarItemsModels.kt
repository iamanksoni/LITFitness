package com.litmethod.android.models

import java.time.LocalDateTime

data class calendarItemsModels(
    val time: LocalDateTime,
    val activeDays: String,
    val classesTaken: String,
    val remaining: String,
)
