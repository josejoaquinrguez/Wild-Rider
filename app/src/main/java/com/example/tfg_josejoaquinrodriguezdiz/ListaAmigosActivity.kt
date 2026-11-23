package com.example.tfg_josejoaquinrodriguezdiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListaAmigosActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listaamigos)

        // Encontrar el botón Volver a perfil
        val buttonVolverAmigos: Button = findViewById(R.id.buttonVolverAmigos)

        // Agregar el OnClickListener para abrir el registro
        buttonVolverAmigos.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

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