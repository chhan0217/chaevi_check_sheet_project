package com.example.checksheetproject.presentation.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val INSPECTION_DATE_PATTERN = "yyyy-MM-dd"
private const val SERVER_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

internal fun currentInspectionDate(): String {
    return SimpleDateFormat(INSPECTION_DATE_PATTERN, Locale.KOREA).format(Date())
}

internal fun Long.toServerDateTime(): String {
    return SimpleDateFormat(SERVER_DATE_TIME_PATTERN, Locale.KOREA).format(Date(this))
}
