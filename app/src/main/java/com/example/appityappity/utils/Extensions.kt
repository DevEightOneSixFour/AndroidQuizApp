package com.example.appityappity.utils

import java.lang.StringBuilder
import java.util.*

fun String.toPrefFormat(): String {
    return this.lowercase(Locale.getDefault()).replace(' ', '_')
}

fun String.toTitle(): String {
    val temp1 = this.replace('_', ' ')
    val temp = temp1.split(' ')
    val result = StringBuilder()
    for (i in temp) {
       result.append(i.replaceFirstChar { it.uppercase() } + " ")
    }

    return result.toString().trim()
}