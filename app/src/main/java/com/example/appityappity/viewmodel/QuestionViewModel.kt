package com.example.appityappity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appityappity.model.GradeOverview
import com.example.appityappity.model.Question
import com.example.appityappity.model.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class QuestionViewModel : ViewModel() {

    private val _questionLiveData = MutableLiveData<Question>()
    val questionLiveData: LiveData<Question>
        get() = _questionLiveData

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    var category = ""
    private var questionList = listOf<Question>()
    private var currentQuestion = 0

    val incorrectQuestions = mutableListOf<Question>()

    var previousGrade = 0.0

    fun setPreviousGrade(grade: String) {
        previousGrade = grade.toDouble()
    }

    fun getQuestionListSize() = questionList.size
    fun getCurrentQuestionIndex() = currentQuestion + 1

    fun refreshQuestionList(list: List<Question>) {
        _uiState.value = UIState.LOADING
        incorrectQuestions.clear()
        viewModelScope.launch {
            delay(2000)
            currentQuestion = 0
            questionList = list.shuffled()
            _questionLiveData.postValue(questionList[0])
            _uiState.postValue(UIState.LOADED)
        }
    }

    fun correctAnswer(yes: Boolean) {
        _uiState.value = if (yes) {
            UIState.CORRECT
        } else {
            UIState.WRONG
        }
    }

    fun getNextQuestion() {
        if (uiState.value == UIState.WRONG) {
            incorrectQuestions.add(questionLiveData.value!!)
        }
        _uiState.value = UIState.LOADING
        viewModelScope.launch {
            delay(2000)
            if (questionList.last() == questionList[currentQuestion]) {
                _uiState.postValue(UIState.RESULTS)
            } else {
                _questionLiveData.postValue(questionList[++currentQuestion])
                _uiState.postValue(UIState.LOADED)
            }
        }
    }

    fun gradeOverView(): GradeOverview =
        when {
            previousGrade == 0.0 -> GradeOverview.FIRST
            getCurrentGrade() > previousGrade.toBigDecimal() -> GradeOverview.BETTER
            getCurrentGrade() < previousGrade.toBigDecimal() -> GradeOverview.WORSE
            else -> GradeOverview.SAME
        }

    fun getCurrentGrade(): BigDecimal = (((questionList.size.toDouble() - incorrectQuestions.size.toDouble()) / questionList.size.toDouble()) * 100.00)
        .toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
}

