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

        @field:SerializedName("nutriscore")
        val nutriScore: String,

        @field:SerializedName("nutrisi")
        val nutrition: Nutrition,

        @field:SerializedName("nutrition_fact_image")
        val nutritionFactImage: String,

        @field:SerializedName("product_image")
        val productImage: String,

        @field:SerializedName("ringkasan")
        val summary: String,

        @field:SerializedName("sajian")
        val serving: String,

        @field:SerializedName("sub_type")
        val subType: String,

        @field:SerializedName("type")
        val type: String,

        @field:SerializedName("confidence")
        val confidence: String
    )

    data class Nutrition(
        @field:SerializedName("energi")
        val energy: String,

        @field:SerializedName("karbohidrat")
        val carbohydrate: Carbohydrate,

        @field:SerializedName("lemak")
        val fat: Fat,

        @field:SerializedName("protein")
        val protein: String,

        @field:SerializedName("sodium")
        val sodium: String
    )

    data class Carbohydrate(
        @field:SerializedName("serat")
        val fiber: String,

        @field:SerializedName("gula")
        val sugar: String,

        @field:SerializedName("total")
        val total: String
    )

    data class Fat(
        @field:SerializedName("jenuh")
        val saturated: String,

        @field:SerializedName("total")
        val total: String,

        @field:SerializedName("trans")
        val trans: String
    )
