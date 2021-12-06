package com.taijoo.potfolioproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when {
            intent.action.equals(Intent.ACTION_SCREEN_ON) -> {
                Toast.makeText(context, "SCREEN_ON", Toast.LENGTH_SHORT).show();
            }
            intent.action.equals(Intent.ACTION_SCREEN_OFF) -> {
                Toast.makeText(context, "SCREEN_OFF", Toast.LENGTH_SHORT).show();
            }
            intent.action.equals(Intent.ACTION_BOOT_COMPLETED) -> {
                Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
                val i = Intent(context, MyService::class.java)
                context.startService(i)
            }
        }

    }
}