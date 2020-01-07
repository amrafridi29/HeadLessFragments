package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*

fun AppCompatActivity.setOnTimeTickStatusListener(lambda : ((timeTick : TimeTick)->Unit)){
    TimeTickStatusListener(this).observe(this , Observer(lambda))
}

fun Fragment.setOnTimeTickStatusListener(lambda: ((timeTick: TimeTick) -> Unit)){
    activity?.apply { (this as AppCompatActivity).setOnTimeTickStatusListener(lambda) }
}

class TimeTickStatusListener(private val context: Context) : LiveData<TimeTick>(){
    private val timeTickReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val calendar  = Calendar.getInstance()
            postValue(TimeTick(calendar , calendar.time))
        }
    }

    private fun registerReceiver() = context.registerReceiver(timeTickReceiver,
        IntentFilter(Intent.ACTION_TIME_TICK).apply {
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
        })

    private fun unRegisterReceiver() = context.unregisterReceiver(timeTickReceiver)

    override fun onInactive() = unRegisterReceiver()

    override fun onActive() {
        super.onActive()
        registerReceiver()
    }
}


data class TimeTick(val calendar: Calendar , val time : Date)