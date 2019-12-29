package com.example.ankolayoutexample.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File


//fun File.openFile(context: Context) {
//    val uri = Uri.fromFile(this)
//    val intent = Intent(Intent.ACTION_VIEW)
//    if (path.endsWith(".doc") || path.endsWith(".docx")) {
//        // Word document
//        intent.setDataAndType(uri, "application/msword")
//    } else if (path.endsWith(".pdf")) {
//        // PDF file
//        intent.setDataAndType(uri, "application/pdf")
//    } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
//        // Powerpoint file
//        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
//    } else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
//        // Excel file
//        intent.setDataAndType(uri, "application/vnd.ms-excel")
//    } else if (path.endsWith(".zip") || path.endsWith(".rar")) {
//        // WAV audio file
//        intent.setDataAndType(uri, "application/zip")
//    } else if (path.endsWith(".rtf")) {
//        // RTF file
//        intent.setDataAndType(uri, "application/rtf")
//    } else if (path.endsWith(".wav") || path.endsWith(".mp3")) {
//        // WAV audio file
//        intent.setDataAndType(uri, "audio/x-wav")
//    } else if (path.endsWith(".gif")) {
//        // GIF file
//        intent.setDataAndType(uri, "image/gif")
//    } else if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")
//    ) {
//        // JPG file
//        intent.setDataAndType(uri, "image/jpeg")
//    } else if (path.endsWith(".txt")) {
//        // Text file
//        intent.setDataAndType(uri, "text/plain")
//    } else if (path.endsWith(".3gp") || path.endsWith(".mpg") || path.endsWith(
//            ".mpeg"
//        ) || path.endsWith(".mpe") || path.endsWith(".mp4") || path.endsWith(
//            ".avi"
//        )
//    ) {
//        // Video files
//        intent.setDataAndType(uri, "video/*")
//    } else {
//        intent.setDataAndType(uri, "*/*")
//    }
//
//    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    intent.also { fileIntent ->
//        fileIntent.resolveActivity(context.packageManager)?.also {
//            context.startActivity(fileIntent)
//        }
//
//    }
//}

fun File.openFile(context: Context){
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension).also {mimeType->
        Uri.fromFile(this).also {uri->
            Intent(Intent.ACTION_VIEW).also {fileIntent->
                fileIntent.setDataAndType(uri , mimeType ?: "*/*")
                fileIntent.resolveActivity(context.packageManager)?.also {
                    context.startActivity(fileIntent)
                }
            }
        }
    }

}