package com.example.appityappity.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.appityappity.R
import com.example.appityappity.databinding.FragmentQuestionBinding
import com.example.appityappity.model.Question
import com.example.appityappity.model.UIState
import com.example.appityappity.utils.OnRadioButtonClickListener
import com.example.appityappity.utils.toTitle
import com.example.appityappity.viewmodel.QuestionViewModel

class QuestionFragment : Fragment(), OnRadioButtonClickListener {

    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding get() = _binding!!
    private lateinit var viewModel: QuestionViewModel
    private val answers = mutableListOf<String>()
    private var selected = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
        _binding = FragmentQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }

        binding.rgAnswerGroup.setOnCheckedChangeListener { _, checkedId ->
            //TODO refactor this
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
        // clear answers and get correct one
        answers.clear()
        answers.add(question.correctAnswer)
        answers.addAll(question.incorrectAnswers)

        answers.shuffle()

        binding.apply {
            tvQuizTitle.text = viewModel.category.toTitle()
            tvQuestion.text = question.question
            rbtnAnswer1.text = answers[0]
            rbtnAnswer2.text = answers[1]
            rbtnAnswer3.text = answers[2]
            rbtnAnswer4.text = answers[3]
        }
    }

    private fun configureObservers() {
        viewModel.questionLiveData.observe(viewLifecycleOwner) { question ->
            randomizeAnswers(question)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
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
                        tvConfirmation.setBackgroundColor(resources.getColor(R.color.white, null))
                        tvQuestionNumber.visibility = View.GONE
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
                        tvQuestionNumber.text = resources.getString(
                            R.string.question_number,
                            viewModel.getCurrentQuestionIndex().toString(),
                            viewModel.getQuestionListSize().toString()
                        )
                        tvQuestionNumber.visibility = View.VISIBLE
                    }
                }
                UIState.CORRECT -> {
                    binding.apply {
                        tvConfirmation.setBackgroundColor(
                            resources.getColor(
                                R.color.bright_green,
                                null
                            )
                        )
                        tvConfirmation.text = resources.getString(R.string.good_job)
                        tvConfirmation.visibility = View.VISIBLE
                        flipRadioGroup(false)
                        btnSubmit.text = resources.getString(R.string.next)
                    }
                }
                UIState.WRONG -> {
                    binding.apply {
                        tvConfirmation.setBackgroundColor(
                            resources.getColor(
                                R.color.bright_red,
                                null
                            )
                        )
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
                else -> {}
            }
        }
    }


    private fun flipRadioGroup(flag: Boolean) {
        binding.apply {
            rbtnAnswer1.isEnabled = flag
            rbtnAnswer2.isEnabled = flag
            rbtnAnswer3.isEnabled = flag
            rbtnAnswer4.isEnabled = flag
        }
    }

    override fun onRadioButtonClick(btn: RadioButton) {
        selected = btn.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}