package com.example.discoorario

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val bundle: Bundle? = intent.extras
        val message = bundle?.getString("alarm_message")
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        //Toast.makeText(context, "ciao mondo!!", Toast.LENGTH_LONG).show()
    }
}
