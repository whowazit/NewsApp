package com.example.newsapi.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toStringDateAndTime(format: String): String {
    var date = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    var formatter = DateTimeFormatter.ofPattern(format)
    return date.format(formatter)
}