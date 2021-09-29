package com.test.myapplication

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat

class IntentService: IntentService("BackgroundService") {
    private val TAG = "BackgroundService"

    override fun onCreate() {
        super.onCreate()

//        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
//            .setContentTitle("Example IntentService")
//            .setContentText("Running")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .build()

//        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {

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
}