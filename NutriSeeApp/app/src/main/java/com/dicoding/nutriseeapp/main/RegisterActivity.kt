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
import com.dicoding.nutriseeapp.customView.CustomViewName
import com.dicoding.nutriseeapp.customView.CustomViewPassword
import com.dicoding.nutriseeapp.utils.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEditText: CustomViewEmail
    private lateinit var passwordEditText: CustomViewPassword
    private lateinit var nameEditText: CustomViewName
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)

        loadingAnimation = findViewById(R.id.loading_animation)
        val registerButton: Button = findViewById(R.id.register_button)
        emailEditText = findViewById(R.id.edt_email)
        passwordEditText = findViewById(R.id.edt_password)
        nameEditText = findViewById(R.id.edt_name)

        val signInTextView: TextView = findViewById(R.id.signin_click)
        signInTextView.setOnClickListener {
            goToLoginActivity()
        }

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            showLoadingAnimation()

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {
                                val token = tokenTask.result?.token
                                if (token != null) {
                                    sessionManager.saveUserSession(name, email, token)
                                    updateUserProfile(name, email, token)
                                } else {
                                    hideLoadingAnimation()
                                    showToast("Token generation failed")
                                }
                            } else {
                                hideLoadingAnimation()
                                showToast("Token generation failed: ${tokenTask.exception?.message}")
                            }
                        }
                    } else {
                        handleRegistrationFailure(task)
                    }
                }
        } else {
            showToast("Please enter name, email, and password")
        }
    }

    private fun showLoadingAnimation() {
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
    }

    private fun updateUserProfile(name: String, email: String, token: String) {
        val user = firebaseAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { updateTask ->
                hideLoadingAnimation()

                if (updateTask.isSuccessful) {
                    handleRegistrationSuccess(name, email, token)
                } else {
                    handleProfileUpdateFailure(updateTask)
                }
            }
    }

    private fun hideLoadingAnimation() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
    }

    private fun handleRegistrationSuccess(name: String, email: String, token: String) {
        sessionManager.saveUserSession(name, email, token)
        showToast("Registration successful!")
        goToLoginActivity()
        finish()
    }

    private fun handleRegistrationFailure(task: Task<AuthResult>) {
        hideLoadingAnimation()
        showToast("Registration failed: ${task.exception?.message}")
    }

    private fun handleProfileUpdateFailure(task: Task<Void>) {
        hideLoadingAnimation()
        showToast("Profile update failed: ${task.exception?.message}")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
