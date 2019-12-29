package com.example.ankolayoutexample.extensions

import android.content.Intent
import android.provider.MediaStore
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ankolayoutexample.activityResult.FileType
import com.example.ankolayoutexample.activityResult.OnResultData
import com.example.ankolayoutexample.activityResult.RuntimeActivityResult
import com.example.ankolayoutexample.network.NetworkHelper
import java.io.File

fun <T : Fragment> Fragment.replaceFragment(
    fragment: T,
    tag: String = fragment::class.java.simpleName,
    @IdRes containerViewId: Int,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): T {
    if (isAdded) {
        val ft = childFragmentManager.beginTransaction()
        ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft.replace(containerViewId, fragment, tag)
        activity?.let {
            if (addToBackStack) {
                ft.addToBackStack(tag)
            }
            if (childFragmentManager.isStateSaved) {
                if (commitNow) ft.commitNowAllowingStateLoss() else ft.commitAllowingStateLoss()
            } else {
                if (commitNow) ft.commitNow() else ft.commit()
            }
        }
    }
    return fragment
}


fun <T : Fragment> Fragment.addFragment(
    fragment: T,
    tag: String = fragment::class.java.simpleName,
    @IdRes containerViewId: Int = 0,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0,
    commitNow: Boolean = false,
    addToBackStack: Boolean = false
): T {
    return (findFragmentByTag(tag) as T?) ?: fragment.also {
        childFragmentManager.beginTransaction().apply {
            setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            if (containerViewId == 0)
                add(it, tag)
             else
                add(containerViewId, it, tag)

            if (addToBackStack) {
                addToBackStack(tag)
            }
            if (childFragmentManager.isStateSaved) {
                if (commitNow) commitNowAllowingStateLoss() else commitAllowingStateLoss()
            } else {
                if (commitNow) commitNow() else commit()
            }
        }
    }
}


fun Fragment.findFragmentByTag(tag: String): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}

fun Fragment.removeFragmentByTag(tag : String){
    findFragmentByTag(tag)?.let {
        childFragmentManager.beginTransaction()
            .remove(it)
            .commitNow()
    }
}

fun Fragment.setOnNetworkConnectivityListener(lambda : ((isConnected : Boolean)-> Unit)?){
    var networkHelper = findFragmentByTag(NetworkHelper.TAG)
    if (networkHelper == null) {
        networkHelper = NetworkHelper.newInstance(lambda)
        addFragment(networkHelper ,  tag = NetworkHelper.TAG)

    }
}

fun Fragment.startActivityForResult(intent : Intent, OnResultCallback : ((onResultData : OnResultData)-> Unit)?){
   activity?.let {
       (it as AppCompatActivity).startActivityForResult(intent , OnResultCallback)
   }
}

fun Fragment.takeCameraPictureResult(authority : String, storageDirectory : File? = null, OnResultCallback : ((onResultData : OnResultData)-> Unit)?){
  activity?.let {
      (it as AppCompatActivity).takeCameraPictureResult(authority, storageDirectory, OnResultCallback)
  }
}


fun Fragment.takeCameraVideoResult(OnResultCallback : ((onResultData : OnResultData)-> Unit)?){
    activity?.let {
        (it as AppCompatActivity).takeCameraVideoResult(OnResultCallback)
    }
}

fun Fragment.pickFileFromGallery(fileType: FileType , isMultiSelect : Boolean = false, OnResultCallback: ((onResultData: OnResultData) -> Unit)?){
    activity?.let {
        (it as AppCompatActivity).pickFileFromGallery(fileType, isMultiSelect, OnResultCallback)
    }

}