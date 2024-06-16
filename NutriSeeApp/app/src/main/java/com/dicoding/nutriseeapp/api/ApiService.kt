package com.dicoding.nutriseeapp.api

import com.dicoding.nutriseeapp.model.HistoryResponse
import com.dicoding.nutriseeapp.model.NewsResponse
import com.dicoding.nutriseeapp.model.UserUploadStory
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("upload")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<UserUploadStory>

    @GET("v2/everything")
    fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
    @GET("history")
    fun getHistory(): Call<HistoryResponse>
}
