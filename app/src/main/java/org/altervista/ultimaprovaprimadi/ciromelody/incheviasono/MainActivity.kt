package org.altervista.ultimaprovaprimadi.ciromelody.incheviasono

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    var currentLocation: Location? = null
    private lateinit var bps_button:Button
    private lateinit var tx_coordinate_gps:TextView
    private lateinit var button_memorizza:Button
    private lateinit var button_carica_posizione:Button
    private var stringa_gps:String = "non è stato rilevato nessun spostamento"
    private var stringa_gps_x:String="01"
    private var stringa_gps_y:String="01"
    var archivio_coordinate=Preferenze(MainApplication.appContext)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(org.altervista.ultimaprovaprimadi.ciromelody.incheviasono.R.layout.activity_main)

        tx_coordinate_gps=findViewById(R.id.tx_coordinate_gps)
        bps_button=findViewById(R.id.button)
        button_memorizza = findViewById(R.id.bn_memorizza_posizione)
        button_carica_posizione = findViewById(R.id.bn_richiama_posizione)
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {


                stringa_gps="Latitudine ${location.latitude.toString()} ,Longitudine ${location.longitude.toString()}"
               stringa_gps_x=location.latitude.toString()
                stringa_gps_y=location.longitude.toString()
                tx_coordinate_gps.setText(stringa_gps)
                currentLocation=location
                Log.i("Posizione utente", location.latitude.toString())
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) { tx_coordinate_gps.setText("attiva il GPS")}
        }
        bps_button.setOnClickListener(View.OnClickListener {
            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
                tx_coordinate_gps.setText(stringa_gps)
                dammi_la_via()
                 } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available")
            }


           })
        /**
         * salva le coordinate
         */
        /**
         * salva le coordinate
         */
       button_memorizza.setOnClickListener(View.OnClickListener {
            if (currentLocation != null) {
                Toast.makeText(
                    applicationContext,
                    "Latitudine: " + currentLocation!!.latitude
                            + " longitudine: " + currentLocation!!.longitude /*messagge*/,
                    Toast.LENGTH_SHORT
                ).show()
               archivio_coordinate.saveValue(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                )
            }
        })
        /**
         * carica le coordinate e ci guida alla destinazione attraverso google maps
         */
        /**
         * carica le coordinate e ci guida alla destinazione attraverso google maps
         */
        button_carica_posizione.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) { //permessi non concessi
                Toast.makeText(
                    applicationContext, "Devi attivare la localizzazione" /*messagge*/,
                    Toast.LENGTH_SHORT
                ).show()
            } else {  if (currentLocation!=null){

                Log.d("VIA","Locazione corrente:"+currentLocation)

                val latitudine: Double =
                   archivio_coordinate.loadValue().get(Preferenze.LATITUDINE)!!.toDouble()
                val longitudine: Double =
                    archivio_coordinate.loadValue().get(Preferenze.LONGITUDINE)!!.toDouble()

                val URI =
                    "http://maps.google.com/maps?saddr=" + currentLocation!!.latitude + "," +
                            currentLocation!!.longitude +
                            "&daddr=" + latitudine + "," + longitudine
                Log.d("VIA","Locazione destinazione:"+latitudine+" "+longitudine)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URI))
                startActivity(intent)

            }else{ // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
                Toast.makeText(
                    applicationContext, "Ripremi il tasto sto calcolando la tua posizione" /*messagge*/,
                    Toast.LENGTH_SHORT
                ).show()
                 }
        }})
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // Chiedo il permesso
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }else{val posizione=locationListener
                                   }



    }
    fun dammi_la_via(){
        // Create a Uri from an intent string. Use the result to create an Intent.
        if(stringa_gps_x=="01"){return}
        val gmmIntentUri = Uri.parse("google.streetview:cbll=${stringa_gps_x},${stringa_gps_y}")

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }
    override fun onBackPressed() { /*    Scrivi qui il tuo codice prima di uscire
        }*/
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://ultimaprovaprimadi.altervista.org/le-mie-app-android/")
        )
        startActivity(browserIntent)
        super.onBackPressed()
    }
}
