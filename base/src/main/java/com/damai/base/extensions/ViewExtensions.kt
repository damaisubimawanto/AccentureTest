package com.damai.base.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Created by damai007 on 11/July/2023
 */

inline fun <T : ViewBinding> ViewGroup.viewBinding(bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T) =
    bindingInflater(LayoutInflater.from(context), this, false)