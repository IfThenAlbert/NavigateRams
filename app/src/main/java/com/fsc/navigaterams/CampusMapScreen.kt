package com.fsc.navigaterams

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.fsc.navigaterams.databinding.ActivityCampusMapScreenBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

class CampusMapScreen : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCampusMapScreenBinding
    lateinit var locationClient:FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityCampusMapScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mark_buildings()

        mMap.setOnMarkerClickListener(this)


        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val main = LatLng(40.7508079, -73.4305578)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(main, 18f))

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        if( (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            locationClient.lastLocation.addOnSuccessListener{
                val user:LatLng = LatLng(it.latitude,it.longitude)


                mMap.clear()
                mark_buildings()

                var userMarker:LatLng =  LatLng(it.latitude,it.longitude)
                mMap.addMarker(MarkerOptions().title("--YOU--").position(userMarker).icon(BitmapDescriptorFactory.defaultMarker(250f)))

                val  targetLocation:Location = Location("FSC_BUILDING")
                targetLocation.latitude = p0.position.latitude
                targetLocation.longitude = p0.position.longitude

                val userCurr:Location = Location("APP_USER")
                userCurr.latitude = it.latitude
                userCurr.longitude = it.longitude

                val distanceInFeet:Double = targetLocation.distanceTo(userCurr) * 3.28084;
                Toast.makeText(applicationContext,"FEET: $distanceInFeet",Toast.LENGTH_LONG).show()

            }
        }
        return false
    }

    fun mark_buildings() {
        val url = getString(R.string.fsc_infos) // public URL
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        // Request to get LAT AND LONG
        val requestObj = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val dataArray = response.getJSONArray("fsc_info")

                for(i in 0 until dataArray.length()) {
                    val building = dataArray.getJSONObject(i)
                    val givenLat = building.getString("LAT").toDouble()
                    val givenLong = building.getString("LONG").toDouble()
                    val fsc:LatLng = LatLng(givenLat,givenLong)
                    mMap.addMarker(MarkerOptions().title(building.getString("BUILDING")).position(fsc))
                }

            },
            { error ->
                Toast.makeText(applicationContext,"$error", Toast.LENGTH_LONG).show()
            }
        )

        // Add the request to the RequestQueue
        queue.add(requestObj)
    }
}