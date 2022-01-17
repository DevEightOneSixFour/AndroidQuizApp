package com.example.appityappity.utils

import android.content.Context
import com.example.appityappity.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class Convertor {

    fun readJsonFile(context: Context, fileName: String): List<Question>? {
        val questions: List<Question>?
        val jsonString: String?
        val gson = Gson()
        val listOfQuestions  = object: TypeToken<List<Question>>() {}.type

        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            questions = gson.fromJson(jsonString, listOfQuestions)
            questions?.forEachIndexed { index, question -> println("Question $index: $question") }

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return questions
    }
}