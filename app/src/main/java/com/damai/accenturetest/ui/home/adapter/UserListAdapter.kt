package com.damai.accenturetest.ui.home.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.damai.accenturetest.databinding.ItemRvUserBinding
import com.damai.accenturetest.ui.home.diff.UserComparator
import com.damai.base.extensions.loadImageWithCenterCrop
import com.damai.base.extensions.orZero
import com.damai.base.extensions.setCustomOnClickListener
import com.damai.base.extensions.viewBinding
import com.damai.domain.models.UserDetailsModel

/**
 * Created by damai007 on 16/December/2023
 */
class UserListAdapter(
    private val callback: (userId: Int, username: String) -> Unit
) : PagingDataAdapter<UserDetailsModel, UserListAdapter.ViewHolder>(
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
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserDetailsModel?) {
            with(binding) {
                tvUserName.text = data?.username
                tvUserGithubUrl.text = data?.htmlUrl

                ivUserPhoto.loadImageWithCenterCrop(url = data?.avatarUrl)

                clMainItemLayout.setCustomOnClickListener {
                    callback.invoke(
                        data?.id.orZero(),
                        data?.username.orEmpty()
                    )
                }
            }
        }
    }
}