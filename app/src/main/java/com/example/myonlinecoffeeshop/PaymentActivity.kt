package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var cardName: EditText
    private lateinit var cardNumber: EditText
    private lateinit var expiryDate: EditText
    private lateinit var securityCode: EditText
    private lateinit var postalCode: EditText
    private lateinit var payButton: Button
    private lateinit var cashButton: Button
    private lateinit var totalAmountText: TextView

    private var totalAmount: Double = 0.0
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize FirebaseAuth and get the current user
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser


        // Initialize UI elements
        cardName = findViewById(R.id.cardName)
        cardNumber = findViewById(R.id.cardNumber)
        expiryDate = findViewById(R.id.expiryDate)
        securityCode = findViewById(R.id.securityCode)
        postalCode = findViewById(R.id.postalCode)
        payButton = findViewById(R.id.payButton)
        cashButton = findViewById(R.id.CashButton)
        totalAmountText = findViewById(R.id.totalPriceText)

        // Get the total amount from the intent
        totalAmount = intent.getDoubleExtra("totalPrice", 0.0)
        totalAmountText.text = "Total Amount: RM %.2f".format(totalAmount)

        // Handle payment via card
        payButton.setOnClickListener {
            if (validateCardDetails()) {
                navigateToPaymentSuccess(totalAmount)
            }
        }

        // Handle payment via cash
        cashButton.setOnClickListener {
            navigateToPaymentSuccess(totalAmount)
        }
    }

    private fun redirectToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun navigateToPaymentSuccess(amount: Double) {
        val successIntent = Intent(this, PaymentSuccessActivity::class.java).apply {
            putExtra("amount", amount)  // Pass the total payment amount
            putExtra("referenceNumber", generateReferenceNumber())  // Generate a mock reference number
        }
        startActivity(successIntent)
        finish()
    }

    // Validate card details entered by the user
    private fun validateCardDetails(): Boolean {
        if (TextUtils.isEmpty(cardName.text.toString())) {
            showToast("Please enter the name on the card.")
            return false
        }

        if (TextUtils.isEmpty(cardNumber.text.toString()) || cardNumber.text.toString().length != 16) {
            showToast("Please enter a valid 16-digit card number.")
            return false
        }

        if (TextUtils.isEmpty(expiryDate.text.toString()) || !isValidExpiryDate(expiryDate.text.toString())) {
            showToast("Please enter a valid expiry date (MM/YY).")
            return false
        }

        if (TextUtils.isEmpty(securityCode.text.toString()) || securityCode.text.toString().length != 3) {
            showToast("Please enter a valid 3-digit security code.")
            return false
        }

        if (TextUtils.isEmpty(postalCode.text.toString())) {
            showToast("Please enter a valid ZIP/Postal code.")
            return false
        }

        return true
    }

    // Check if the expiry date is valid (not in the past)
    private fun isValidExpiryDate(expiryDate: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
            dateFormat.isLenient = false
            val expiry = dateFormat.parse(expiryDate)
            expiry != null && expiry.after(Date())  // Check if the expiry date is in the future
        } catch (e: Exception) {
            false
        }
    }

    // Generate a mock reference number for the payment
    private fun generateReferenceNumber(): String {
        return "REF-${System.currentTimeMillis()}"
    }

    // Show a Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
