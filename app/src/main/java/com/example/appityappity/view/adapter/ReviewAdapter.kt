package com.example.appityappity.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.appityappity.databinding.ReviewRowItemBinding
import com.example.appityappity.model.Question

class ReviewAdapter(
    private val incorrectList: List<Question>
    ): RecyclerView.Adapter<ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewViewHolder(
            ReviewRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context
        )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.onBind(incorrectList[position])
    }

    override fun getItemCount() = incorrectList.size
}

class ReviewViewHolder(
    private val binding: ReviewRowItemBinding,
    private val context: Context)
    : RecyclerView.ViewHolder(binding.root) {

        fun onBind(question: Question) {
            binding.tvQuestionCard.text = question.question
            binding.btnDetails.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle(question.question)
                    .setMessage("Correct Answer: ${question.correctAnswer}")
                    .show()
            }
        }


}