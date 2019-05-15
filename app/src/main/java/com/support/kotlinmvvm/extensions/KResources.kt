package com.support.kotlinmvvm.extensions

import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

fun Context.color(@ColorRes clr: Int): Int {
    return ContextCompat.getColor(this, clr)
}

fun Context.string(@StringRes str: Int): String {
    return getString(str)
}

fun Context.drawable(@DrawableRes drw: Int): Drawable? {
    return ContextCompat.getDrawable(this, drw)
}

fun Context.font(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}

fun Context.stringArray(array: Int): Array<String> {
    return resources.getStringArray(array)
}

fun Context.intArray(array: Int): IntArray {
    return resources.getIntArray(array)
}

fun Fragment.color(@ColorRes clr: Int): Int? {
    return activity?.color(clr)
}

fun Fragment.string(@StringRes str: Int): String? {
    return activity?.string(str)
}

fun Fragment.drawable(@DrawableRes drw: Int): Drawable? {
    return activity?.drawable(drw)
}

fun Fragment.font(@FontRes font: Int): Typeface? {
    return activity?.font(font)
}

fun Fragment.stringArray(array: Int): Array<String>? {
    return activity?.stringArray(array)
}

fun Fragment.intArray(array: Int): IntArray? {
    return activity?.intArray(array)
}

fun Activity.screenWidth(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun Activity.screenHeight(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

fun convertPxToDp(px: Int): Int {
    return Math.round(px / (Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}
