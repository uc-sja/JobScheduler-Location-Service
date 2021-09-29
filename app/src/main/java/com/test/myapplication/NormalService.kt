package com.test.myapplication

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.test.myapplication.App.Companion.CHANNEL_ID

class NormalService: Service() {
    private val TAG = "BackgroundService"

    override fun onCreate() {
        super.onCreate()

//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Example IntentService")
//            .setContentText("Running")
//            .build()


//        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var input = intent?.getStringExtra("inputExtra")


        for (i in 1 until 10){
            Log.d(TAG, "onHandleIntent: $input  $i")
            SystemClock.sleep(2000)
        }


        return START_NOT_STICKY



    }

    fun onHandleIntent(intent: Intent?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onHandleIntent onDestroy: ")
    }
}