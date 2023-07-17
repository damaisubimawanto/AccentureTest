package com.damai.base.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewbinding.ViewBinding
import com.damai.base.glide.GlideApp

/**
 * Created by damai007 on 11/July/2023
 */

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

inline fun <T : ViewBinding> ViewGroup.viewBinding(bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T) =
    bindingInflater(LayoutInflater.from(context), this, false)

fun View.setCustomOnClickListener(listener: View.OnClickListener) {
    setOnClickListener(object : View.OnClickListener {
        var lastTimeClicked = 0L

        override fun onClick(p0: View?) {
            if (System.currentTimeMillis() - lastTimeClicked > 1_500L) {
                lastTimeClicked = System.currentTimeMillis()
                listener.onClick(p0)
            }
        }
    })
}

fun AppCompatImageView.loadImageWithCenterCrop(
    url: String?
) {
    GlideApp.with(context)
        .load(url)
        .into(this)
}