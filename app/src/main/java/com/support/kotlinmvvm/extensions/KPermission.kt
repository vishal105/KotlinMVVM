package com.support.kotlinmvvm.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/*
val permissions =
            arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        coreFragment?.requestPermissionsIfRequired(permissions, object : PermissionCallBack {
            override fun permissionGranted() {
                coreFragment?.let { ImageChooserUtil.startGalleryIntent(it) }
            }

            override fun permissionDenied() {
                coreActivity.customAlertDialog(
                    icon = null,
                    title = ResourceUtils.getString(R.string.permission),
                    message = ResourceUtils.getString(R.string.write_external_storage_title),
                    positiveAlertButton = PositiveAlertButton(ResourceUtils.getString(R.string.allow)) {
                        onGallery()
                    },
                    negativeAlertButton = NegativeAlertButton(ResourceUtils.getString(R.string.dont_allow))
                )
            }

            override fun onPermissionDisabled() {
                coreActivity.customAlertDialog(
                    icon = null,
                    title = ResourceUtils.getString(R.string.permission),
                    message = ResourceUtils.getString(R.string.write_external_storage_title),
                    positiveAlertButton = PositiveAlertButton(ResourceUtils.getString(R.string.settings)) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", coreFragment?.context?.packageName, null)
                        intent.data = uri
                        coreActivity.startActivity(intent)
                    },
                    negativeAlertButton = NegativeAlertButton(ResourceUtils.getString(R.string.cancel))
                )
            }
        })
 */

interface PermissionCallBack {
    fun permissionGranted()
    fun permissionDenied()
    /**
     * Callback on permission "Never show again" checked and denied
     * */
    fun onPermissionDisabled()
}

fun Activity.checkPermissionRationale(permissions: Array<out String>): Boolean {
    var result = true
    for (permission in permissions) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            result = false
            break
        }
    }
    return result
}

fun Context.checkSelfPermissions(permissions: ArrayList<String>): Boolean {
    return permissions.none { ContextCompat.checkSelfPermission(this.applicationContext, it) != PackageManager.PERMISSION_GRANTED }
}

fun Activity.requestAllPermissions(permissions: ArrayList<out String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
}

fun Fragment.checkSelfPermissions(permissions: ArrayList<String>): Boolean {
    return permissions.none { this.context?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } != PackageManager.PERMISSION_GRANTED }
}

fun Fragment.requestAllPermissions(permissions: ArrayList<out String>, requestCode: Int) {
    requestPermissions(permissions.toTypedArray(), requestCode)
}

fun Fragment.checkPermissionRationale(permissions: Array<out String>): Boolean {
    var result = true
    for (permission in permissions) {
        if (!shouldShowRequestPermissionRationale(permission)) {
            result = false
            break
        }
    }
    return result
}