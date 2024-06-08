package com.dicoding.nutriseeapp.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.customView.CustomViewEmail
import com.dicoding.nutriseeapp.customView.CustomViewName
import com.dicoding.nutriseeapp.customView.CustomViewPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEditText: CustomViewEmail
    private lateinit var passwordEditText: CustomViewPassword
    private lateinit var nameEditText: CustomViewName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        loadingAnimation = findViewById(R.id.loading_animation)
        val registerButton: Button = findViewById(R.id.register_button)
        emailEditText = findViewById(R.id.edt_email)
        passwordEditText = findViewById(R.id.edt_password)
        nameEditText = findViewById(R.id.edt_name)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                loadingAnimation.visibility = View.VISIBLE
                loadingAnimation.playAnimation()

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { updateTask ->
                                    loadingAnimation.visibility = View.GONE
                                    loadingAnimation.cancelAnimation()

                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Profile update failed: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            loadingAnimation.visibility = View.GONE
                            loadingAnimation.cancelAnimation()
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter name, email, and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
