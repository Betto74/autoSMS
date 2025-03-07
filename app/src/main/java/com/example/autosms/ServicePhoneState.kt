package com.example.autosms


import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.TelephonyManager
import com.example.autosms.BroadcastReceiver.CallReceiver

class ServicePhoneState : Service() {
    val br : CallReceiver = CallReceiver()
    val intentFilter: IntentFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)

    override fun onBind(p0: Intent?): IBinder? {
        //TODO("Not yet implemented")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(br, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(br)
    }

}