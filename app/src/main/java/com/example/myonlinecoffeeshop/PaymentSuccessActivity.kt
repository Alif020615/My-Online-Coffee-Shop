package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var amountPaidText: TextView
    private lateinit var referenceNumberText: TextView
    private lateinit var dateTimeText: TextView
    private lateinit var returnHomeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        // Initialize UI elements
        amountPaidText = findViewById(R.id.amountPaid)
        referenceNumberText = findViewById(R.id.referenceNumber)
        dateTimeText = findViewById(R.id.dateTime)
        returnHomeButton = findViewById(R.id.returnHomeButton)

        // Retrieve total amount and reference number from the Intent
        val totalAmount = intent.getDoubleExtra("amount", 0.0)
        val referenceNumber = intent.getStringExtra("referenceNumber") ?: "No Reference"

        // Mock: Retrieve customer details from Firebase Authentication
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val customerUID = firebaseUser?.uid  // Get the logged-in user's UID
        val customerName = firebaseUser?.displayName ?: "Unknown Name"
        val customerEmail = firebaseUser?.email ?: "Unknown Email"

        // Set the amount and reference number to the TextViews
        amountPaidText.text = "RM %.2f".format(totalAmount)
        referenceNumberText.text = referenceNumber

        // Get the current date and time
        val currentDateTime = SimpleDateFormat("MMM dd, yyyy, HH:mm:ss", Locale.getDefault()).format(Date())
        dateTimeText.text = currentDateTime

        // Save payment details along with customer details to Firebase Realtime Database
        if (customerUID != null) {
            saveOrderToFirebase(
                customerUID, customerName, customerEmail,
                totalAmount, referenceNumber, currentDateTime
            )
        } else {
            Toast.makeText(this, "purchase successful!", Toast.LENGTH_SHORT).show()
        }

        // Set OnClickListener for Return to Homepage button
        returnHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveOrderToFirebase(
        customerUID: String, customerName: String, customerEmail: String,
        amount: Double, referenceNumber: String, date: String
    ) {
        val database = FirebaseDatabase.getInstance().reference

        // Create a unique key for the order entry
        val orderId = database.child("orders").child(customerUID).push().key

        // Create a map for the order details
        val orderDetails = mapOf(
            "customerName" to customerName,
            "customerEmail" to customerEmail,
            "amountPaid" to amount,
            "referenceNumber" to referenceNumber,
            "dateTime" to date
        )

        // Save under the user's UID
        if (orderId != null) {
            database.child("orders").child(customerUID).child(orderId).setValue(orderDetails)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Order saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save order: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
