package com.example.ankolayoutexample.activityResult

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


internal class ActivityResultFragment(
    private val intentType: IntentType?
) : Fragment() {

    private val requestCode = 22
    private var cameraFilePath: String = ""

    private var OnResultCallback: ((onResultData: OnResultData) -> Unit)? = null
    private var runtimeActivityResult: RuntimeActivityResult? = null


    fun setListener(
        OnResultCallback: ((onResultData: OnResultData) -> Unit)?,
        runtimeActivityResult: RuntimeActivityResult?
    ): ActivityResultFragment {
        this.OnResultCallback = OnResultCallback
        this.runtimeActivityResult = runtimeActivityResult
        return this
    }

    override fun onResume() {
        super.onResume()
        ask()
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(cameraFilePath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }

    private fun ask() {
        if (intentType != null) {
            when (intentType) {
                is IntentType.CameraImage -> {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                null
                            }
                            photoFile?.also {
                                val photoUri: Uri =
                                    FileProvider.getUriForFile(
                                        requireActivity(),
                                        intentType.authority,
                                        it
                                    )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                startActivityForResult(takePictureIntent, requestCode)
                            }
                        }
                    }
                }
                is IntentType.CameraVideo -> {
                    Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                        takeVideoIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startActivityForResult(takeVideoIntent, requestCode)
                        }
                    }
                }
                is IntentType.Other -> {
                    intentType.intent.resolveActivity(requireActivity().packageManager)?.also {
                        startActivityForResult(intentType.intent, requestCode)
                    }
                }
                is IntentType.Gallery -> {
                    val intent = when (intentType.fileType) {
                        FileType.AUDIOS -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.AUDIO_WAVE -> Intent(Intent.ACTION_PICK).apply {
                            type = "audio/x-wav"
                        }
                        FileType.VIDEOS -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.IMAGES -> Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        FileType.MEDIAS -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*" , "video/*"))
                            }
                        }
                        FileType.PDF -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/pdf"
                        }
                        FileType.MSWORD -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/msword"
                        }
                        FileType.POWER_POINT -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/vnd.ms-powerpoint"
                        }
                        FileType.EXCEL -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/vnd.ms-excel"
                        }
                        FileType.TXT -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "text/plain"
                        }

                        FileType.ZIP -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/zip"
                        }

                        FileType.RAR -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/rar"
                        }

                        FileType.ZIP_RAR -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/zip" , "application/rar"))
                            }
                        }

                        FileType.RTF -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/rtf"
                        }

                        FileType.ANY_FILE -> Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "*/*"
                        }

                        FileType.APPLICATIONS->Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "application/*"
                        }

                    }

                    intent.also { pickFileIntent ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            pickFileIntent.putExtra(
                                Intent.EXTRA_ALLOW_MULTIPLE,
                                intentType.isMultiSelect
                            )
                        }

                        pickFileIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startActivityForResult(pickFileIntent, requestCode)
                        }
                    }

                }
                is IntentType.WriteSettingsPermission->{
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).also { settingIntent ->
                        settingIntent.data = Uri.parse("package:${requireActivity().packageName}")
                        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        settingIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startActivityForResult(settingIntent, requestCode)
                        }
                    }
                }
                is IntentType.OverlayPermission->{
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireActivity().packageName}")).also{overlayIntent->
                        overlayIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startActivityForResult(overlayIntent, requestCode)
                        }
                    }
                }

                is IntentType.AccessUsageSettingsPermission->
                {
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).also{usageIntent->
                        usageIntent.resolveActivity(requireActivity().packageManager)?.also {
                            startActivityForResult(usageIntent, requestCode)
                        }
                    }
                }

                is IntentType.AccessibilitySettingsPermission->Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).also { accIntent->
                    accIntent.resolveActivity(requireActivity().packageManager)?.also {
                        startActivityForResult(accIntent , requestCode)
                    }
                }
            }
        } else {
            fragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commitAllowingStateLoss()
        }
    }

    fun askAgain() {
        ask()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            when (intentType) {
                is IntentType.CameraImage -> {
                    data?.data = Uri.parse(cameraFilePath)
                    galleryAddPic()
                }
            }

            OnResultCallback?.invoke(
                OnResultData(
                    resultCode,
                    data,
                    runtimeActivityResult,
                    activity)
            )
        fragmentManager?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
        super.onActivityResult(requestCode, resultCode, data)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDirectory = when (intentType) {
            is IntentType.CameraImage -> intentType.storageFileDirectory
            else -> null
        }
        storageDirectory?.also { if (!it.exists()) it.mkdirs() }
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDirectory ?: storageDir
        ).apply {
            cameraFilePath = absolutePath
        }
    }


}