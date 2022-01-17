package com.example.appityappity.utils

import java.util.*

fun String.toPrefFormat(): String {
    return this.lowercase(Locale.getDefault()).replace(' ', '_')
}

fun String.toTitle(): String {
    val temp1 = this.replace('_', ' ')
    val temp = temp1.split(' ')
    var result = ""
    for (i in temp) {
       result+= i.replaceFirstChar { it.uppercase() } + " "
    }


    return result.trim()
}