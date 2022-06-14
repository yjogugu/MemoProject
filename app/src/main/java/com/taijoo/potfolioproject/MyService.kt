package com.taijoo.potfolioproject

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.taijoo.potfolioproject.data.repository.Http.RetrofitSender
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null){
            return START_STICKY
        }
//        return START_STICKY
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}