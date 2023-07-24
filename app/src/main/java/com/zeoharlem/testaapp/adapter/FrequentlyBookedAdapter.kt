package com.zeoharlem.testaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zeoharlem.testaapp.databinding.CategoryItemBinding
import com.zeoharlem.testaapp.databinding.FrequentlyBookedItemBinding
import com.zeoharlem.testaapp.extensions.toWordTitleCase
import com.zeoharlem.testaapp.models.Category

class FrequentlyBookedAdapter : ListAdapter<Category, FrequentlyBookedAdapter.CategoryViewHolder>(CategoryDiffUtil) {

    companion object CategoryDiffUtil : ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    inner class CategoryViewHolder(private val viewBinding: FrequentlyBookedItemBinding) :
        ViewHolder(viewBinding.root) {

        fun binding(category: Category) {
            viewBinding.categoryTitle.text = category.categoryTitle.toWordTitleCase()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = FrequentlyBookedItemBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding(getItem(position))
    }
}