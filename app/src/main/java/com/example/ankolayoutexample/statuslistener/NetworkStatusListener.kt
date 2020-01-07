package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun AppCompatActivity.setOnNetworkStatusListener(lambda : ((isConnected : Boolean)-> Unit)?){

    NetworkStatusListener(this).observe(this , Observer {
        when(it){
            is NetworkStatus.Disabled -> lambda?.invoke(false)
            is NetworkStatus.Enabled -> lambda?.invoke(true)
        }
    })
}

fun Fragment.setOnNetworkStatusListener(lambda : ((isConnected : Boolean)-> Unit)?){
    activity?.apply {
        (this as AppCompatActivity).setOnNetworkStatusListener(lambda)
    }
}


class NetworkStatusListener(private val context: Context) : LiveData<NetworkStatus>(){

    private val networkSwitchStateReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?)  = checkNetworkAndReact()
    }

    private fun checkNetworkAndReact() = if(isInternetConnected()){
        postValue(NetworkStatus.Enabled)
    }else{
        postValue(NetworkStatus.Disabled)
    }

    fun isInternetConnected() : Boolean{
        var result = false
        val connec = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            connec.run{
                activeNetwork?.run{
                    getNetworkCapabilities(this)?.run {
                        result = when{
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)-> true
                            else-> false
                        }
                    }
                }
            }
        }else{
            connec.run {
                activeNetworkInfo?.run{
                    result = when(type){
                        ConnectivityManager.TYPE_WIFI->true
                        ConnectivityManager.TYPE_MOBILE-> true
                        ConnectivityManager.TYPE_ETHERNET-> true
                        else->false
                    }
                }
            }
        }

        return result
    }

    override fun onInactive()  = unRegisterReceiver()

    override fun onActive() {
        super.onActive()
        registerReceiver()
        checkNetworkAndReact()
    }

    private fun registerReceiver() = context.registerReceiver(networkSwitchStateReceiver,
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    private fun unRegisterReceiver() = context.unregisterReceiver(networkSwitchStateReceiver)

}

sealed class NetworkStatus{
    object Enabled : NetworkStatus()
    object Disabled : NetworkStatus()
}