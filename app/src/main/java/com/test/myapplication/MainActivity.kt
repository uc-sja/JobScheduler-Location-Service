package com.test.myapplication

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat

//- [ ] One major important difference between intent service and normal service
// (irrespective of oreo update) is that in intent service as soon as we kill the
// activity the intent service stops whereas in normal service the service still
// continues to run in background although the difference is that pre oreo it will
// run indefinitely whereas post oreo it is highly in the risk of being killed if
// systems needs memory.



//- [ ] In Job Scheduler, scheduling a job with same job id multiple time simultaneously
// will cancel the previous job(if unfinished) by calling onStopJob method .


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val LOCATION_REQUEST_PERMISSION_CODE: Int = 322

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent(this, IntentService::class.java)
//        intent.putExtra("inputExtra", "job intent")
//
//        startService(intent)

        var start = findViewById<TextView>(R.id.start)
        var stop = findViewById<TextView>(R.id.stop)



        start.setOnClickListener(View.OnClickListener {
            if(LocationPermissionsGranted()){
                scheduleJob()

            } else {
                requestLocationPermissions()
            }
        })




    }

    private fun LocationPermissionsGranted(): Boolean {
        val requestStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION))
        } else {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return requestStatus
    }

    private fun requestLocationPermissions() {
        Log.d(TAG, "requestLocationPermissions: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                LOCATION_REQUEST_PERMISSION_CODE
            )
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_PERMISSION_CODE)

        }
    }



    fun scheduleJob(){
        var componentName = ComponentName(this, JobSchedulerService::class.java)
        var jobInfo: JobInfo = JobInfo.Builder(123, componentName)
            .setPeriodic(15*60*1000L)
            .setPersisted(true)
            .build()
        var jobScheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        var result = jobScheduler.schedule(jobInfo)
        if(result == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "onCreate: job scheduled")
        } else{
            Log.d(TAG, "onCreate: schedule failded")
        }

    }
}