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


//- [ ] Irrespective of android version if job scheduler if task is killed before the
// code within onStartJob is completed IN JOBSCHEDULER and onHandleIntent is completed
// in JOBINTENTSERVICE, then the task will be restarted within 1 sec. There will also be a
// message on log as:  “Scheduling restart of crashed service com.test.myapplication/.JobSchedulerService
// in 1000ms”

class JobSchedulerService: JobService() {
    private  var locationRequest1: LocationRequest? = null
    private   var locationCallback1: LocationCallback? = null
    private  var fusedLocationClient1: FusedLocationProviderClient? = null
    var isRunning = false

    companion object{
        var requestingLocation = false
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")

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




        fusedLocationClient1?.requestLocationUpdates(locationRequest1, locationCallback1, Looper.getMainLooper())
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob: ")
        isRunning = true

        if(locationRequest1 != null){
            startLocationUpdates()
            return true
        }
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


        startLocationUpdates()
        return true
    }


    //clear resources here to stop redundancy
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: ")
        isRunning = false
        stopLocationUpdates()
        return  true
    }

    private fun stopLocationUpdates() {
        if(locationCallback1 != null){
            fusedLocationClient1?.removeLocationUpdates(locationCallback1)
        }
    }
}