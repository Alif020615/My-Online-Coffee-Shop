package com.example.myonlinecoffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHotDrinks = findViewById<ImageButton>(R.id.btnHotDrinks)
        val btnIceDrinks = findViewById<ImageButton>(R.id.btnIceDrinks)
        val btnHotCoffee = findViewById<ImageButton>(R.id.btnHotCoffee)
        val btnIceCoffee = findViewById<ImageButton>(R.id.btnIceCoffee)

        btnHotDrinks.setOnClickListener {
            startActivity(Intent(this, HotDrinkActivity::class.java))
        }

        btnIceDrinks.setOnClickListener {
            startActivity(Intent(this, IceDrinkActivity::class.java))
        }

        btnHotCoffee.setOnClickListener {
            startActivity(Intent(this, HotCoffeeActivity::class.java))
        }

        btnIceCoffee.setOnClickListener {
            startActivity(Intent(this, IceCoffeeActivity::class.java))
        }
    }
}
