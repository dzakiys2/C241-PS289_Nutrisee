package com.dicoding.nutriseeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsResponse(
    val articles: List<Article>
)

@Parcelize
data class Article(
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
) : Parcelable
