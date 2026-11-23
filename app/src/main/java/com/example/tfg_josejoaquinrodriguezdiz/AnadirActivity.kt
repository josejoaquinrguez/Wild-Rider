package com.example.tfg_josejoaquinrodriguezdiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.Locale


class AnadirActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationClickListener, OnMyLocationButtonClickListener {

    //Variable para google maps
    private lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir)

        createFragment()

        //Barra de navegacion
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttonNavegacion)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.buttonInicio -> {
                    // Abre la pantalla inicio
                    val intent = Intent(this, InicioActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.buttonAnadir -> {
                    // Abre la pantalla para añadir
                    val intent = Intent(this, AnadirActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.buttonPerfil -> {
                    // Abre la pantalla de perfil
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun createFragment(){
        val mapsFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapsFragment.getMapAsync(this)
    }

    //Solicita permisos de ubicacion
    private fun solicitarPermisosUbicacion() {
        val permisos = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permisos.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permisos, 1001)
        }
    }

    //Crea el mapa
    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mapa.isMyLocationEnabled = true
        } else {
            solicitarPermisosUbicacion()
        }
        crearMarcador(googleMap)
    }

    //Permite seleccionar un punto del mapa
    private fun crearMarcador(googleMap: GoogleMap){

        // Encontrar el botón Guardar ubicacion
        val buttonGuardarUbicacion: Button = findViewById(R.id.buttonGuardar)
        val guardarTitulo = findViewById<EditText>(R.id.tituloUbicacion)
        val database3 = Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference


        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()

            val opcionMarcador = MarkerOptions()
                .position(latLng)
                .title("Ubicación")
                .snippet("Lat: ${latLng.latitude}, Long: ${latLng.longitude}")

            googleMap.addMarker(opcionMarcador)

            val geocoder = Geocoder(this, Locale.getDefault())
            val ubicaciones = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!ubicaciones.isNullOrEmpty()) {
                val pais = ubicaciones[0].countryName
                val ciudad = ubicaciones[0].locality
                val calle = ubicaciones[0].getAddressLine(0)



                // Agregar el OnClickListener para añadir ubicacion a la BBDD
                buttonGuardarUbicacion.setOnClickListener {

                    val titulo = guardarTitulo.text.toString().trim()

                    database3.child("Ubicaciones").child(titulo).child(pais).child(ciudad).child(calle).child("latitud").setValue(latLng.latitude)

                    database3.child("Ubicaciones").child(titulo).child(pais).child(ciudad).child(calle).child("longitud").setValue(latLng.longitude)
                        .addOnSuccessListener { Toast.makeText(this,"Ubicación guardada", Toast.LENGTH_SHORT).show() }

                    val intent = Intent(this, AnadirActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onMyLocationClick(p0: Location) {

    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }
}