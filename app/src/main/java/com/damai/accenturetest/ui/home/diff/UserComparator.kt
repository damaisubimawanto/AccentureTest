package com.damai.accenturetest.ui.home.diff

import androidx.recyclerview.widget.DiffUtil
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 16/December/2023
 */
object UserComparator : DiffUtil.ItemCallback<UserDetailsModel>() {

    override fun areItemsTheSame(oldItem: UserDetailsModel, newItem: UserDetailsModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserDetailsModel, newItem: UserDetailsModel): Boolean {
        return oldItem == newItem
    }
}