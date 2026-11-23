package com.example.tfg_josejoaquinrodriguezdiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class ModificarPerfilActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificarperfil)

        //Accede a la BBDD para mostrar la informacion en el perfil
        val currentUser = FirebaseAuth.getInstance().currentUser

        val nombrePerfil = findViewById<TextView>(R.id.nombrePerfil)
        val apellidoPerfil = findViewById<TextView>(R.id.apellidosPerfil)
        val modificarMunicipio = findViewById<TextView>(R.id.municipioPerfil)
        val modificarModelo = findViewById<TextView>(R.id.motoPerfil)


        if (currentUser != null) {

            val correo = currentUser.email.toString().split("@")[0]

            val buttonModificar: Button = findViewById(R.id.aceptarPerfil)

            val database =
                Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference
            database.child("Usuarios").child(correo).child("nombre").get().addOnSuccessListener {


                nombrePerfil.text = it.value.toString()
            }
            database.child("Usuarios").child(correo).child("apellido").get().addOnSuccessListener {

                apellidoPerfil.text = it.value.toString()
            }

            database.child("Datos adicionales").child(correo).child("municipio").get().addOnSuccessListener {

                modificarMunicipio.text = it.value.toString()
            }
            database.child("Datos adicionales").child(correo).child("modelo").get().addOnSuccessListener {

                modificarModelo.text = it.value.toString()
            }

            //Permite modificar los datos al darle al boton modificar
            buttonModificar.setOnClickListener {

                val municipio = modificarMunicipio.text.toString().trim()
                val modelo = modificarModelo.text.toString().trim()

                database.child("Datos adicionales").child(correo).child("municipio").setValue(municipio)
                database.child("Datos adicionales").child(correo).child("modelo").setValue(modelo)
                    .addOnSuccessListener { Toast.makeText(this,"Informacion actualizada", Toast.LENGTH_SHORT).show() }

                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
            }
        }

        // Encontrar el botón Volver al Perfil
        val buttonVolverPerfil: Button = findViewById(R.id.volverPerfil)

        // Agregar el OnClickListener para abrir el registro
        buttonVolverPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

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