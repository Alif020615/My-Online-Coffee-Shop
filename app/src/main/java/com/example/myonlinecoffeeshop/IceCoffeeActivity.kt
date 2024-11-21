package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IceCoffeeActivity : AppCompatActivity() {

    private val cart = mutableMapOf<Product, Int>() // To keep track of product quantity in cart
    private var totalPrice = 0.0 // To track total price of cart items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ice_coffee)  // Ensure this layout corresponds to the Ice Coffee page

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // List of Ice Coffees
        val iceCoffeeList = listOf(
            Product("Iced Mocha", 5.50, "Ice Coffee", R.drawable.iced_mocha),
            Product("Iced Latte", 4.00, "Ice Coffee", R.drawable.iced_latte),
            Product("Iced Espresso", 4.50, "Ice Coffee", R.drawable.iced_espresso),
            Product("Iced Caramel Macchiato", 4.20, "Ice Coffee", R.drawable.iced_caramel_machiatto),
            Product("Iced Vietnamese Coffee", 5.00, "Ice Coffee", R.drawable.iced_vietnamese_coffee)
        )

        // Adapter for displaying the products
        val adapter = ProductAdapter(iceCoffeeList) { product ->
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

        // Prepare to pass cart and total price to the PaymentActivity
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("totalPrice", totalPrice)  // Pass total price to PaymentActivity
            // Convert the cart map to a list of products and quantities, then pass them
            val cartItems = cart.map { it.key } // List of products
            val cartQuantities = cart.map { it.value } // List of quantities
            putParcelableArrayListExtra("cartItems", ArrayList(cartItems))  // Pass the list of products (Product objects)
            putIntegerArrayListExtra("cartQuantities", ArrayList(cartQuantities)) // Pass the list of quantities
        }

        startActivity(intent)
    }
}
