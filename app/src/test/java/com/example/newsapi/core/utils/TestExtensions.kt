package com.example.newsapi.core.utils

import org.junit.Assert
import org.junit.Test

class TestExtensions {

    @Test
    fun testToStringDateAndTime() {
        val dateString = "2023-05-22T09:50:19Z"
        val newDateString = dateString.toStringDateAndTime("dd MMM, yyyy hh:mma")
        Assert.assertEquals("22 May, 2023 09:50am", newDateString)
    }
}