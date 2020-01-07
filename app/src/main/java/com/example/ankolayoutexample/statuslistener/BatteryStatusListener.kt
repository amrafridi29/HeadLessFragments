package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt


fun AppCompatActivity.setOnBatteryStatusListener(lambda : ((batteryStatus : BatteryStatus)-> Unit)){
    BatteryStatusListener(this).observe(this , Observer(lambda))
}

fun Fragment.setOnBatteryStatusListener(lambda : ((batteryStatus : BatteryStatus)-> Unit)){
    activity?.apply {
        (this as AppCompatActivity).setOnBatteryStatusListener(lambda)
    }
}

class BatteryStatusListener(private val context: Context) : LiveData<BatteryStatus>() {

    private val batteryStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.also { batteryIntent ->
                when (batteryIntent.action) {
                    Intent.ACTION_BATTERY_LOW-> postValue(
                        BatteryStatus.Low(
                            batteryIntent,
                            context
                        )
                    )
                    Intent.ACTION_BATTERY_OKAY-> postValue(
                        BatteryStatus.Okay(
                            batteryIntent,
                            context
                        )
                    )
                    Intent.ACTION_BATTERY_CHANGED,
                    Intent.ACTION_POWER_CONNECTED,
                    Intent.ACTION_POWER_DISCONNECTED-> when (batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                        BatteryManager.BATTERY_STATUS_CHARGING -> postValue(
                            BatteryStatus.Charging(
                                batteryIntent,
                                context
                            )
                        )
                        BatteryManager.BATTERY_STATUS_FULL -> postValue(
                            BatteryStatus.Full(
                                batteryIntent,
                                context
                            )
                        )
                        else -> postValue(
                            BatteryStatus.Discharging(
                                batteryIntent,
                                context
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onInactive() = unRegisterReceiver()

    override fun onActive() {
        super.onActive()
        registerReceiver()
    }

    private fun registerReceiver() = context.registerReceiver(
        batteryStatusReceiver,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED).apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
    )

    private fun unRegisterReceiver() = context.unregisterReceiver(batteryStatusReceiver)
}


sealed class BatteryStatus(private val intent: Intent , private val context: Context?) : Battery(intent, context) {
    data class Charging(val intent: Intent, val context: Context?) : BatteryStatus(intent, context)
    data class Full(val intent: Intent, val context: Context?) : BatteryStatus(intent, context)
    data class Discharging(val intent: Intent, val context: Context?) : BatteryStatus(intent, context)
    data class Low(val intent : Intent, val context: Context?) : BatteryStatus(intent, context)
    data class Okay(val intent : Intent, val context: Context?) : BatteryStatus(intent, context)

}

open class Battery(private val intent: Intent, private val context: Context?) {
    val batteryUtils : BatteryUtils by lazy{
        BatteryUtils(context)
    }
    val level get() = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale get() = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    val temperature  get() = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
    val voltage get() = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
    val plugType get() = when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            BatteryManager.BATTERY_PLUGGED_USB -> PlugType.USB
            BatteryManager.BATTERY_PLUGGED_AC -> PlugType.AC
            BatteryManager.BATTERY_PLUGGED_WIRELESS-> PlugType.WIRELESS
            else -> PlugType.UNKNOWN
        }


    val batteryPercent : String
    get() = run {
        val batteryPct = level / scale.toFloat()
        val p = batteryPct * 100
        return p.roundToInt().toString()
    }

    val technology : String get() = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

    val batteryHealth get() = when(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)){
        BatteryManager.BATTERY_HEALTH_COLD -> BatteryHealth.COLD
        BatteryManager.BATTERY_HEALTH_DEAD -> BatteryHealth.DEAD
        BatteryManager.BATTERY_HEALTH_GOOD -> BatteryHealth.GOOD
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE-> BatteryHealth.OVER_VOLTAGE
        BatteryManager.BATTERY_HEALTH_OVERHEAT-> BatteryHealth.OVERHEAT
        BatteryManager.BATTERY_HEALTH_UNKNOWN-> BatteryHealth.UNKNOWN
        else-> BatteryHealth.UNSPECIFIED_FAILURE
    }
}

enum class PlugType {
    USB,
    AC,
    WIRELESS,
    UNKNOWN
}

enum class BatteryHealth{
    COLD,
    DEAD,
    GOOD,
    OVER_VOLTAGE,
    OVERHEAT,
    UNKNOWN,
    UNSPECIFIED_FAILURE
}

class BatteryUtils(private val context: Context?){
    /**
     * Get the battery capacity at the moment (in %, from 0-100)
     *
     * @return Battery capacity (in %, from 0-100)
     */

    val capacity : Int get() = run{
        context ?: return 0
        var value = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            }
        }
        return if(value!=0 && value != Int.MIN_VALUE) value  else 0
    }

    /**
     * Get the battery full capacity (charge counter) in mAh.
     * Since Power (W) = (Current (A) * Voltage (V)) <=> Power (Wh) = (Current (Ah) * Voltage (Vh)).
     * Therefore, Current (mA) = Power (mW) / Voltage (mV)
     *
     * @return Battery full capacity (in mAh)
     */

    val chargeCounter : Int get() = run{
        context ?: return 0
        var value = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
            }
        }
        return value
    }

    val currentAverage : Int get() = run{
        context ?: return 0
        var value = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
            }
        }

        return if (value != 0 && value != Integer.MIN_VALUE) value else 0
    }

    /**
     * Get the Battery current at the moment (in mA)
     *
     * @return battery current now (in mA)
     */
    val currentNow : Int get() = run{
        context ?: return 0
        var value = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
            }
        }

        return if (value != 0 && value != Integer.MIN_VALUE) value else 0
    }

    /**
     * Get the Battery current at the moment (in mA)
     *
     * @param context Application context
     * @return battery current now (in mA)
     */

    val currentNowInAmperes : Double get()= run{
        context ?: return 0.0
        var value = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
            }
        }
        value = if (value != 0 && value != Integer.MIN_VALUE) value else 0
        return (value / 1000000).toDouble()
    }

    /**
     * Get the battery energy counter capacity (in mWh)
     *
     * @return battery energy counter (in mWh)
     */

    val energyCounter : Long get() = run{
        context ?: return 0
        var value : Long = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                value = it.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)
            }
        }
        return value
    }

    /**
     * Calculate Average Power
     * Average Power = (Average Voltage * Average Current) / 1e9
     *
     * @param context Context of application
     * @return Average power in integer
     */

    fun getAveragePower(voltage : Int) : Int{
        context ?: return 0
        var current = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                current = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
            }
        }

        return voltage * current / 1000000000
    }

    /**
     * Calculate Battery Capacity Consumed
     * Battery Capacity Consumed = (Average Current * Workload Duration) / 1e3
     *
     * @param workload Workload duration (in hours)
     * @param context  Context of application
     * @return Average power in integer
     */
    fun getBatteryCapacityConsumed(workload: Double): Double {
        context ?: return 0.0
        var current = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).also {
                current = it.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
            }
        }

        return current * workload / 1000
    }

    val designCapacity : Int get() = run{
        context ?: return 0
        val mPowerProfile: Any
        var batteryCapacity = 0.0
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java)
                .newInstance(context)

            batteryCapacity = Class
                .forName(POWER_PROFILE_CLASS)
                .getMethod("getBatteryCapacity")
                .invoke(mPowerProfile) as Double

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return batteryCapacity.toInt()
    }

    /**
     * Calculates the battery's remaining energy capacity
     *
     * @param context the context of application
     * @return the battery remaining capacity, in mAh, as Integer
     */
    fun getBatteryRemainingCapacity(mVoltage : Int): Int {
        val voltage = if (mVoltage == 0) mVoltage else mVoltage / 1000;
        val remainingCapacity: Double
        var capacity = this.capacity
        if (capacity <= -1) {
            capacity = 0
        }

        var chargeCounter = this.chargeCounter
        if (chargeCounter <= -1) {
            chargeCounter = abs(designCapacity)  // in mAh
        }

        if (capacity > 0 && chargeCounter > 0) {
            remainingCapacity = (chargeCounter * capacity / 100).toDouble()
        } else {
            val voltageNow = max(1, voltage)
            var energyCounter = this.energyCounter
            if (energyCounter <= -1) {
                energyCounter = 0
            }
            return (energyCounter / voltageNow).toInt()
        }

        return remainingCapacity.toInt()
    }

    /**
     * Calculate Remaining Battery Time (in hours) - An Estimate
     * Remaining Battery Life [h] ->
     * Battery Remaining Capacity [mAh/mWh] / Battery Present Drain Rate [mA/mW]
     *
     * @param context Context of application
     * @return Remaining Time (in hours)
     */
    @Deprecated("")
    fun getRemainingBatteryTimeEstimate(voltage: Int): Double {
        val remainingCapacity = getBatteryRemainingCapacity(voltage)
        //in mA
        val currentNow = (if (this.currentNow != -1)
            abs(this.currentNow)
        else
            0).toDouble()

        return if (remainingCapacity > 0 && currentNow > 0) {
            remainingCapacity / currentNow
        } else -1.0

    }


}