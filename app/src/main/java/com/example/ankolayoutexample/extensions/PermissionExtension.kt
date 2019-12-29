package com.example.ankolayoutexample.extensions

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.florent37.runtimepermission.kotlin.askPermission
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton

fun AppCompatActivity.askGalleryPermission(Granted : (()-> Unit)){
    askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE){
        Granted.invoke()
    }.onDeclined {e->
        if(e.hasDenied()){
            alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again"){
                    dialog -> dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if(e.hasForeverDenied())
            e.goToSettings()
    }
}

fun AppCompatActivity.askCameraPermission(Granted : (()-> Unit)){
    askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.CAMERA){
        Granted.invoke()
    }.onDeclined {e->
        if(e.hasDenied()){
            alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again"){
                        dialog -> dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if(e.hasForeverDenied())
            e.goToSettings()
    }
}

fun Fragment.askGalleryPermission(Granted : (()-> Unit)){
    askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE){
        Granted.invoke()
    }.onDeclined {e->
        if(e.hasDenied()){
            activity?.alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again"){
                        dialog -> dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if(e.hasForeverDenied())
            e.goToSettings()
    }
}

fun Fragment.askCameraPermission(Granted : (()-> Unit)){
    askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.CAMERA){
        Granted.invoke()
    }.onDeclined {e->
        if(e.hasDenied()){
            activity?.alert("Access to gallery is denied", "Storage Permission") {
                positiveButton("Ask Again"){
                        dialog -> dialog.dismiss()
                }
                cancelButton { dialog -> dialog.dismiss() }
            }
        }
        if(e.hasForeverDenied())
            e.goToSettings()
    }
}