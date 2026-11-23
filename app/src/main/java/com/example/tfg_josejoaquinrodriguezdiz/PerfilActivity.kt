package com.example.tfg_josejoaquinrodriguezdiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Accede a las BBDD para recuperar los datos
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {

            val correo=currentUser.email.toString().split("@")[0]

            val database =
                Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference
            database.child("Usuarios").child(correo).child("nombre").get().addOnSuccessListener {
                val nombrePerfil = findViewById<TextView>(R.id.nombrePerfil)

                nombrePerfil.text = it.value.toString()
            }
            database.child("Usuarios").child(correo).child("apellido").get().addOnSuccessListener {
                val apellidoPerfil = findViewById<TextView>(R.id.apellidoPerfil)
                apellidoPerfil.text = it.value.toString()
            }

            val database2 =
                Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference
            database2.child("Datos adicionales").child(correo).child("municipio").get().addOnSuccessListener {
                val municipioPerfil = findViewById<TextView>(R.id.municipioPerfil)

                municipioPerfil.text = it.value.toString()
            }
            database2.child("Datos adicionales").child(correo).child("modelo").get().addOnSuccessListener {
                val modeloPerfil = findViewById<TextView>(R.id.motoPerfil)
                modeloPerfil.text = it.value.toString()
            }

        }

        // Encontrar el botón Modificar Perfil
        val buttonModificarPerfil: Button = findViewById(R.id.buttonModificarPerfil)

        // Agregar el OnClickListener para abrir el registro
        buttonModificarPerfil.setOnClickListener {

            val intent = Intent(this, ModificarPerfilActivity::class.java)
            startActivity(intent)
        }

        // Encontrar el botón Modificar Perfil
        val buttonCerrarSesion: Button = findViewById(R.id.buttonCerrarSesion)

        // Agregar el OnClickListener para abrir el registro
        buttonCerrarSesion.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
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