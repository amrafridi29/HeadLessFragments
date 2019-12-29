package com.example.ankolayoutexample.activityResult

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import com.example.ankolayoutexample.extensions.getFile
import java.io.File

data class OnResultData(val resultCode : Int, val data : Intent?, private val runtimeActivityResult: RuntimeActivityResult?, private val activity : Activity?){
    fun askAgain(){
        runtimeActivityResult?.ask()
    }

    fun getSelectedFile() : File?{
       return data?.data?.getFile(activity)
    }

     fun getSelectedFiles(): MutableList<File>? {
        activity ?: return null
        val clipData = data?.clipData ?: return null
        val files = mutableListOf<File>()
        for(index in 0 until clipData.itemCount){
            clipData.getItemAt(index).apply {
                uri.getFile(activity)?.also {
                    files.add(it)
                }
            }

        }
        return files
    }
}