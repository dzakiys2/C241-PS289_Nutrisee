package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutriseeapp.R

class OnBoard1Activity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        val nextButton: Button = findViewById(R.id.next_button) // Assuming the ID is next_button
        nextButton.setOnClickListener {
            navigateToNextActivity()
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, OnBoard2Activity::class.java) // Replace MainActivity with your target activity
        startActivity(intent)
        finish()
    }

}
