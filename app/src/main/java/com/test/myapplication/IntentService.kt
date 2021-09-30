package com.test.myapplication

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat

class IntentService: IntentService("BackgroundService") {
    private val TAG = "BackgroundService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        

//        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
//            .setContentTitle("Example IntentService")
//            .setContentText("Running")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .build()

//        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {

        Log.d(TAG, "onHandleIntent: ")

        var input = intent?.getStringExtra("inputExtra")


        for (i in 1 until 6){
            Log.d(TAG, "onHandleIntent: $input  $i")
            SystemClock.sleep(2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: onHandleIntent")
    
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")
        return super.onBind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.d(TAG, "onStartCommand: ")
    }
}
