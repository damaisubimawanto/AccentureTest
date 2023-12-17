package com.damai.accenturetest.ui.home.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damai.accenturetest.databinding.ItemRvFooterUserBinding
import com.damai.base.extensions.setCustomOnClickListener
import com.damai.base.extensions.viewBinding

/**
 * Created by damai007 on 17/December/2023
 */
class UserFooterStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<UserFooterStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            binding = parent.viewBinding(ItemRvFooterUserBinding::inflate)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState = loadState)
    }

    inner class ViewHolder(
        private val binding: ItemRvFooterUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    tvErrorMessage.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                tvErrorMessage.isVisible = loadState is LoadState.Error

                btnRetry.setCustomOnClickListener {
                    retry.invoke()
                }
            }
        }
    }
}