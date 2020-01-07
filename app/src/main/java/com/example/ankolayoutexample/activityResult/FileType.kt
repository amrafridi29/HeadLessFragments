package com.example.ankolayoutexample.activityResult

import android.content.Intent
import java.io.File


enum class FileType {
    AUDIOS,
    AUDIO_WAVE,
    VIDEOS,
    IMAGES,
    PDF,
    MSWORD,
    POWER_POINT,
    EXCEL,
    TXT,
    ZIP,
    RAR,
    ZIP_RAR,
    RTF,
    MEDIAS,
    ANY_FILE,
    APPLICATIONS
}

sealed class IntentType{
    data class CameraImage(val authority : String , val storageFileDirectory : File? = null) : IntentType()
    object CameraVideo : IntentType()
    data class Other(val intent : Intent) : IntentType()
    data class Gallery(val fileType: FileType , val isMultiSelect : Boolean = false) : IntentType()
    object OverlayPermission : IntentType()
    object WriteSettingsPermission : IntentType()
    object AccessUsageSettingsPermission : IntentType()
}