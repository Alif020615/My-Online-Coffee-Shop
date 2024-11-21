package com.example.myonlinecoffeeshop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Find the button
        val startButton: ImageButton = findViewById(R.id.btnIntro)

        // Set click listener
        startButton.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close IntroActivity
        }
    }
}