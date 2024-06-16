package com.dicoding.nutriseeapp.api

import android.content.Context
import com.dicoding.nutriseeapp.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {
    private const val BASE_URL = "https://1e91-2001-448a-50e1-5aea-51a7-2209-47a7-d055.ngrok-free.app/"
    private lateinit var sessionManager: SessionManager

    fun initialize(context: Context) {
        sessionManager = SessionManager(context)
    }
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        val token = sessionManager.getUserToken()

        if (token != null) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response: Response = chain.proceed(requestBuilder.build())

        if (response.code == 401) { // Unauthorized
            val newToken = refreshToken()
            if (newToken != null) {
                sessionManager.saveUserSession(sessionManager.getUserName() ?: "", sessionManager.getUserEmail() ?: "", newToken)
                val newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
                return@Interceptor chain.proceed(newRequest)
            }
        }
        response
    }

    private fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        var newToken: String? = null
        val lock = Object()
        user.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                newToken = task.result?.token
                synchronized(lock) {
                    lock.notify()
                }
            } else {
                synchronized(lock) {
                    lock.notify()
                }
            }
        }
        synchronized(lock) {
            lock.wait()
        }
        return newToken
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun refreshToken(callback: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return callback(false)
        user.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val newToken = task.result?.token
                if (newToken != null) {
                    sessionManager.saveUserSession(user.displayName ?: "", user.email ?: "", newToken)
                    callback(true)
                } else {
                    callback(false)
                }
            } else {
                callback(false)
            }
        }
    }
}
