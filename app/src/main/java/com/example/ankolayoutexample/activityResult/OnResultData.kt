package com.example.ankolayoutexample.activityResult

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import java.io.File

data class OnResultData(val resultCode : Int, val data : Intent?, private val runtimeActivityResult: RuntimeActivityResult?, private val activity : Activity?){
    fun askAgain(){
        runtimeActivityResult?.ask()
    }

    fun getSelectedFile() : File?{
        activity ?: return null
        var file : File? =null
        RealPathUtil.getRealPath(activity , data?.data)?.also {
            file = File(it)
        }
        return file
    }

     fun getSelectedFiles(): MutableList<File>? {
        activity ?: return null
        val clipData = data?.clipData
        clipData ?: return null
        val files = mutableListOf<File>()
        for(index in 0 until clipData.itemCount){
            clipData.getItemAt(index).apply {
                RealPathUtil.getRealPath(activity , uri)?.also {
                    files.add(File(it))
                }
            }

        }
        return files
    }
}