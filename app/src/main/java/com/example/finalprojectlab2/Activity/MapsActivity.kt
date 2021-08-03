package com.example.finalprojectlab2.Activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalprojectlab2.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    var lag= ""
    var lat = ""
    var id = ""
    val TAG = "aaa3"


    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        getLastLocation()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        id = intent.getStringExtra("item_id")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
/*
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        lag = sharedPref.getString("lag","")!!
        lat = sharedPref.getString("lat","")!!*/


        //     geitemData(id)

    }

    override fun onMapReady(googleMap: GoogleMap) {



        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("item").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                    var latItem = data!!["item_Local_lag"] as String
                    var lagItem = data["item_Local_lat"] as String
                    Log.d("aaa1" , latItem)
                    Log.d("aaa1" , lagItem)

                    productMap(latItem.toDouble(),lagItem.toDouble())
                }
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }


            }


        mMap = googleMap


        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.isMyLocationEnabled = true

     //   productMap(item_Local_lag.toDouble(),item_Local_lat.toDouble())
        mMap.setOnMapClickListener { point ->

                    mMap.addMarker(MarkerOptions().position(LatLng(point.latitude, point.longitude)).title("Procdunt Marking"))

                    Toast.makeText(
                        this,
                        point.latitude.toString() + ", " + point.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                }


    }

    fun geitemData(id : String){

         }

    fun productMap(item_Local_lag : Double ,item_Local_lat : Double ){
        // Add a marker in Sydney and move the camera
        val UserMarking = LatLng(lat.toDouble(), lag.toDouble())
        mMap.addMarker(MarkerOptions().position(UserMarking).title("User Marking"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UserMarking))


        // Add a marker in Sydney and move the camera
        val ProcduntMarking = LatLng(item_Local_lat, item_Local_lag)
        mMap.addMarker(MarkerOptions().position(ProcduntMarking).title("Procdunt Marking"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ProcduntMarking))


        mMap.addPolyline(
            PolylineOptions()
                .add(LatLng(lat.toDouble(), lag.toDouble()))
                .add(LatLng(item_Local_lag, item_Local_lag))
                .color(Color.BLUE)
                .visible(true)
        )
    }

    private fun getLastLocation(){
        val locationClient  = LocationServices.getFusedLocationProviderClient(this)
        locationClient.lastLocation
            .addOnSuccessListener {location ->
                if(location!=null){
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val provider = location.provider
                    Log.e("aaa2","aaa"+latitude.toString())

                    lag = longitude.toString()
                    lat = latitude.toString()

                    val latlang = LatLng(latitude,longitude)

                    Log.e(TAG,latitude.toString())
                    Log.e(TAG,longitude.toString())
                    Log.e(TAG,provider)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG,exception.message)
            }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()

            //Can add more as per requirement
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }



}
