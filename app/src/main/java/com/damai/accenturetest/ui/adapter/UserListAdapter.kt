package com.damai.accenturetest.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damai.accenturetest.databinding.ItemRvUserBinding
import com.damai.accenturetest.ui.diff.UserComparator
import com.damai.base.extensions.viewBinding
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 11/July/2023
 */
class UserListAdapter : PagingDataAdapter<UserDetailsModel, UserListAdapter.ViewHolder>(
    diffCallback = UserComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = parent.viewBinding(ItemRvUserBinding::inflate)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data = getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemRvUserBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserDetailsModel?) {
            with(binding) {
                tvUserName.text = data?.username
            }
        }
    }
}