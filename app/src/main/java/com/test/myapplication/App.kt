package com.test.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

class App: Application(){
    
    companion object{
        private const val TAG = "App"
        val CHANNEL_ID = "example_notification_channel"
        private var instance: App? = null
        fun getAppContext(): Context{
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

             val notificationChannel =     NotificationChannel(
                CHANNEL_ID,
                "Example Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

             val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(notificationChannel)
             Log.d(TAG, "createNotificationChannel: ")
        }
    }
}