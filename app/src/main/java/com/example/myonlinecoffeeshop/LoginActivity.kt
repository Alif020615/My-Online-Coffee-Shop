package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {

    private lateinit var btnLog: Button
    private lateinit var btnRegister: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize components
        btnLog = findViewById(R.id.btnLogIn)
        btnRegister = findViewById(R.id.btnRegister)
        email = findViewById(R.id.emailLogIn)
        password = findViewById(R.id.passwordLogIn)

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Customer")

        // Log In functionality
        btnLog.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                logIn(emailText, passwordText)
            } else {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()
            }
        }

        // Register button functionality
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn(email: String, password: String) {
        databaseReference.orderByChild("customerEmail").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var loginSuccessful = false

                // Loop through all customers and check the password
                for (custSnapshot in dataSnapshot.children) {
                    val model = custSnapshot.getValue(Model::class.java)
                    if (model != null && model.customerPassword == password) {
                        loginSuccessful = true
                        break
                    }
                }

                // Show success or failure message
                if (loginSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java) // Navigate to MainActivity3
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
