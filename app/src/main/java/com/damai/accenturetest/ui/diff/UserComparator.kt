package com.damai.accenturetest.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 11/July/2023
 */
object UserComparator : DiffUtil.ItemCallback<UserDetailsModel>() {

    override fun areItemsTheSame(oldItem: UserDetailsModel, newItem: UserDetailsModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserDetailsModel, newItem: UserDetailsModel): Boolean {
        return oldItem == newItem
    }
}