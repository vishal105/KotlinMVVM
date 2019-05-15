package com.support.kotlinmvvm.utils.statelayout

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View

internal fun <T : View> View?.findView(@IdRes id: Int, block: T.() -> Unit) {
    this?.findViewById<T>(id)?.let { block.invoke(it) }
}

internal fun View.inflate(@LayoutRes layoutId: Int): View? {
    return LayoutInflater.from(this.context).inflate(layoutId, null)
}

internal fun View?.visible() = visible { }

internal fun View?.visible(block: (View) -> Unit) {
    this?.apply {
        visibility = View.VISIBLE
        block.invoke(this)
    }
}

internal fun View?.gone() = gone { }

internal fun View?.gone(block: (View) -> Unit) {
    this?.apply {
        visibility = View.GONE
        block.invoke(this)
    }
}