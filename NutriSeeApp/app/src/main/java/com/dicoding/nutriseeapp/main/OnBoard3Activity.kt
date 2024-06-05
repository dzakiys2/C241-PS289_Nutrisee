package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutriseeapp.R

class OnBoard3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard3)

        val nextLoginButton: Button = findViewById(R.id.next_login_button)
        val nextPrevButton: Button = findViewById(R.id.next_regis_button)

        nextLoginButton.setOnClickListener {
            navigateToLoginActivity()
        }

        nextPrevButton.setOnClickListener {
            navigateToRegisterActivity()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java) // Replace MainActivity with your target activity
        startActivity(intent)
        finish()
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java) // Replace SplashScreenActivity with your previous activity
        startActivity(intent)
        finish()
    }
}