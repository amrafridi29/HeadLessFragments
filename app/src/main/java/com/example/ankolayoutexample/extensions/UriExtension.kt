package com.example.ankolayoutexample.extensions

import android.content.Context
import android.net.Uri
import java.io.File

fun Uri.getRealPath(context: Context?) : String?{
    context ?: return null
    return RealPathUtil.getRealPath(context ,this)
}

fun Uri.getFile(context: Context?) : File?{
    val path = getRealPath(context) ?: return null
    return File(path)
}