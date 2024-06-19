package com.dicoding.nutriseeapp.model

import com.google.gson.annotations.SerializedName

data class UserUploadData(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: Data?
)

data class HistoryResponse(
    @field:SerializedName("data")
    val data: Map<String, Data>
)

data class Data(
    @field:SerializedName("history_id")
    val historyId: String,

    @field:SerializedName("product_image")
    val productImage: String,

    @field:SerializedName("merek")
    val brandName: String,

    @field:SerializedName("nama_produk")
    val productName: String,

    @field:SerializedName("confidence")
    val confidence: String,

    @field:SerializedName("barcode")
    val barcode: String,

    @field:SerializedName("barcode_url")
    val barcodeUrl: String,

    @field:SerializedName("ukuran_porsi")
    val servingSize: String,

    @field:SerializedName("nutrient_profiling_class")
    val nutritionProfilingClass: String,

    @field:SerializedName("halal_description")
    val halalDescription: String,

    @field:SerializedName("isHalal")
    val isHalal: Boolean,

    @field:SerializedName("status_logo_url")
    val statusLogoUrl: String,

    @field:SerializedName("nutriscore")
    val nutriScore: String,

    @field:SerializedName("nutriscore_label_description")
    val nutriScoreLabelDescription: String,

    @field:SerializedName("sajian")
    val serving: String,

    @field:SerializedName("summary")
    val summary: Summary,

    @field:SerializedName("nutrisi")
    val nutrition: Nutrition,

    @field:SerializedName("nutrition_fact_image")
    val nutritionFactImage: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("sub_type")
    val subType: String,

    @field:SerializedName("display_timestamp")
    val displayTimestamp: String
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

data class Summary(
    @field:SerializedName("salt_status")
    val saltStatus: String,

    @field:SerializedName("salt_percentage")
    val saltPercentage: String,

    @field:SerializedName("sat_fat_percentage")
    val satFatPercentage: String,

    @field:SerializedName("sat_fat_status")
    val satFatStatus: String,

    @field:SerializedName("sugar_percentage")
    val sugarPercentage: String,

    @field:SerializedName("sugar_status")
    val sugarStatus: String,

    @field:SerializedName("total_fat_percentage")
    val totalFatPercentage: String,

    @field:SerializedName("total_fat_status")
    val totalFatStatus: String,

    @field:SerializedName("salt_status_url")
    val saltStatusUrl: String,

    @field:SerializedName("salt_summary")
    val saltSummary: String,

    @field:SerializedName("sat_fat_status_url")
    val satFatStatusUrl: String,

    @field:SerializedName("sat_fat_summary")
    val satFatSummary: String,

    @field:SerializedName("sugar_status_url")
    val sugarStatusUrl: String,

    @field:SerializedName("sugar_summary")
    val sugarSummary: String,

    @field:SerializedName("total_fat_status_url")
    val totalFatStatusUrl: String,

    @field:SerializedName("total_fat_summary")
    val totalFatSummary: String
)