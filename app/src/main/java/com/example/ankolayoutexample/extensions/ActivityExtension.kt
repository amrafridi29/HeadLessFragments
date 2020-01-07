package com.example.ankolayoutexample.extensions

import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ankolayoutexample.activityResult.FileType
import com.example.ankolayoutexample.activityResult.IntentType
import com.example.ankolayoutexample.activityResult.OnResultData
import com.example.ankolayoutexample.activityResult.RuntimeActivityResult
import com.example.ankolayoutexample.network.NetworkHelper
import java.io.File

fun <T : Fragment> AppCompatActivity.replaceFragment(
    fragment : T,
    tag : String  = fragment::class.java.simpleName,
    @IdRes containerViewId : Int = android.R.id.content,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): T{
    val ft = supportFragmentManager.beginTransaction()
    ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
    ft.replace(containerViewId, fragment, tag)
    if (addToBackStack) {
        ft.addToBackStack(tag)
    }
    if (supportFragmentManager.isStateSaved) {
        if (commitNow) ft.commitNowAllowingStateLoss() else ft.commitAllowingStateLoss()
    } else {
        if (commitNow) ft.commitNow() else ft.commit()
    }
    return fragment
}


fun <T : Fragment> AppCompatActivity.addFragment(
    fragment: T,
    tag: String = fragment::class.java.simpleName,
    @IdRes containerViewId: Int = android.R.id.content,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): T {
    return (findFragmentByTag(tag) as T?) ?: fragment.also {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            add(containerViewId, it, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            if (supportFragmentManager.isStateSaved) {
                if (commitNow) commitNowAllowingStateLoss() else commitAllowingStateLoss()
            } else {
                if (commitNow) commitNow() else commit()
            }
        }
    }
}


fun AppCompatActivity.findFragmentByTag(tag: String): androidx.fragment.app.Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

fun AppCompatActivity.removeFragmentByTag(tag : String){
    findFragmentByTag(tag)?.let {
        supportFragmentManager.beginTransaction()
            .remove(it)
            .commitNow()
    }
}

fun AppCompatActivity.setOnNetworkConnectivityListener(lambda : ((isConnected : Boolean)-> Unit)?){
    var networkHelper = findFragmentByTag(NetworkHelper.TAG)
    if (networkHelper == null) {
        networkHelper = NetworkHelper.newInstance(lambda)
        addFragment(networkHelper, tag = NetworkHelper.TAG)

    }
}

fun AppCompatActivity.startActivityForResult(intent : Intent, OnResultCallback : ((onResultData : OnResultData)-> Unit)?){
    RuntimeActivityResult(this, IntentType.Other(intent) , OnResultCallback).ask()
}

fun AppCompatActivity.takeCameraPictureResult(authority : String, storageDirectory : File? = null, OnResultCallback : ((onResultData : OnResultData)-> Unit)?){
    RuntimeActivityResult(this, IntentType.CameraImage(authority, storageDirectory) , OnResultCallback).ask()
}

fun AppCompatActivity.takeCameraVideoResult(OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    RuntimeActivityResult(this , IntentType.CameraVideo, OnResultCallback).ask()
}

fun AppCompatActivity.pickFileFromGallery(fileType: FileType , isMultiSelect : Boolean = false, OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    RuntimeActivityResult(this, IntentType.Gallery(fileType , isMultiSelect) , OnResultCallback).ask()

}

fun AppCompatActivity.requestOverlayPermission(OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    RuntimeActivityResult(this , IntentType.OverlayPermission, OnResultCallback).ask()
}

fun AppCompatActivity.requestWriteSettingsPermission(OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    RuntimeActivityResult(this , IntentType.WriteSettingsPermission, OnResultCallback).ask()
}

fun AppCompatActivity.requestUsageAccessSettingsPermission(OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    RuntimeActivityResult(this , IntentType.AccessUsageSettingsPermission, OnResultCallback).ask()
}



