package edu.udb.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login exitoso
                            val user = auth.currentUser
                            user?.let {
                                val userId = user.uid
                                // Leer datos del usuario en Firebase Database
                                database.child("users").child(userId).get()
                                    .addOnSuccessListener { snapshot ->
                                        val name = snapshot.child("name").value as? String
                                        Toast.makeText(this, "Bienvenido, $name", Toast.LENGTH_SHORT).show()
                                        // Navegar a MenuActivity
                                        startActivity(Intent(this, MenuActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Error al obtener datos de usuario", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // Login fallido
                            Toast.makeText(this, "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            // Navegar a RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}
