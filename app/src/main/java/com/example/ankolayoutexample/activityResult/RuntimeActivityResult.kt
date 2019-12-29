package com.example.ankolayoutexample.activityResult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.io.File

private val TAG = "ATIVITY_RESULT_FRAGMENT"
class RuntimeActivityResult (
    private val activity : AppCompatActivity,
    private val intentType: IntentType?,
    private val OnResultCallback : ((onResultData : OnResultData)-> Unit)? ){

    fun ask(){
        val oldFragment = activity
            .supportFragmentManager
            .findFragmentByTag(TAG) as ActivityResultFragment?

        oldFragment?.setListener(OnResultCallback , this)?.askAgain() ?: run{
            val fragment = ActivityResultFragment(intentType)//.invoke(intent)
                .setListener(OnResultCallback , this)
            activity.runOnUiThread {
                activity.supportFragmentManager
                    .beginTransaction()
                    .add(fragment , TAG)
                    .commitNowAllowingStateLoss()
            }
        }
    }

}