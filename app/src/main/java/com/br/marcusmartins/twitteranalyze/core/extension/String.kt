package com.br.marcusmartins.twitteranalyze.core.extension

import java.text.SimpleDateFormat
import java.util.*

const val RESPONSE_DATE_PATTERN = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
const val DATE_PATTERN = "MM/dd/yyyy HH:mm"

fun String.formatDate() : String{
    val date = SimpleDateFormat(RESPONSE_DATE_PATTERN, Locale.ENGLISH).parse(this)
    return SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(date)
}


