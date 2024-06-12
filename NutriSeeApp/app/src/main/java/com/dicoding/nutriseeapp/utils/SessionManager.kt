package com.dicoding.nutriseeapp.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val USER_TOKEN = "user_token"
    }

    fun saveUserSession(name: String, email: String, token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun getUserToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
