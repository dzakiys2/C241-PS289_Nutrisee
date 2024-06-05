package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutriseeapp.R

class OnBoard2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard2)

        val nextButton: Button = findViewById(R.id.next2_button)
        val prevButton: Button = findViewById(R.id.prev_button)

        nextButton.setOnClickListener {
            navigateToNextActivity()
        }

        prevButton.setOnClickListener {
            navigateToPreviousActivity()
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, OnBoard3Activity::class.java) // Replace MainActivity with your target activity
        startActivity(intent)
        finish()
    }

    private fun navigateToPreviousActivity() {
        val intent = Intent(this, OnBoard1Activity::class.java) // Replace SplashScreenActivity with your previous activity
        startActivity(intent)
        finish()
    }
}
