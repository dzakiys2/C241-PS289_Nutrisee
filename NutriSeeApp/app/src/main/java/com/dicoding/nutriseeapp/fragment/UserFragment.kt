package com.dicoding.nutriseeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.main.LoginActivity
import com.dicoding.nutriseeapp.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        userNameTextView = view.findViewById(R.id.userNameTextView)
        userEmailTextView = view.findViewById(R.id.userEmailTextView)
        btnLogout = view.findViewById(R.id.btnLogout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        currentUser?.let { user ->
            userNameTextView.text = user.displayName
            userEmailTextView.text = user.email
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        sessionManager.clearSession()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}

