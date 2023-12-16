package com.damai.base.extensions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide

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
    Glide.with(context)
        .load(url)
        .fitCenter()
        .into(this)
}

fun AppCompatEditText.addOnTextChanged(callback: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            callback.invoke(p0?.toString().orEmpty().trim())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {}
    })
}

fun AppCompatEditText.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun AppCompatEditText.showSoftKeyboard() {
    if (requestFocus()) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}