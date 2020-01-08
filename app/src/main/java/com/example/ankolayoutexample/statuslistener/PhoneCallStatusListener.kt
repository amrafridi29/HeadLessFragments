package com.example.ankolayoutexample.statuslistener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import java.util.*

fun AppCompatActivity.onPhoneCallStatusListener(lambda : ((CallStatus)-> Unit)){
    PhoneCallStatusListener(this).observe(this , androidx.lifecycle.Observer(lambda))
}

fun Fragment.onPhoneCallStatusListener(lambda : ((CallStatus)-> Unit)){
    activity?.let { (it as AppCompatActivity).onPhoneCallStatusListener(lambda) }
}


class PhoneCallStatusListener(private val context: Context) : LiveData<CallStatus>(){
    companion object {
        private const val NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL"
        private const val PHONE_STATE = "android.intent.action.PHONE_STATE"
        private const val PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER"
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private lateinit var callStartTime: Date
        private var isIncoming = false
        private var savedNumber //because the passed incoming is only valid in ringing
                : String? = null
    }
    private val onPhoneStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (intent.action == NEW_OUTGOING_CALL) {
                savedNumber =
                    intent.extras?.getString(PHONE_NUMBER)
            } else { val stateStr = intent.extras?.getString(TelephonyManager.EXTRA_STATE)
                val number = intent.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                var state = 0
                if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                    state = TelephonyManager.CALL_STATE_IDLE
                } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK
                } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                    state = TelephonyManager.CALL_STATE_RINGING
                }
                onCallStateChanged(intent, state, number)
            }
        }


        fun onCallStateChanged(
            intent: Intent,
            state: Int,
            number: String?
        ) {
            if (lastState == state) {
                return
            }
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    isIncoming = true
                    callStartTime = Date()
                    savedNumber = number
                    postValue(CallStatus.IncomingCallReceived(intent , number , callStartTime))
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    postValue(CallStatus.OutgoingCallStarted(intent, savedNumber, callStartTime))
                } else {
                    isIncoming = true
                    callStartTime = Date()
                    postValue(CallStatus.IncomingCallAnswered(intent, savedNumber, callStartTime))
                }
                TelephonyManager.CALL_STATE_IDLE -> if (lastState == TelephonyManager.CALL_STATE_RINGING) {

                    postValue(CallStatus.MissedCall(intent, savedNumber, callStartTime))
                } else if (isIncoming) {
                    postValue(CallStatus.IncomingCallEnded(intent, savedNumber,callStartTime, Date()))
                } else {
                    postValue(CallStatus.OutgoingCallEnded(intent , savedNumber, callStartTime, Date()))
                }
            }
            lastState = state
        }


    }

    override fun onInactive() {
        super.onInactive()
       // unRegisterReceiver()
    }

    private fun unRegisterReceiver()  = context.unregisterReceiver(onPhoneStateReceiver)

    override fun onActive() {
        super.onActive()
        registerReceiver()
    }

    private fun registerReceiver() = context.registerReceiver(onPhoneStateReceiver , IntentFilter(NEW_OUTGOING_CALL).apply {
            addAction(PHONE_STATE)
        })


}

sealed class CallStatus{
    data class IncomingCallReceived(val intent : Intent, val number : String?, val start : Date) : CallStatus()
    data class IncomingCallAnswered(val intent : Intent, val number : String?, val start : Date) : CallStatus()
    data class OutgoingCallStarted(val intent : Intent,val number : String? ,val start : Date) : CallStatus()
    data class IncomingCallEnded(val intent : Intent,val number : String? ,val start : Date , val end : Date) : CallStatus()
    data class OutgoingCallEnded(val intent : Intent,val number : String? ,val start : Date , val end : Date) : CallStatus()
    data class MissedCall(val intent : Intent , val number : String? ,val start : Date) : CallStatus()
}