package com.example.appityappity.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: Long,
    val category: String? = "",
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>?,
    var correct: Boolean = false
): Parcelable

@Parcelize
data class Category(
    val title: String,
    val color: Int,
    var score: String
): Parcelable
