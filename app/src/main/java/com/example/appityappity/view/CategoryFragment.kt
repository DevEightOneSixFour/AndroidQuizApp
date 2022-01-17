package com.example.appityappity.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appityappity.R
import com.example.appityappity.databinding.FragmentCategoriesBinding
import com.example.appityappity.model.Category
import com.example.appityappity.model.Question
import com.example.appityappity.utils.Convertor
import com.example.appityappity.utils.OnListItemClickListener
import com.example.appityappity.utils.TEST_SCORES
import com.example.appityappity.utils.toPrefFormat
import com.example.appityappity.view.adapter.CategoryAdapter
import com.example.appityappity.viewmodel.QuestionViewModel

class CategoryFragment : Fragment(), OnListItemClickListener {

    lateinit var binding: FragmentCategoriesBinding
    private val convert = Convertor()
    private val categoryList = mutableListOf<Category>()
    private lateinit var categories: Array<String>
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[QuestionViewModel::class.java]
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categories = resources.getStringArray(R.array.categories)
        sharedPreference = context?.getSharedPreferences(
            TEST_SCORES,
            Context.MODE_PRIVATE
        )!!

        if (categoryList.isEmpty()) {
            createCategoryList(categories)
        }
        configureRecyclerView()
    }

    private fun createCategoryList(array: Array<String>) {
        for ((count, element) in array.withIndex()) {
            categoryList.add(
                Category(
                    title = element,
                    color = when (count) {
                        0 -> resources.getColor(R.color.forrest_green, null)
                        1 -> resources.getColor(R.color.teal_200, null)
                        2 -> resources.getColor(R.color.cyan, null)
                        3 -> resources.getColor(R.color.light_pink, null)
                        4 -> resources.getColor(R.color.mild_green, null)
                        5 -> resources.getColor(R.color.light_brownish_orange, null)
                        6 -> resources.getColor(R.color.purple_500, null)
                        7 -> resources.getColor(R.color.purple_200, null)
                        8 -> resources.getColor(R.color.brownish_orange, null)
                        9 -> resources.getColor(R.color.magenta, null)
                        else -> resources.getColor(R.color.black, null)
                    },
                    score = sharedPreference.getString(element.toPrefFormat(), "0.00").toString()
                )
            )
        }
    }

    private fun configureRecyclerView() {
        binding.rvCategories.adapter = CategoryAdapter(categoryList, requireContext(), this)
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onClickListItem(filename: String) {
        sharedPreference.getString(filename, "0.00")?.let { viewModel.setPreviousGrade(it) }
        viewModel.category = filename
        viewModel.refreshQuestionList(
            convert.readJsonFile(requireContext(), "${filename}.json").orEmpty()
        )
        binding.root.findNavController().navigate(
            CategoryFragmentDirections.actionNavCategoriesToNavQuestions()
        )
    }
}