package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R

class ResultFragment : Fragment() {

    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var energyTextView: TextView
    private lateinit var sugarTextView: TextView
//    private lateinit var fiberTextView: TextView
    private lateinit var totalFatTextView: TextView
    private lateinit var saturatedFatTextView: TextView
    private lateinit var transFatTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var sodiumTextView: TextView
    private lateinit var confidenceTextView: TextView
    private lateinit var nutritionFactImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        productImageView = view.findViewById(R.id.ivProductImage)
        productNameTextView = view.findViewById(R.id.tvProductName)
        energyTextView = view.findViewById(R.id.tvEnergy)
        sugarTextView = view.findViewById(R.id.tvSugar)
//        fiberTextView = view.findViewById(R.id.tvFiber)
        totalFatTextView = view.findViewById(R.id.tvTotalFat)
        saturatedFatTextView = view.findViewById(R.id.tvSaturatedFat)
        transFatTextView = view.findViewById(R.id.tvTransFat)
        proteinTextView = view.findViewById(R.id.tvProtein)
        sodiumTextView = view.findViewById(R.id.tvSodium)
        confidenceTextView = view.findViewById(R.id.tvConfidence)
        nutritionFactImageView = view.findViewById(R.id.ivNutritionFactImage)

        arguments?.let {
            val imageUri = it.getString(ARG_IMAGE_URI)
            val productName = it.getString(ARG_PRODUCT_NAME)
            val energy = it.getString(ARG_ENERGY)
            val sugar = it.getString(ARG_SUGAR)
//            val fiber = it.getString(ARG_FIBER)
            val totalFat = it.getString(ARG_TOTAL_FAT)
            val saturatedFat = it.getString(ARG_SATURATED_FAT)
            val transFat = it.getString(ARG_TRANS_FAT)
            val protein = it.getString(ARG_PROTEIN)
            val sodium = it.getString(ARG_SODIUM)
            val confidence = it.getString(ARG_CONFIDENCE)
            val nutritionFactImage = it.getString(ARG_NUTRITION_FACT_IMAGE)

            Glide.with(this)
                .load(imageUri)
                .fitCenter()
                .into(productImageView)

            Glide.with(this)
                .load(nutritionFactImage)
                .fitCenter()
                .into(nutritionFactImageView)

            productNameTextView.text = productName
            energyTextView.text = energy
            sugarTextView.text = sugar
//            fiberTextView.text = fiber
            totalFatTextView.text = totalFat
            saturatedFatTextView.text = saturatedFat
            transFatTextView.text = transFat
            proteinTextView.text = protein
            sodiumTextView.text = sodium
            confidenceTextView.text = confidence
        }

        return view
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_ENERGY = "energy"
        private const val ARG_SUGAR = "sugar"
//        private const val ARG_FIBER = "fiber"
        private const val ARG_TOTAL_FAT = "total_fat"
        private const val ARG_SATURATED_FAT = "saturated_fat"
        private const val ARG_TRANS_FAT = "trans_fat"
        private const val ARG_PROTEIN = "protein"
        private const val ARG_SODIUM = "sodium"
        private const val ARG_CONFIDENCE = "confidence"
        private const val ARG_NUTRITION_FACT_IMAGE = "nutrition_fact_image"

        @JvmStatic
        fun newInstance(
            imageUri: String,
            productName: String,
            energy: String,
            sugar: String,
//            fiber: String,
            totalFat: String,
            saturatedFat: String,
            transFat: String,
            protein: String,
            sodium: String,
            confidence: String,
            nutritionFactImage: String
        ) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                    putString(ARG_PRODUCT_NAME, productName)
                    putString(ARG_ENERGY, energy)
                    putString(ARG_SUGAR, sugar)
//                    putString(ARG_FIBER, fiber)
                    putString(ARG_TOTAL_FAT, totalFat)
                    putString(ARG_SATURATED_FAT, saturatedFat)
                    putString(ARG_TRANS_FAT, transFat)
                    putString(ARG_PROTEIN, protein)
                    putString(ARG_SODIUM, sodium)
                    putString(ARG_CONFIDENCE, confidence)
                    putString(ARG_NUTRITION_FACT_IMAGE, nutritionFactImage)
                }
            }
    }
}
