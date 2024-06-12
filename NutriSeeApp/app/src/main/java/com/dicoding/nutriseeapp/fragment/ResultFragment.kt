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
    private lateinit var confidenceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        productImageView = view.findViewById(R.id.ivProductImage)
        productNameTextView = view.findViewById(R.id.tvProductName)
        energyTextView = view.findViewById(R.id.tvEnergy)
        sugarTextView = view.findViewById(R.id.tvSugar)
        confidenceTextView = view.findViewById(R.id.tvConfidence)

        arguments?.let {
            val imageUri = it.getString(ARG_IMAGE_URI)
            val productName = it.getString(ARG_PRODUCT_NAME)
            val energy = it.getString(ARG_ENERGY)
            val sugar = it.getString(ARG_SUGAR)
            val confidence = it.getString(ARG_CONFIDENCE)

            Glide.with(this)
                .load(imageUri)
                .fitCenter() // Ensure the image is scaled properly
                .into(productImageView)

            productNameTextView.text = productName
            energyTextView.text = energy
            sugarTextView.text = sugar
            confidenceTextView.text = confidence
        }

        return view
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_ENERGY = "energy"
        private const val ARG_SUGAR = "sugar"
        private const val ARG_CONFIDENCE = "confidence"

        @JvmStatic
        fun newInstance(
            imageUri: String,
            productName: String,
            energy: String,
            sugar: String,
            confidence: String
        ) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                    putString(ARG_PRODUCT_NAME, productName)
                    putString(ARG_ENERGY, energy)
                    putString(ARG_SUGAR, sugar)
                    putString(ARG_CONFIDENCE, confidence)
                }
            }
    }
}
