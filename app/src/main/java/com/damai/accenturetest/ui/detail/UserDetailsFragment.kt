package com.damai.accenturetest.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.damai.accenturetest.application.MyApplication
import com.damai.accenturetest.databinding.FragmentUserDetailsBinding
import com.damai.base.BaseFragment
import com.damai.base.extensions.gone
import com.damai.base.extensions.loadImageWithCenterCrop
import com.damai.base.extensions.observe
import com.damai.base.extensions.setCustomOnClickListener
import com.damai.base.extensions.showToastMessage
import com.damai.base.extensions.visible
import com.damai.base.utils.Constants.BUNDLE_ARGS_USERNAME
import com.damai.base.utils.EventObserver
import javax.inject.Inject

/**
 * Created by damai007 on 12/July/2023
 */
class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding, UserDetailsViewModel>() {

    @Inject
    override lateinit var viewModel: UserDetailsViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentUserDetailsBinding {
        return FragmentUserDetailsBinding.inflate(inflater, viewGroup, false)
    }

    override fun setupDaggerInjection() {
        (requireContext().applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun FragmentUserDetailsBinding.viewInitialization() {
        arguments?.let { bundle ->
            viewModel.clickedUsername = bundle.getString(BUNDLE_ARGS_USERNAME).orEmpty()
        }
    }

    override fun FragmentUserDetailsBinding.setupListeners() {
        rlFavoriteButton.setCustomOnClickListener {
            viewModel.setUserFavorite()
        }
    }

    override fun FragmentUserDetailsBinding.setupObservers() {
        observe(viewModel.userFavoritedLiveData) { isFavorited ->
            if (isFavorited) {
                ivFavoriteIconSelected.visible()
                ivFavoriteIconUnselected.gone()
            } else {
                ivFavoriteIconSelected.gone()
                ivFavoriteIconUnselected.visible()
            }
        }

        observe(viewModel.userDetailsLiveData, EventObserver {
            tvUserName.text = it.name
            ivPhotoProfile.loadImageWithCenterCrop(url = it.avatarUrl)
        })

        observe(viewModel.userDetailsErrorLiveData, EventObserver {
            requireContext().showToastMessage(message = it)
        })
    }

    override fun FragmentUserDetailsBinding.onPreparationFinished() {
        viewModel.clickedUsername?.let {
            viewModel.getUserDetails(username = it)
        }
    }
}