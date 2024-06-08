package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.customView.CustomViewEmail
import com.dicoding.nutriseeapp.customView.CustomViewPassword
import com.dicoding.nutriseeapp.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEditText: CustomViewEmail
    private lateinit var passwordEditText: CustomViewPassword
    private lateinit var sessionManager: SessionManager
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        initializeFirebase()
        setClickListeners()
    }

    private fun initializeViews() {
        loadingAnimation = findViewById(R.id.loading_animation)
        emailEditText = findViewById(R.id.edt_email)
        passwordEditText = findViewById(R.id.edt_password)
        loginButton = findViewById(R.id.sign_in_button)
        signUpTextView = findViewById(R.id.signup_click)
    }

    private fun initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)
    }

    private fun setClickListeners() {
        signUpTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginButton.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            showLoadingAnimation()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideLoadingAnimation()
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val name = user?.displayName ?: ""
                        sessionManager.saveUserSession(name, email)
                        navigateToHome()
                    } else {
                        handleLoginError(task.exception?.message)
                    }
                }
        } else {
            showEmptyFieldsMessage()
        }
    }

    private fun showLoadingAnimation() {
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
    }

    private fun hideLoadingAnimation() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleLoginError(errorMessage: String?) {
        Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyFieldsMessage() {
        Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
    }
}

