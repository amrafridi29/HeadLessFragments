package com.example.ankolayoutexample.extensions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import androidx.fragment.app.Fragment
import org.jetbrains.anko.support.v4.act

fun Activity.enableDisableWifi(isEnable : Boolean){
   (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = isEnable
}

val Activity.isWifiEnable : Boolean get()  = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled


fun Fragment.enableDisableWifi(isEnable : Boolean){
    activity?.enableDisableWifi(isEnable)
}

val Fragment.isWifiEnable : Boolean get()  = requireActivity().isWifiEnable

val Activity.isBluetoothEnable : Boolean get() = BluetoothAdapter.getDefaultAdapter().isEnabled
val Fragment.isBluetoothEnable : Boolean get() = requireActivity().isBluetoothEnable

fun Activity.enableDisableBluetooth() = BluetoothAdapter.getDefaultAdapter().apply { if(isEnabled) disable() else enable() }

fun Fragment.enableDisableBluetooth() = requireActivity().enableDisableBluetooth()