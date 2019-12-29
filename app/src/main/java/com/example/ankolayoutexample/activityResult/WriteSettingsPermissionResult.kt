package com.example.ankolayoutexample.activityResult

data class WriteSettingsPermissionResult(val canWriteSettings : Boolean, private val onResultData: OnResultData? = null){
    fun askAgain(){
        onResultData?.askAgain()
    }
}