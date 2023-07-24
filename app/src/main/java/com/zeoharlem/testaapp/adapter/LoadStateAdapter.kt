package com.zeoharlem.testaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.databinding.LoadStateItemBinding

class LoadStateAdapter(
    private val retry: () -> Unit
) : androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)

    inner class LoadStateViewHolder(
        parent: ViewGroup, retry: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false)
    ) {
        private val binding = LoadStateItemBinding.bind(itemView)
        private val progressBar = binding.loadingProgressBar
        private val errorMsg = binding.errorLoadingMessage
        private val retry = binding.retryButton.also {
                it.setOnClickListener { retry() }
            }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }

            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}