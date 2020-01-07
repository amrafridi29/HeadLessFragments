package com.example.ankolayoutexample.extensions

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Process
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ankolayoutexample.activityResult.AccessUsagePermissionResult
import com.example.ankolayoutexample.activityResult.AccessibilityPermissionResult
import com.example.ankolayoutexample.activityResult.OverlyPermissionResult
import com.example.ankolayoutexample.activityResult.WriteSettingsPermissionResult
import com.github.florent37.runtimepermission.kotlin.askPermission
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton


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

fun AppCompatActivity.hasUsageAccessSettingPermission(Granted: (result: AccessUsagePermissionResult) -> Unit){
    if(isUsageAccessGranted()){
        Granted.invoke(AccessUsagePermissionResult(true))
    }else{
        requestUsageAccessSettingsPermission { Granted.invoke(AccessUsagePermissionResult(isUsageAccessGranted(), it)) }
    }
}

fun Fragment.hasUsageAccessSettingPermission(Granted: (result: AccessUsagePermissionResult) -> Unit){
    activity?.let { (it as AppCompatActivity).hasUsageAccessSettingPermission(Granted) }
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

fun AppCompatActivity.isUsageAccessGranted(): Boolean {
    if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
        return false
    }
    val appOpsManager =
        getSystemService(APP_OPS_SERVICE) as AppOpsManager

    val mode = appOpsManager.checkOpNoThrow(
        OPSTR_GET_USAGE_STATS,
        Process.myUid(), packageName
    )
    if (mode != MODE_ALLOWED) {
        return false
    }

    // Verify that access is possible. Some devices "lie" and return MODE_ALLOWED even when it's not.
    // Verify that access is possible. Some devices "lie" and return MODE_ALLOWED even when it's not.
    val now = System.currentTimeMillis()
    val mUsageStatsManager =
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
            getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        } else {
            null
        }
    mUsageStatsManager ?: return false
    val stats =
        mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 10, now)
    return stats != null && stats.isNotEmpty()

   /* return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            OPSTR_GET_USAGE_STATS, Process.myUid(),
            packageName
        )
        mode == MODE_ALLOWED
    } else {
        true
    }*/

}

fun Fragment.isUsageAccessGranted() : Boolean{
    activity ?: return false
    return (activity as AppCompatActivity).isUsageAccessGranted()
}

fun AppCompatActivity.hasAccessibilityPermission(Granted: (result: AccessibilityPermissionResult) -> Unit){
    if(isAccessibilityServiceEnabled()){
        Granted.invoke(AccessibilityPermissionResult(true))
    }else{
        requestAccessibilitySettingsPermission { Granted.invoke(AccessibilityPermissionResult(isAccessibilityServiceEnabled(), it)) }
    }
}

fun Fragment.hasAccessibilityPermission(Granted: (result: AccessibilityPermissionResult) -> Unit) {
    activity?.let {
        (it as AppCompatActivity).hasAccessibilityPermission(Granted)
    }
}

fun AppCompatActivity.isAccessibilityServiceEnabled(): Boolean {
    var accessibilityServiceEnabled = false
    val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val runningServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
    for (service in runningServices) {
        if (service.resolveInfo.serviceInfo.packageName == packageName) {
            accessibilityServiceEnabled = true
        }
    }
    return accessibilityServiceEnabled
}


fun Fragment.isAccessibilityServiceEnabled() : Boolean{
    return (requireActivity()  as AppCompatActivity).isAccessibilityServiceEnabled()
}

fun AppCompatActivity.hasAccessibilityService(){

}