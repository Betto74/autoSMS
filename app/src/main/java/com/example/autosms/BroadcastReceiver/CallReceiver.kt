package com.example.autosms.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.telephony.SmsManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Verifica si la acción es un cambio en el estado del teléfono
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            Log.d("CallReceiver", "Hola")
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            // Verifica si el estado es "RINGING" (llamada entrante)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Log.d("CallReceiver", "Llamada entrante de: $incomingNumber")

                // Recuperar número de teléfono y mensaje desde SharedPreferences
                val sharedPreferences = context.getSharedPreferences("AutoSMS", Context.MODE_PRIVATE)
                val targetNumber = sharedPreferences.getString("phoneNumber", null)
                val message = sharedPreferences.getString("message", null)

                // Verifica si el número coincide con el número guardado
                if (incomingNumber == targetNumber && !targetNumber.isNullOrEmpty() && !message.isNullOrEmpty()) {
                    // Envía el SMS automático
                    sendSms(targetNumber, message)
                    Log.d("CallReceiver", "SMS enviado a: $targetNumber")
                }
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            Log.e("CallReceiver", "Error al enviar SMS: ${e.message}")
        }
    }
}