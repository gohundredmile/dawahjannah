package com.example.util

object BanglaNumberUtils {
    fun toBanglaDigits(number: Int): String {
        return CalendarHelper.toBanglaNumber(number)
    }

    fun toBanglaDigits(number: Long): String {
        return CalendarHelper.toBanglaNumber(number.toString())
    }

    fun toBanglaDigits(text: String): String {
        return CalendarHelper.toBanglaNumber(text)
    }
}
