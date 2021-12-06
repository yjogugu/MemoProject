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
        Log.e("여기","서비스 onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("여기","서비스 onStartCommand")
        if(intent == null){
            return START_STICKY
        }
//        return START_STICKY
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.e("여기","서비스 onBind")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}