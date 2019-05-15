package com.support.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

// using this class

//permissionGranted = PermissionUtil.getPermission(activity, PermissionUtil.Permissions.READ_PHONE_STATE,
//        PermissionUtil.PermissionCode.READ_PHONE_STATE,
//        PermissionUtil.PermissionMessage.READ_PHONE_STATE,
//        ResourceUtils.getString(R.string.phone_state_permission_message));

//
//@Override
//public void onRequestPermissionsResult(int requestCode,@NonNull String[]permissions,@NonNull int[]grantResults){
//        switch(requestCode){
//        case RunTimePermission.PermissionCode.READ_PHONE_STATE:
//        if(grantResults.length>0&&grantResults[0]==PackageManager
//        .PERMISSION_GRANTED){
//        // do code after permission is given
//        }
//        break;
//        }
//        }

object PermissionUtil {
    @RequiresApi(Build.VERSION_CODES.M)
    fun getPermission(
        activity: Activity?,
        permission: String,
        permissionCode: Int,
        permissionDialogTitle: String,
        permissionDialogMessage: String
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager
                    .PERMISSION_GRANTED
            ) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    val alertBuilder = AlertDialog.Builder(activity)
                    alertBuilder.setCancelable(true)
                    if (TextUtils.isEmpty(permissionDialogMessage)) {
                        alertBuilder.setMessage(permissionDialogTitle)
                    } else {
                        alertBuilder.setTitle(permissionDialogTitle)
                        alertBuilder.setMessage(permissionDialogMessage)
                    }
                    alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(permission),
                            permissionCode
                        )
                    }

                    val alert = alertBuilder.create()
                    alert.show()

                } else {
                    activity.requestPermissions(
                        arrayOf(permission),
                        permissionCode
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun getPermission(
        fragment: Fragment,
        permission: String,
        permissionCode: Int,
        permissionDialogTitle: String,
        permissionDialogMessage: String
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(fragment.context!!, permission) != PackageManager
                    .PERMISSION_GRANTED
            ) {
                if (fragment.shouldShowRequestPermissionRationale(permission)) {
                    val alertBuilder = AlertDialog.Builder(fragment.context!!)
                    alertBuilder.setCancelable(true)
                    if (TextUtils.isEmpty(permissionDialogMessage)) {
                        alertBuilder.setMessage(permissionDialogTitle)
                    } else {
                        alertBuilder.setTitle(permissionDialogTitle)
                        alertBuilder.setMessage(permissionDialogMessage)
                    }
                    alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        fragment.requestPermissions(
                            arrayOf(permission),
                            permissionCode
                        )
                    }

                    val alert = alertBuilder.create()
                    alert.show()

                } else {
                    fragment.requestPermissions(
                        arrayOf(permission),
                        permissionCode
                    )
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun checkPermission(context: Context?, permission: String): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(
            context!!,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    interface PermissionMessage {
        companion object {
            val READ_PHONE_STATE = ""
            val WRITE_EXTERNAL_STORAGE = ""
            val CAMERA = ""
        }
    }

    interface Permissions {
        companion object {
            val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
            val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
            val CAMERA = Manifest.permission.CAMERA
        }
    }

    interface PermissionCode {
        companion object {
            val READ_PHONE_STATE = 1002
            val WRITE_EXTERNAL_STORAGE = 1003
            val CAMERA = 1004
        }
    }
}
