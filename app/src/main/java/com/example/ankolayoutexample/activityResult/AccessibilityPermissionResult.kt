package com.example.ankolayoutexample.activityResult


data class AccessibilityPermissionResult(val hasAccessibility : Boolean, private val onResultData: OnResultData? = null){
    fun askAgain(){
        onResultData?.askAgain()
    }
}