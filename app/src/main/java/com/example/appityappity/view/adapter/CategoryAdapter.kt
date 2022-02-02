package com.example.appityappity.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appityappity.model.Category
import com.example.appityappity.R
import com.example.appityappity.databinding.CatRowItemBinding
import com.example.appityappity.utils.OnListItemClickListener
import com.example.appityappity.utils.toPrefFormat
import java.util.*

class CategoryAdapter(
    private val list: List<Category>,
    private val context: Context,
    private val listener: OnListItemClickListener
): RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CatRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.tvCategory.text = list[position].title
        holder.binding.tvCurrentScore.text =
            context.getString(R.string.current_score_s, list[position].score.toString() + "%")
        holder.binding.cvCatCard.background.setTint(list[position].color)

        val questionFile = list[position].title.toPrefFormat()

        holder.itemView.setOnClickListener {
            listener.onClickListItem(questionFile)
        }
    }

    override fun getItemCount() = list.size
}

class CategoryViewHolder(val binding: CatRowItemBinding): RecyclerView.ViewHolder(binding.root)