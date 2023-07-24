package com.zeoharlem.testaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeoharlem.testaapp.databinding.CartSelectedItemListBinding
import com.zeoharlem.testaapp.databinding.TestItemRowBinding
import com.zeoharlem.testaapp.extensions.toLocalPrice
import com.zeoharlem.testaapp.extensions.toWordTitleCase
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests
import java.text.NumberFormat

class TestCartListAdapter : ListAdapter<CartMenu<Tests>, TestCartListAdapter.ViewHolder>(TestSearchDiffUtil) {
    lateinit var binding: CartSelectedItemListBinding
    //var addItemCheckEvent: ((CartMenu<Tests>) -> Unit)? = null
    var removeItemCheckEvent: ((CartMenu<Tests>) -> Unit)? = null

    companion object TestSearchDiffUtil : ItemCallback<CartMenu<Tests>>() {
        override fun areItemsTheSame(oldItem: CartMenu<Tests>, newItem: CartMenu<Tests>): Boolean {
            return oldItem.dataType.testId == newItem.dataType.testId
        }

        override fun areContentsTheSame(oldItem: CartMenu<Tests>, newItem: CartMenu<Tests>): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val view: CartSelectedItemListBinding) : RecyclerView.ViewHolder(view.root), OnClickListener {
        init {
            view.deleteCartItem.setOnClickListener(this)
        }

        fun binding(tests: CartMenu<Tests>) {
            view.cartSelectedItemTv.text = tests.dataType.title.toWordTitleCase()
        }

        override fun onClick(p0: View?) {
            removeItemCheckEvent?.invoke(getItem(bindingAdapterPosition))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binding(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CartSelectedItemListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }
}