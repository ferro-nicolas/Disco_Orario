package com.example.discoorario

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener{

    private var time: Long = 0
    private lateinit var textScadenza:TextView
    private lateinit var countdownButton: Button
    private lateinit var numberPicker: NumberPicker

    private lateinit var buttonMacchina: Button

    private lateinit var locationManager:LocationManager
    var latitude = 0.0
    var longitude = 0.0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker = findViewById(R.id.numberPicker)
        numberPicker.minValue = 0
        numberPicker.maxValue = 180

        countdownButton = findViewById(R.id.button)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
        )

        prendiCoordinate()
    }

    public override fun onStart() {
        super.onStart()
        countdownButton.setOnClickListener {

            setContentView(R.layout.tempo_rimanente)
            textScadenza = findViewById(R.id.textViewScadenza)

            time = numberPicker.value.toLong()

            val calendar = Calendar.getInstance()
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minute = calendar[Calendar.MINUTE]

            val hourAfter = hour + ((time+minute)/60)
            val minuteAfter = (time+minute)%60
            textScadenza.text = "$hourAfter:$minuteAfter"
            startCountdown()
        }
    }

    private fun startCountdown() {
        object: CountDownTimer(time*60000,1000){
            override fun onTick(l: Long){
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onFinish(){
                inviaNotifica()
                setContentView(R.layout.tempo_scaduto)

                buttonMacchina= findViewById(R.id.button2)
                buttonMacchina.setOnClickListener{
                    val i = Intent(this@MainActivity, MainActivity2::class.java)
                    i.putExtra("latitudine","$latitude")
                    i.putExtra("longitudine","$longitude")
                    startActivity(i)
                }
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun inviaNotifica(){

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel_description"
            notificationManager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this@MainActivity, "my_channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("tempo scaduto")
                .setContentText("Il disco orario è scaduto, affrettati a ritornare alla macchina")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
        notificationManager.notify(0, builder.build())
    }

    private fun prendiCoordinate() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val gpsOptionsIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                startActivity(gpsOptionsIntent)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, this)
            }

        }
    }

    override fun onLocationChanged(location: Location) {
        locationManager.removeUpdates(this)
        latitude=location.latitude
        longitude=location.longitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

}


