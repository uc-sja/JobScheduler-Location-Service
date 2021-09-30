package com.test.myapplication

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.JobIntentService

private const val TAG = "JobIntentServiceExample"
class JobIntentServiceExample: JobIntentService() {

    companion object{
        fun enqueTask( context: Context, intent: Intent){
            enqueueWork(context, JobIntentServiceExample::class.java,500, intent )

        }
    }
    override fun onHandleWork(intent: Intent) {
        for(i in 1 until 11){
            Log.d(TAG, "onHandleWork: "+i)
            SystemClock.sleep(1000L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

}