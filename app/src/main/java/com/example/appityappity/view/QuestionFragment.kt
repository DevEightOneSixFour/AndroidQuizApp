package com.example.appityappity.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.appityappity.R
import com.example.appityappity.databinding.FragmentQuestionBinding
import com.example.appityappity.model.Question
import com.example.appityappity.model.UIState
import com.example.appityappity.utils.OnRadioButtonClickListener
import com.example.appityappity.viewmodel.QuestionViewModel

class QuestionFragment : Fragment(), OnRadioButtonClickListener {

    lateinit var binding: FragmentQuestionBinding
    private lateinit var viewModel: QuestionViewModel
    private val answers = mutableListOf<String>()
    private var selected = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }

        binding.rgAnswerGroup.setOnCheckedChangeListener { group, checkedId ->
            selected = when(checkedId){
                R.id.rbtn_answer_1 ->
                    binding.root.findViewById<RadioButton>(binding.rgAnswerGroup.checkedRadioButtonId).text.toString()
                R.id.rbtn_answer_2 ->
                    binding.root.findViewById<RadioButton>(binding.rgAnswerGroup.checkedRadioButtonId).text.toString()
                R.id.rbtn_answer_3 ->
                    binding.root.findViewById<RadioButton>(binding.rgAnswerGroup.checkedRadioButtonId).text.toString()
                R.id.rbtn_answer_4 ->
                    binding.root.findViewById<RadioButton>(binding.rgAnswerGroup.checkedRadioButtonId).text.toString()
                else -> ""
            }
        }

        configureObservers()
    }

    private fun checkAnswer() {
        if (selected.isEmpty()){
            binding.tvConfirmation.text = resources.getString(R.string.empty_answer)
        } else {
//            val radioButton : RadioButton =
//                binding.root.findViewById(binding.rgAnswerGroup.checkedRadioButtonId)
//            selected = radioButton.text.toString()
            if (viewModel.uiState.value == UIState.WRONG ||
                viewModel.uiState.value == UIState.CORRECT) {
                viewModel.getNextQuestion()
            } else if (selected == viewModel.questionLiveData.value?.correctAnswer) {
                viewModel.correctAnswer(true)
            } else {
                viewModel.correctAnswer(false)
            }
        }
    }

    private fun randomizeAnswers(question: Question) {
        answers.clear()
        answers.add(question.correctAnswer) // clear answers and get correct one
        answers.addAll(question.incorrectAnswers!!)

        answers.shuffle()

        binding.apply {
            tvQuizTitle.text = question.category
            tvQuestion.text = question.question
            rbtnAnswer1.text = answers[0]
            rbtnAnswer2.text = answers[1]
            rbtnAnswer3.text = answers[2]
            rbtnAnswer4.text = answers[3]
        }
    }

    private fun configureObservers() {
        viewModel.questionLiveData.observe(viewLifecycleOwner, { question ->
            randomizeAnswers(question)
        })

        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            when (uiState) {
                UIState.LOADING -> {
                    binding.apply {
                        tvQuizTitle.visibility = View.GONE
                        tvQuestion.visibility = View.GONE
                        rgAnswerGroup.clearCheck()
                        rgAnswerGroup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        btnSubmit.visibility = View.GONE
                        tvConfirmation.text = ""
                        root.setBackgroundColor(resources.getColor(R.color.white, null))
                    }
                }
                UIState.LOADED -> {
                    binding.apply {
                        tvQuizTitle.visibility = View.VISIBLE
                        tvQuestion.visibility = View.VISIBLE
                        rgAnswerGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        flipRadioGroup(true)
                        btnSubmit.text = resources.getString(R.string.submit)
                        btnSubmit.visibility = View.VISIBLE
                    }
                }
                UIState.CORRECT -> {
                    binding.apply {
                        root.setBackgroundColor(resources.getColor(R.color.bright_green, null))
                        tvConfirmation.text = resources.getString(R.string.good_job)
                        tvConfirmation.visibility = View.VISIBLE
                        flipRadioGroup(false)
                        btnSubmit.text = resources.getString(R.string.next)
                    }
                }
                UIState.WRONG -> {
                    binding.apply {
                        root.setBackgroundColor(resources.getColor(R.color.bright_red, null))
                        tvConfirmation.text = resources.getString(R.string.unfortunate)
                        tvConfirmation.visibility = View.VISIBLE
                        flipRadioGroup(false)
                        btnSubmit.text = resources.getString(R.string.next)
                    }
                }
                UIState.RESULTS -> {
                    binding.root.findNavController().navigate(
                        QuestionFragmentDirections.actionNavQuestionsToNavResults()
                    )
                }
            }
        })
    }


    private fun flipRadioGroup(flag: Boolean) {
        binding.rbtnAnswer1.isEnabled = flag
        binding.rbtnAnswer2.isEnabled = flag
        binding.rbtnAnswer3.isEnabled = flag
        binding.rbtnAnswer4.isEnabled = flag
    }

    override fun onRadioButtonClick(btn: RadioButton) {
        selected = btn.text.toString()
    }
}