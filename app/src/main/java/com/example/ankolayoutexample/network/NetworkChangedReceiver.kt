package com.example.ankolayoutexample.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NetworkChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val mIntent = Intent(NetworkHelper.CHECK_INTERNET)
        mIntent.putExtra(
            NetworkHelper.CHECK_INTERNET,
            NetworkHelper.isInternetConnected(context)
        )
        context?.run {
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
        }
    }

}