package com.example.ankolayoutexample.activityResult

data class OverlyPermissionResult(val canDrawOverlay : Boolean, private val onResultData: OnResultData? = null){
    fun askAgain(){
        onResultData?.askAgain()
    }
}