package com.example.ankolayoutexample.activityResult


data class AccessUsagePermissionResult(val hasAccessUsage : Boolean, private val onResultData: OnResultData? = null){
    fun askAgain(){
        onResultData?.askAgain()
    }
}