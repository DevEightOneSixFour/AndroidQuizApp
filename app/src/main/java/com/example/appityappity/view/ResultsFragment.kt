package com.example.appityappity.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.appityappity.R
import com.example.appityappity.databinding.FragmentResultsBinding
import com.example.appityappity.model.GradeOverview
import com.example.appityappity.utils.TEST_SCORES
import com.example.appityappity.viewmodel.QuestionViewModel
import java.math.RoundingMode

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding
    private lateinit var viewModel: QuestionViewModel
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
        sharedPreference = context?.getSharedPreferences(
            TEST_SCORES,
            Context.MODE_PRIVATE
        )!!
        binding = FragmentResultsBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGrade.text = "${viewModel.getCurrentGrade().toPlainString()}%"
        binding.tvOverview.text =
            when (viewModel.gradeOverView()) {
                GradeOverview.FIRST -> context?.getString(R.string.ov_first_time)
                GradeOverview.BETTER -> context?.getString(R.string.ov_better)
                GradeOverview.WORSE -> context?.getString(R.string.ov_worse)
                else -> context?.getString(R.string.ov_equal)
            }
        binding.tvLastTime.text =
            "${viewModel.previousGrade
                .toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toPlainString()}%"

        binding.btnReturn.setOnClickListener {
            it.findNavController().navigate(
                ResultsFragmentDirections.actionNavResultsToNavCategories()
            )
        }

        binding.btnReview.setOnClickListener {
            it.findNavController().navigate(
                ResultsFragmentDirections.actionNavResultsToNavReview()
            )
        }

        updateCategoryScore()
    }

    private fun updateCategoryScore() {
        sharedPreference.edit().putString(
            viewModel.category.replace(".json", ""),
            viewModel.getCurrentGrade().toPlainString()
        ).apply()
    }
}