package com.example.ankolayoutexample.extensions

import android.Manifest
import android.app.Activity
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.florent37.runtimepermission.kotlin.askPermission
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.net.Uri
import com.example.ankolayoutexample.activityResult.OverlyPermissionResult
import com.example.ankolayoutexample.activityResult.WriteSettingsPermissionResult


fun AppCompatActivity.askGalleryPermission(Granted: (() -> Unit)) {
    askPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) {
        Granted.invoke()
    }.onDeclined { e ->
        if (e.hasDenied()) {
            alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again") { dialog ->
                    dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if (e.hasForeverDenied())
            e.goToSettings()
    }
}

fun AppCompatActivity.askCameraPermission(Granted: (() -> Unit)) {
    askPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    ) {
        Granted.invoke()
    }.onDeclined { e ->
        if (e.hasDenied()) {
            alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again") { dialog ->
                    dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if (e.hasForeverDenied())
            e.goToSettings()
    }
}

fun Fragment.askGalleryPermission(Granted: (() -> Unit)) {
    askPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) {
        Granted.invoke()
    }.onDeclined { e ->
        if (e.hasDenied()) {
            activity?.alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again") { dialog ->
                    dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if (e.hasForeverDenied())
            e.goToSettings()
    }
}

fun Fragment.askCameraPermission(Granted: (() -> Unit)) {
    askPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    ) {
        Granted.invoke()
    }.onDeclined { e ->
        if (e.hasDenied()) {
            activity?.alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again") { dialog ->
                    dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if (e.hasForeverDenied())
            e.goToSettings()
    }
}

fun AppCompatActivity.hasOverlayPermission(Granted: (result : OverlyPermissionResult) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission {
                Granted.invoke(OverlyPermissionResult(Settings.canDrawOverlays(this), it))
            }
        } else {
            Granted.invoke(OverlyPermissionResult(true))
        }
    } else {
        Granted.invoke(OverlyPermissionResult(true))
    }

}

fun AppCompatActivity.hasWriteSettingPermission(Granted: (result : WriteSettingsPermissionResult) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.System.canWrite(this)) {
            Granted.invoke(WriteSettingsPermissionResult(true))
        } else {
            requestWriteSettingsPermission {
                Granted.invoke(WriteSettingsPermissionResult(Settings.System.canWrite(this), it))
            }
        }
    }else{
        Granted.invoke(WriteSettingsPermissionResult(true))
    }
}

fun Fragment.hasOverlayPermission(Granted: (result : OverlyPermissionResult) -> Unit){
    activity?.let {
        (it as AppCompatActivity).hasOverlayPermission(Granted)
    }
}

fun Fragment.hasWriteSettingPermission(Granted: (result : WriteSettingsPermissionResult) -> Unit){
    activity?.let {
        (it as AppCompatActivity).hasWriteSettingPermission(Granted)
    }
}