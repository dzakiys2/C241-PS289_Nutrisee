package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.utils.SessionManager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        sessionManager = SessionManager(this)
        ApiClient.initialize(this)

        navigateNext()
    }

    private fun navigateNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            ApiClient.refreshToken { success ->
                if (success) {
                    if (sessionManager.getUserName() != null && sessionManager.getUserEmail() != null) {
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SplashScreenActivity, OnBoard1Activity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }, 1000)
    }
}
