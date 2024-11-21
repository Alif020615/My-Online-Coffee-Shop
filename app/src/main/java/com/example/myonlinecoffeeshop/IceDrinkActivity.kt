package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IceDrinkActivity : AppCompatActivity() {

    private val cart = mutableMapOf<Product, Int>() // To keep track of product quantity in the cart
    private var totalPrice = 0.0 // To track the total price of cart items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ice_drink)  // Ensure this layout corresponds to the Ice Drink page

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // List of Ice Drinks
        val icedDrinkList = listOf(
            Product("Iced Green Tea", 5.00, "Ice Drink", R.drawable.iced_green_tea),
            Product("Iced Berry Drink", 4.50, "Ice Drink", R.drawable.iced_berry_drink),
            Product("Iced Mango Juice", 5.20, "Ice Drink", R.drawable.iced_mango_juice),
            Product("Iced Peach Tea", 3.80, "Ice Drink", R.drawable.iced_peach_tea),
            Product("Iced Lemon Tea", 3.50, "Ice Drink", R.drawable.iced_lemon_tea)
        )

        // Pass the correct list to the adapter, including the cart and the callback
        val adapter = ProductAdapter(icedDrinkList) { product ->
            onBuyProduct(product)  // Handle buy button click
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

