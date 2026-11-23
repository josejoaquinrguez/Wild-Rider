package com.example.tfg_josejoaquinrodriguezdiz

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val correoLogin = findViewById<EditText>(R.id.correoLogin)
        val passLogin = findViewById<EditText>(R.id.passLogin)
        val buttonGuardar = findViewById<Button>(R.id.buttonLogin)

        //Ingresa los datos para iniciar sesion
        buttonGuardar.setOnClickListener {
            val correo = correoLogin.text.toString().trim()
            val password = passLogin.text.toString().trim()

            // Si los campos estan vacios se resetea la pagina
            if (correo == "" || password == ""){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            lateinit var auth: FirebaseAuth

            // Iniciar Firebase Autentificador
            auth = FirebaseAuth.getInstance()

            acceder(auth, correo, password)
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pantallaAcceso)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Encontrar el botón Crear Cuenta
        val buttonCrearCuenta: Button = findViewById(R.id.buttonCrearCuenta)

        // Agregar el OnClickListener para abrir el registro
        buttonCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

    }
    //Resetea la página
    private fun resetear() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Iniciar usuario
    private fun acceder(auth: FirebaseAuth, correo: String, contrasena:String) {
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si el correo existe accede a la BBDD
                    val user = auth.currentUser
                } else {
                    // Si el correo no esta registrado lanza mensaje de error
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    resetear()
                }
            }
    }
}