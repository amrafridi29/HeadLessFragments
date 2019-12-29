package com.example.ankolayoutexample.network

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class NetworkHelper(private val ConnectivityCallback : ((isConnected : Boolean)-> Unit)?) : Fragment(){
    companion object{
        const val TAG = "NetworkHelper"
        const val CHECK_INTERNET = "network_connection"

        @Suppress("DEPRECATION")
        fun isInternetConnected(context: Context?) : Boolean{
            var result = false
            val connec = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

        @JvmStatic
        fun newInstance(ConnectivityCallback : ((isConnected : Boolean)-> Unit)?) = NetworkHelper(ConnectivityCallback)
    }

    private var mActivity : Activity?=null
    private val networkChangedReceiver : NetworkChangedReceiver? by lazy{
        NetworkChangedReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as Activity
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(CHECK_INTERNET)
        val mFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mActivity?.run {
            LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter)
            mActivity?.registerReceiver(networkChangedReceiver, mFilter)
        }

        //ConnectivityCallback?.invoke(isInternetConnected(mActivity))
    }

    override fun onPause() {
        super.onPause()
        mActivity?.run {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice)
            unregisterReceiver(networkChangedReceiver)
        }

    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    private val onNotice  = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.run {
                if(hasExtra(CHECK_INTERNET)){
                  ConnectivityCallback?.invoke(getBooleanExtra(CHECK_INTERNET , true))
                }
            }
        }
    }


}