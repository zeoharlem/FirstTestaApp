package com.zeoharlem.testaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeoharlem.testaapp.databinding.TestItemRowBinding
import com.zeoharlem.testaapp.extensions.toLocalPrice
import com.zeoharlem.testaapp.extensions.toWordTitleCase
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests
import java.text.NumberFormat

//class TestSearchAdapter: PagingDataAdapter<Tests, TestSearchAdapter.ViewHolder>(TestSearchDiffUtil) {
class TestSearchAdapter : ListAdapter<CartMenu<Tests>, TestSearchAdapter.ViewHolder>(TestSearchDiffUtil) {
    lateinit var binding: TestItemRowBinding
    var addItemCheckEvent: ((CartMenu<Tests>) -> Unit)? = null
    var removeItemCheckEvent: ((CartMenu<Tests>) -> Unit)? = null

    companion object TestSearchDiffUtil : ItemCallback<CartMenu<Tests>>() {
        override fun areItemsTheSame(oldItem: CartMenu<Tests>, newItem: CartMenu<Tests>): Boolean {
            return oldItem.quantity == newItem.quantity
        }

        override fun areContentsTheSame(oldItem: CartMenu<Tests>, newItem: CartMenu<Tests>): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val view: TestItemRowBinding) : RecyclerView.ViewHolder(view.root), OnClickListener {
        init {
            view.cardTestTv.setOnClickListener(this)
        }

        fun binding(tests: CartMenu<Tests>) {
            view.let {
                it.testPrice.text = tests.dataType.price.toLocalPrice()
                it.testTitleTv.text = tests.dataType.title.toWordTitleCase()
            }
        }

        override fun onClick(p0: View?) {
            with(view.cardTestTv) {
                this.toggle()
                val testData = getItem(bindingAdapterPosition)
                if (isChecked) addItemCheckEvent?.invoke(testData)
                else removeItemCheckEvent?.invoke(testData)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binding(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TestItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }
}