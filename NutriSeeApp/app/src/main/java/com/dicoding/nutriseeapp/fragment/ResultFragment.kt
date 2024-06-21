package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.model.Carbohydrate

class ResultFragment : Fragment() {

    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var brandNameTextView: TextView
    private lateinit var barcodeTextView: TextView
    private lateinit var barcodeUrlImageView: ImageView
    private lateinit var nutritionProfilingClassTextView: TextView
    private lateinit var halalDescriptionTextView: TextView
    private lateinit var statusLogoImageView: ImageView
    private lateinit var nutriScoreImageView: ImageView
    private lateinit var nutriScoreLabelDescriptionTextView: TextView
    private lateinit var energyTextView: TextView
    private lateinit var totalCarbohydrateTextView: TextView
    private lateinit var sugarTextView: TextView
    private lateinit var totalFatTextView: TextView
    private lateinit var saturatedFatTextView: TextView
    private lateinit var transFatTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var sodiumTextView: TextView
    private lateinit var confidenceTextView: TextView
    private lateinit var displayTimestampTextView: TextView
    //private lateinit var nutritionFactImageView: ImageView
    private lateinit var totalFatSummaryTextView: TextView
    private lateinit var satFatSummaryTextView: TextView
    private lateinit var sugarSummaryTextView: TextView
    private lateinit var saltSummaryTextView: TextView
    private lateinit var totalFatStatusImageView: ImageView
    private lateinit var satFatStatusImageView: ImageView
    private lateinit var sugarStatusImageView: ImageView
    private lateinit var saltStatusImageView: ImageView
    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        productImageView = view.findViewById(R.id.ivProductImage)
        barcodeTextView = view.findViewById(R.id.tvBarcode)
        barcodeUrlImageView = view.findViewById(R.id.ivBarcode)
        statusLogoImageView = view.findViewById(R.id.ivHalalImage)
        nutriScoreImageView = view.findViewById(R.id.ivNutriScore)
        productNameTextView = view.findViewById(R.id.tvProductName)
        brandNameTextView = view.findViewById(R.id.tvBrandName)
        nutritionProfilingClassTextView = view.findViewById(R.id.tvNutriScoreprofiling)
        halalDescriptionTextView = view.findViewById(R.id.tvHalalImage)
        nutriScoreLabelDescriptionTextView = view.findViewById(R.id.tvNutriScorelabel)
        energyTextView = view.findViewById(R.id.tvEnergy)
        totalCarbohydrateTextView = view.findViewById(R.id.tvTotalCarbohydrate)
        sugarTextView = view.findViewById(R.id.tvSugar)
        totalFatTextView = view.findViewById(R.id.tvTotalFat)
        saturatedFatTextView = view.findViewById(R.id.tvSaturatedFat)
        transFatTextView = view.findViewById(R.id.tvTransFat)
        proteinTextView = view.findViewById(R.id.tvProtein)
        sodiumTextView = view.findViewById(R.id.tvSodium)
        confidenceTextView = view.findViewById(R.id.tvConfidence)
        displayTimestampTextView = view.findViewById(R.id.tvTimeStamp)
        //nutritionFactImageView = view.findViewById(R.id.ivNutritionFactImage)
        totalFatSummaryTextView = view.findViewById(R.id.tvTotalfatSummary)
        satFatSummaryTextView = view.findViewById(R.id.tvSatfatSummary)
        sugarSummaryTextView = view.findViewById(R.id.tvSugarSummary)
        saltSummaryTextView = view.findViewById(R.id.tvSaltSummary)
        totalFatStatusImageView = view.findViewById(R.id.ivStatustotalFat)
        satFatStatusImageView = view.findViewById(R.id.ivStatusSatFat)
        sugarStatusImageView = view.findViewById(R.id.ivStatusSugar)
        saltStatusImageView = view.findViewById(R.id.ivStatusSalt)
        backButton = view.findViewById(R.id.btn_back)

        backButton.setOnClickListener {
            navigateToHistoryFragment()
        }
        arguments?.let {
            val imageUri = it.getString(ARG_IMAGE_URI)
            val productName = it.getString(ARG_PRODUCT_NAME)
            val brandName = it.getString(ARG_BRAND_NAME)
            val barcode = it.getString(ARG_BARCODE)
            val barcodeUrl = it.getString(ARG_BARCODE_URL)
            val nutritionProfilingClass = it.getString(ARG_NUTRITION_PROFILING_CLASS)
            val halalDescription = it.getString(ARG_HALAL_DESCRIPTION)
            val statusLogoUrl = it.getString(ARG_STATUS_LOGO_URL)
            val nutriScore = it.getString(ARG_NUTRISCORE)
            val nutriScoreLabelDescription = it.getString(ARG_NUTRISCORE_LABEL_DESCRIPTION)
            val displayTimestamp = it.getString(ARG_DISPLAY_TIMESTAMP)
            val energy = it.getString(ARG_ENERGY)
            val sugar = it.getString(ARG_SUGAR)
            val totalCarbohydrate = it.getString(ARG_TOTAL_CARBOHYDRATE)
            val totalFat = it.getString(ARG_TOTAL_FAT)
            val saturatedFat = it.getString(ARG_SATURATED_FAT)
            val transFat = it.getString(ARG_TRANS_FAT)
            val protein = it.getString(ARG_PROTEIN)
            val sodium = it.getString(ARG_SODIUM)
            val confidence = it.getString(ARG_CONFIDENCE)
            //val nutritionFactImage = it.getString(ARG_NUTRITION_FACT_IMAGE)
            val totalFatSummary = it.getString(ARG_TOTAL_FAT_SUMMARY)
            val satFatSummary = it.getString(ARG_SAT_FAT_SUMMARY)
            val sugarSummary = it.getString(ARG_SUGAR_SUMMARY)
            val saltSummary = it.getString(ARG_SALT_SUMMARY)
            val totalFatStatusUrl = it.getString(ARG_TOTAL_FAT_STATUS_URL)
            val satFatStatusUrl = it.getString(ARG_SAT_FAT_STATUS_URL)
            val sugarStatusUrl = it.getString(ARG_SUGAR_STATUS_URL)
            val saltStatusUrl = it.getString(ARG_SALT_STATUS_URL)

            Glide.with(this)
                .load(imageUri)
                .fitCenter()
                .into(productImageView)

            Glide.with(this)
                .load(barcodeUrl)
                .fitCenter()
                .into(barcodeUrlImageView)

            Glide.with(this)
                .load(statusLogoUrl)
                .fitCenter()
                .into(statusLogoImageView)

            Glide.with(this)
                .load(nutriScore)
                .fitCenter()
                .into(nutriScoreImageView)

            //Glide.with(this).load(nutritionFactImage).fitCenter().into(nutritionFactImageView)

            Glide.with(this)
                .load(totalFatStatusUrl)
                .fitCenter()
                .into(totalFatStatusImageView)

            Glide.with(this)
                .load(satFatStatusUrl)
                .fitCenter()
                .into(satFatStatusImageView)

            Glide.with(this)
                .load(sugarStatusUrl)
                .fitCenter()
                .into(sugarStatusImageView)

            Glide.with(this)
                .load(saltStatusUrl)
                .fitCenter()
                .into(saltStatusImageView)

            productNameTextView.text = productName
            brandNameTextView.text = brandName
            barcodeTextView.text = barcode
            nutritionProfilingClassTextView.text = nutritionProfilingClass
            halalDescriptionTextView.text = halalDescription
            nutriScoreLabelDescriptionTextView.text = nutriScoreLabelDescription
            displayTimestampTextView.text = displayTimestamp
            energyTextView.text = energy
            totalCarbohydrateTextView.text = totalCarbohydrate
            sugarTextView.text = sugar
            totalFatTextView.text = totalFat
            saturatedFatTextView.text = saturatedFat
            transFatTextView.text = transFat
            proteinTextView.text = protein
            sodiumTextView.text = sodium
            confidenceTextView.text = confidence
            totalFatSummaryTextView.text = totalFatSummary
            satFatSummaryTextView.text = satFatSummary
            sugarSummaryTextView.text = sugarSummary
            saltSummaryTextView.text = saltSummary
        }
        return view
    }
    private fun navigateToHistoryFragment() {
        val fragment = HistoryFragment()
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_BRAND_NAME = "brand_name"
        private const val ARG_BARCODE = "barcode"
        private const val ARG_BARCODE_URL = "barcode_url"
        private const val ARG_NUTRITION_PROFILING_CLASS = "nutrition_profiling_class"
        private const val ARG_HALAL_DESCRIPTION = "halal_description"
        private const val ARG_STATUS_LOGO_URL = "status_logo_url"
        private const val ARG_NUTRISCORE = "nutriscore"
        private const val ARG_NUTRISCORE_LABEL_DESCRIPTION = "nutriscore_label_description"
        private const val ARG_DISPLAY_TIMESTAMP = "display_timestamp"
        private const val ARG_ENERGY = "energy"
        private const val ARG_TOTAL_CARBOHYDRATE = "total"
        private const val ARG_SUGAR = "sugar"
        private const val ARG_TOTAL_FAT = "total_fat"
        private const val ARG_SATURATED_FAT = "saturated_fat"
        private const val ARG_TRANS_FAT = "trans_fat"
        private const val ARG_PROTEIN = "protein"
        private const val ARG_SODIUM = "sodium"
        private const val ARG_CONFIDENCE = "confidence"
        //private const val ARG_NUTRITION_FACT_IMAGE = "nutrition_fact_image"
        private const val ARG_TOTAL_FAT_SUMMARY = "total_fat_summary"
        private const val ARG_SAT_FAT_SUMMARY = "sat_fat_summary"
        private const val ARG_SUGAR_SUMMARY = "sugar_summary"
        private const val ARG_SALT_SUMMARY = "salt_summary"
        private const val ARG_TOTAL_FAT_STATUS_URL = "total_fat_status_url"
        private const val ARG_SAT_FAT_STATUS_URL = "sat_fat_status_url"
        private const val ARG_SUGAR_STATUS_URL = "sugar_status_url"
        private const val ARG_SALT_STATUS_URL = "salt_status_url"

        @JvmStatic
        fun newInstance(
            imageUri: String,
            barcode: String,
            barcodeUrl: String,
            nutritionProfilingClass: String,
            halalDescription: String,
            statusLogoUrl: String,
            nutriScore: String,
            nutriScoreLabelDescription: String,
            productName: String,
            brandName: String,
            displayTimestamp: String,
            energy: String,
            total: String,
            sugar: String,
            totalFat: String,
            saturatedFat: String,
            transFat: String,
            protein: String,
            sodium: String,
            confidence: String,
            //nutritionFactImage: String,
            totalFatSummary: String,
            satFatSummary: String,
            sugarSummary: String,
            saltSummary: String,
            totalFatStatusUrl: String,
            satFatStatusUrl: String,
            sugarStatusUrl: String,
            saltStatusUrl: String
        ) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                    putString(ARG_PRODUCT_NAME, productName)
                    putString(ARG_BRAND_NAME, brandName)
                    putString(ARG_BARCODE, barcode)
                    putString(ARG_BARCODE_URL, barcodeUrl)
                    putString(ARG_NUTRITION_PROFILING_CLASS, nutritionProfilingClass)
                    putString(ARG_HALAL_DESCRIPTION, halalDescription)
                    putString(ARG_STATUS_LOGO_URL, statusLogoUrl)
                    putString(ARG_NUTRISCORE, nutriScore)
                    putString(ARG_NUTRISCORE_LABEL_DESCRIPTION, nutriScoreLabelDescription)
                    putString(ARG_DISPLAY_TIMESTAMP, displayTimestamp)
                    putString(ARG_ENERGY, energy)
                    putString(ARG_TOTAL_CARBOHYDRATE, total)
                    putString(ARG_SUGAR, sugar)
                    putString(ARG_TOTAL_FAT, totalFat)
                    putString(ARG_SATURATED_FAT, saturatedFat)
                    putString(ARG_TRANS_FAT, transFat)
                    putString(ARG_PROTEIN, protein)
                    putString(ARG_SODIUM, sodium)
                    putString(ARG_CONFIDENCE, confidence)
                    //putString(ARG_NUTRITION_FACT_IMAGE, nutritionFactImage)
                    putString(ARG_TOTAL_FAT_SUMMARY, totalFatSummary)
                    putString(ARG_SAT_FAT_SUMMARY, satFatSummary)
                    putString(ARG_SUGAR_SUMMARY, sugarSummary)
                    putString(ARG_SALT_SUMMARY, saltSummary)
                    putString(ARG_TOTAL_FAT_STATUS_URL, totalFatStatusUrl)
                    putString(ARG_SAT_FAT_STATUS_URL, satFatStatusUrl)
                    putString(ARG_SUGAR_STATUS_URL, sugarStatusUrl)
                    putString(ARG_SALT_STATUS_URL, saltStatusUrl)
                }
            }
    }
}
