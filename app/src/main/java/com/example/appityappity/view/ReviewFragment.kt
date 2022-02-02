package com.example.appityappity.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appityappity.databinding.FragmentReviewBinding
import com.example.appityappity.utils.toTitle
import com.example.appityappity.view.adapter.ReviewAdapter
import com.example.appityappity.view.adapter.ReviewViewHolder
import com.example.appityappity.viewmodel.QuestionViewModel

class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val  viewModel: QuestionViewModel by lazy {
        ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvReviewCategory.text = viewModel.category.toTitle()

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        binding.rvWrongAnswers.adapter = ReviewAdapter(viewModel.incorrectQuestions)
        binding.rvWrongAnswers.layoutManager = LinearLayoutManager(context)
    }
}