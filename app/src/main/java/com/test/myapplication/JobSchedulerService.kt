package com.test.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*

private const val TAG = "JobSchedulerService"

class JobSchedulerService: JobService() {
    private lateinit var locationRequest1: LocationRequest
    private lateinit  var locationCallback1: LocationCallback
    private lateinit var fusedLocationClient1: FusedLocationProviderClient
    var isRunning = false

    companion object{
        var requestingLocation = false
    }

    override fun onCreate() {
        super.onCreate()

    }
    private fun createLocationRequest(){

        //Simply Fetching the location at the  periodic 30s interval
        locationRequest1 = LocationRequest.create()?.apply {
            interval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!



    }



    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "startLocationUpdates: no permit")
            return
        }




        fusedLocationClient1.requestLocationUpdates(locationRequest1, locationCallback1, Looper.getMainLooper())
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        isRunning = true

        locationRequest1 = LocationRequest.create()?.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!

        fusedLocationClient1 = LocationServices.getFusedLocationProviderClient(this)

        locationCallback1 = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                requestingLocation  = true
                var location = locationResult.lastLocation
                Log.d(TAG, "onLocationResult: "+location)
            }
        }

//        doBackgroundTask(params)

        startLocationUpdates()
        return true
    }

    @SuppressLint("MissingPermission")
    private fun doBackgroundTask(params: JobParameters?) {
        Log.d(TAG, "doBackgroundTask: ")
        Thread {
                for (i in 1 until 101){

                    Log.d(TAG, "doBackgroundTask : $i")
                    if(isRunning){
                        SystemClock.sleep(5000)
                        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location : Location? ->
                                Log.d(TAG, "doBackgroundTask: ")
                                // Got last known location. In some rare situations this can be null.
                            }

                    }
                }
                jobFinished(params, false)
           }.start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: ")
        isRunning = false
        return  true
    }
}