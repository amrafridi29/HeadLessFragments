package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer




fun Fragment.onPackageStatusListner(lambda : ((packageStatus : PackageStatus)-> Unit)){
    activity?.apply {
        (this as AppCompatActivity).onPackageStatusListner(lambda)
    }
}

fun AppCompatActivity.onPackageStatusListner(lambda : ((packageStatus : PackageStatus)-> Unit)){
    PackageStatusListener(this)
        .observe(this , Observer (lambda))
}


class PackageStatusListener(private val context: Context) : LiveData<PackageStatus>(){

    private val packageStatueReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                data?.schemeSpecificPart?.let {packagename->
                    when(action){
                        Intent.ACTION_PACKAGE_REMOVED->postValue(
                            PackageStatus.Removed(
                                packagename
                            )
                        )
                        Intent.ACTION_PACKAGE_CHANGED->postValue(
                            PackageStatus.Changed(
                                packagename
                            )
                        )
                        Intent.ACTION_PACKAGE_ADDED->postValue(
                            PackageStatus.Added(
                                this,
                                getAppInfo(packagename)
                            )
                        )
                    }
                }

            }
        }
    }


    private fun getAppInfo(packagename : String?) : AppInfo {
        val packageManager = context.packageManager
        val info = packageManager.getApplicationInfo(packagename, PackageManager.GET_META_DATA) ?: null
        val label = packageManager.getApplicationLabel(info) as String?
        val className = info?.className
        val icon = info?.loadIcon(packageManager)
        return AppInfo(
            label,
            packagename,
            className,
            icon
        )
    }
    override fun onInactive()= unRegisterReceiver()

    override fun onActive() {
        super.onActive()
        registerReceiver()
    }

    private fun registerReceiver() = context.registerReceiver(packageStatueReceiver,
        IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
            priority = 0
        }
    )

    private fun unRegisterReceiver() = context.unregisterReceiver(packageStatueReceiver)

}

sealed class PackageStatus{
    class Added(val intent : Intent, val appInfo: AppInfo) : PackageStatus()
    class Changed(val mPackageName : String) :  PackageStatus()
    class Removed(val mPackageName : String) :  PackageStatus()
}

data class AppInfo(var label: String? = null,
                   var mPackage: String? = null,
                   var className: String? = null,
                   var icon: Drawable? = null)