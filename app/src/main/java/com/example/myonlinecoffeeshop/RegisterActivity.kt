package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var btnLogInRedirect: Button
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize components
        btnRegister = findViewById(R.id.btnRegister)
        btnLogInRedirect = findViewById(R.id.btnLogInRedirect)
        name = findViewById(R.id.nameRegister)
        email = findViewById(R.id.emailRegister)
        password = findViewById(R.id.passwordRegister)

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Customer")

        // Register functionality
        btnRegister.setOnClickListener {
            val nameText = name.text.toString()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (nameText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                registerUser(nameText, emailText, passwordText)
            } else {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()
            }
        }

        // Redirect to Login page
        btnLogInRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Create a new model object to store in Firebase
        val newUser = Model(name, email, password)

        // Push new user data to Firebase
        databaseReference.push().setValue(newUser)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java) // Redirect to login page
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
