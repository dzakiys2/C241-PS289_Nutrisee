package com.dicoding.nutriseeapp.model

import com.google.gson.annotations.SerializedName

data class UserUploadStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UploadData?
)

data class UploadData(
    @field:SerializedName("merek")
    val brandName: String,

    @field:SerializedName("nama_produk")
    val productName: String,

    @field:SerializedName("ukuran_porsi")
    val servingSize: String,

    @field:SerializedName("nutrisi")
    val nutrition: Nutrition,

    @field:SerializedName("product_image")
    val productImage: String,

    @field:SerializedName("confidence")
    val confidence: String
)

data class Nutrition(
    @field:SerializedName("energi")
    val energy: String,

    @field:SerializedName("karbohidrat")
    val carbohydrate: Carbohydrate
)

data class Carbohydrate(
    @field:SerializedName("gula")
    val sugar: String,

    @field:SerializedName("total")
    val total: String
)
