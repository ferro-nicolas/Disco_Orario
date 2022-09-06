package com.example.discoorario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//AIzaSyBQGuCOIGsD_KdkIioOB4OpM_IJV28DGQE
class MainActivity2 : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var latitudeMacchina: Double = 0.0
    private var longitudeMacchina: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val datipassati = intent.extras
        latitudeMacchina = datipassati!!.getString("latitudine")!!.toDouble()
        longitudeMacchina = datipassati.getString("longitudine")!!.toDouble()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val macchina = LatLng(latitudeMacchina, longitudeMacchina)
        mMap.addMarker(MarkerOptions().position(macchina).title("La tua macchina!"))

        val cameraPosition: CameraPosition = CameraPosition.Builder().target(macchina).zoom(15F).build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}