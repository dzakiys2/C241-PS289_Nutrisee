package com.dicoding.nutriseeapp.model

import com.google.gson.annotations.SerializedName

data class HistoryItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("product_image")
    val productImage: String,

    @field:SerializedName("nama_produk")
    val namaProduk: String,

    @field:SerializedName("nutriscore")
    val nutriscore: String,

    @field:SerializedName("display_timestamp")
    val displayTimestamp: String
)

data class HistoryResponse(
    @field:SerializedName("data")
    val data: Map<String, HistoryItem>
)
