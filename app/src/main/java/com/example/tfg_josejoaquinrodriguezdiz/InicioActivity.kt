package com.example.tfg_josejoaquinrodriguezdiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        //Mostrar Titulo en la lista
        val listaTitulos = mutableListOf<String>()
        val listaUbicacion = findViewById<ListView>(R.id.listaTitulos)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTitulos)
        listaUbicacion.adapter = adapter

        val database = Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference

        database.child("Ubicaciones").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaTitulos.clear()
                for (ubicacion in snapshot.children) {
                    val titulo = ubicacion.key
                    titulo?.let { listaTitulos.add(it) }
                }
                adapter.notifyDataSetChanged()

                listaUbicacion.setOnItemClickListener { _, _, position, _ ->
                    val tituloSeleccionado = listaTitulos[position]

                    val ubicacionRef = database.child("Ubicaciones").child(tituloSeleccionado)

                    ubicacionRef.get().addOnSuccessListener { snapshot ->

                        // Buscar la ubicacion en la BBDD
                        for (paisSnap in snapshot.children) {
                            for (ciudadSnap in paisSnap.children) {
                                for (calleSnap in ciudadSnap.children) {
                                    val lat = calleSnap.child("latitud").getValue(Double::class.java)
                                    val lng = calleSnap.child("longitud").getValue(Double::class.java)

                                    //Abre la ubicacion en Google Maps
                                    if (lat != null && lng != null) {
                                        val ubicacion = Uri.parse("geo:$lat,$lng?q=$lat,$lng($tituloSeleccionado)")
                                        val intent = Intent(Intent.ACTION_VIEW, ubicacion)
                                        intent.setPackage("com.google.android.apps.maps")
                                        startActivity(intent)
                                        return@addOnSuccessListener
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //En caso de error al cargar, lanzara el mensaje
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InicioActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })

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
    }