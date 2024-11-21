package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HotCoffeeActivity : AppCompatActivity() {

    private val cart = mutableMapOf<Product, Int>() // To keep track of product quantity in cart
    private var totalPrice = 0.0 // To track total price of cart items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_coffee)  // Use the correct layout for Hot Coffee

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // List of Hot Coffees
        val hotCoffeeList = listOf(
            Product("Hot Mocha", 4.50, "Hot Coffee", R.drawable.hot_mocha),
            Product("Hot Latte", 3.00, "Hot Coffee", R.drawable.hot_latte),
            Product("Hot Espresso", 3.50, "Hot Coffee", R.drawable.hot_espresso),
            Product("Hot Caramel Macchiato", 3.20, "Hot Coffee", R.drawable.hot_caramel_machiato),
            Product("Hot Vietnamese Coffee", 4.00, "Hot Coffee", R.drawable.hot_vietnamese_coffee)
        )

        // Adapter for displaying the products
        val adapter = ProductAdapter(hotCoffeeList) { product ->
            onBuyProduct(product)  // Pass the product when it's clicked
        }
        recyclerView.adapter = adapter

        // Checkout Section
        val checkoutButton = findViewById<Button>(R.id.checkoutButton)
        checkoutButton.setOnClickListener {
            proceedToCheckout()
        }

        // Update total price display
        val totalPriceText = findViewById<TextView>(R.id.totalPriceText)
        updateTotalPriceText(totalPriceText)
    }

    private fun onBuyProduct(product: Product) {
        // Update the product quantity in the cart
        val currentQuantity = cart[product] ?: 0
        cart[product] = currentQuantity + 1

        // Update total price
        totalPrice += product.price
        Toast.makeText(this, "Added ${product.name} to cart.", Toast.LENGTH_SHORT).show()

        // Update the total price display
        val totalPriceText = findViewById<TextView>(R.id.totalPriceText)
        updateTotalPriceText(totalPriceText)
    }

    private fun updateTotalPriceText(totalPriceText: TextView) {
        totalPriceText.text = "Total: RM %.2f".format(totalPrice)
    }

    private fun proceedToCheckout() {
        if (cart.isEmpty()) {
            Toast.makeText(this, "Your cart is empty. Add some products before checking out.", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare to pass the total price to the PaymentActivity
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("totalPrice", totalPrice)  // Pass the total price
        }

        startActivity(intent)
    }

}