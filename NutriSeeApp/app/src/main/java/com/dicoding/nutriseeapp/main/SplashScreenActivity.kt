package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.utils.SessionManager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        sessionManager = SessionManager(this)

        navigateNext()
    }

    private fun navigateNext() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.getUserName() != null && sessionManager.getUserEmail() != null) {
                // If user session exists, navigate to MainActivity
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                // If no user session, navigate to OnBoard1Activity
                val intent = Intent(this@SplashScreenActivity, OnBoard1Activity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}
