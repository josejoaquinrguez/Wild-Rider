package com.example.tfg_josejoaquinrodriguezdiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val registroNombre = findViewById<EditText>(R.id.registroNombre)
        val registroApellido = findViewById<EditText>(R.id.registroApellido)
        val registroCorreo = findViewById<EditText>(R.id.registroCorreo)
        val registroPass = findViewById<EditText>(R.id.registroPass)
        val buttonGuardar = findViewById<Button>(R.id.buttonInicio)

        buttonGuardar.setOnClickListener {
            val nombre = registroNombre.text.toString().trim()
            val apellido = registroApellido.text.toString().trim()
            val correo = registroCorreo.text.toString().trim()
            val password = registroPass.text.toString().trim()

            lateinit var auth: FirebaseAuth

            // Iniciar Firebase Autentificador
            auth = FirebaseAuth.getInstance()

            super.onStart()


            // Agregar el OnClickListener para abrir el registro
            registroUsuario(auth, correo, password)

            val database = Firebase.database("https://proyectojosejoaquinrguezdiz-default-rtdb.europe-west1.firebasedatabase.app").reference

            database.child("Usuarios").child(correo.split("@")[0]).child("nombre").setValue(nombre)
            database.child("Usuarios").child(correo.split("@")[0]).child("apellido").setValue(apellido)
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

        // Enocntrar el boton volver
        val buttonVolver: Button = findViewById(R.id.buttonVolver)

        buttonVolver.setOnClickListener {
            finish()
        }
    }

    private fun resetear() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    // Accede el usuario
    private fun registroUsuario(auth: FirebaseAuth, correo: String, contrasena:String) {
        lateinit var uid: String
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser

                }
                    else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Fallo en la autentificación.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    resetear()
                }
            }
    }
}