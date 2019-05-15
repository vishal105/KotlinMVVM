package com.support.kotlinmvvm.extensions

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.alertDialog(
    @StringRes message: Int,
    @StringRes title: Int? = null,
    init: (AlertDialogBuilder.() -> Unit)? = null
) = activity?.alertDialog(message, title, init)

fun Context.alertDialog(
    @StringRes message: Int,
    @StringRes title: Int? = null,
    init: (AlertDialogBuilder.() -> Unit)? = null
) = AlertDialogBuilder(this).apply {
    if (title != null) title(title)
    message(message)
    if (init != null) init()
}

fun Fragment.alertDialog(init: AlertDialogBuilder.() -> Unit): AlertDialogBuilder? = activity?.alertDialog(init)

fun Context.alertDialog(init: AlertDialogBuilder.() -> Unit) = AlertDialogBuilder(this).apply(init)

class AlertDialogBuilder(val context: Context) {

    var builder: AlertDialog.Builder? = null
    var dialog: AlertDialog? = null

    init {
        builder = AlertDialog.Builder(context)
    }

    fun dismiss() = dialog?.dismiss()

    fun show(): AlertDialogBuilder {
        dialog = builder?.create()
        dialog!!.show()
        return this
    }

    fun title(title: CharSequence) {
        builder?.setTitle(title)
    }

    fun title(@StringRes resource: Int) {
        builder?.setTitle(resource)
    }

    fun message(title: CharSequence) {
        builder?.setMessage(title)
    }

    fun message(@StringRes resource: Int) {
        builder?.setMessage(resource)
    }

    fun icon(@DrawableRes icon: Int) {
        builder?.setIcon(icon)
    }

    fun icon(icon: Drawable) {
        builder?.setIcon(icon)
    }

    fun customTitle(title: View) {
        builder?.setCustomTitle(title)
    }

    fun customView(view: View) {
        builder?.setView(view)
    }

    fun cancelable(value: Boolean = true) {
        builder?.setCancelable(value)
    }

    fun onCancel(f: () -> Unit) {
        builder?.setOnCancelListener { f() }
    }

    fun neutralButton(@StringRes textResource: Int = android.R.string.ok, f: DialogInterface.() -> Unit = { dismiss() }) {
        neutralButton(context.getString(textResource), f)
    }

    fun neutralButton(title: String, f: DialogInterface.() -> Unit = { dismiss() }) {
        builder?.setNeutralButton(title, { dialog, _ -> dialog.f() })
    }

    fun positiveButton(@StringRes textResource: Int = android.R.string.ok, f: DialogInterface.() -> Unit) {
        positiveButton(context.getString(textResource), f)
    }

    fun positiveButton(title: String, f: DialogInterface.() -> Unit) {
        builder?.setPositiveButton(title, { dialog, _ -> dialog.f() })
    }

    fun negativeButton(@StringRes textResource: Int = android.R.string.cancel, f: DialogInterface.() -> Unit = { dismiss() }) {
        negativeButton(context.getString(textResource), f)
    }

    fun negativeButton(title: String, f: DialogInterface.() -> Unit = { dismiss() }) {
        builder?.setNegativeButton(title, { dialog, _ -> dialog.f() })
    }

}

