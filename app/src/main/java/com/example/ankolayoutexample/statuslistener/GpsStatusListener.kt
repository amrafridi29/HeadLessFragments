package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun AppCompatActivity.setOnGpsStatusListener(lambda : ((isConnected : Boolean)-> Unit)?){
    GpsStatusListener(this).observe(this , Observer {
        when(it){
            is GpsStatus.Disabled -> lambda?.invoke(false)
            is GpsStatus.Enabled -> lambda?.invoke(true)
        }
    })
}

fun Fragment.setOnGpsStatusListener(lambda : ((isConnected : Boolean)-> Unit)?){
    activity?.apply {
        (this as AppCompatActivity).setOnGpsStatusListener(lambda)
    }
}


class GpsStatusListener (private val context : Context) : LiveData<GpsStatus>(){

    private val gpsSwitchStateReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) = checkGpsAndReact()
    }

    override fun onInactive() = unRegisterReceiver()

    override fun onActive() {
        registerReceiver()
        checkGpsAndReact()
    }

    private fun checkGpsAndReact() = if(isLocationEnabled()){
        postValue(GpsStatus.Enabled())
    }else{
        postValue(GpsStatus.Disabled())
    }

    private fun isLocationEnabled() = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    }else{
        try{
            getInt(context.contentResolver , LOCATION_MODE) != LOCATION_MODE_OFF
        }catch (e : Settings.SettingNotFoundException){
            false
        }
    }

    private fun registerReceiver() = context.registerReceiver(gpsSwitchStateReceiver,
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
    )

    private fun unRegisterReceiver() = context.unregisterReceiver(gpsSwitchStateReceiver)



}

sealed class GpsStatus{
   class Enabled : GpsStatus()
    class Disabled : GpsStatus()
}