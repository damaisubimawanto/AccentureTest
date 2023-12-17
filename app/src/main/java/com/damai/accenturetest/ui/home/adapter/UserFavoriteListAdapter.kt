package com.damai.accenturetest.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damai.accenturetest.databinding.ItemRvUserBinding
import com.damai.accenturetest.ui.home.diff.UserComparator
import com.damai.base.extensions.viewBinding
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 16/December/2023
 */
class UserFavoriteListAdapter : ListAdapter<UserDetailsModel, UserFavoriteListAdapter.ViewHolder>(
    UserComparator
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = parent.viewBinding(ItemRvUserBinding::inflate)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data = currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemRvUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserDetailsModel) {
            with(binding) {
                tvUserName.text = data.username
            }
        }
    }
}