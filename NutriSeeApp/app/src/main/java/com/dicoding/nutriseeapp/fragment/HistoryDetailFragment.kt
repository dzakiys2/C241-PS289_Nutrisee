package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.model.Data
import com.dicoding.nutriseeapp.model.UserUploadData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetailFragment : Fragment() {

    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var brandNameTextView: TextView
    private lateinit var timestampTextView: TextView
    private lateinit var confidenceTextView: TextView
    private lateinit var barcodeTextView: TextView
    private lateinit var barcodeImageView: ImageView
    private lateinit var halalImageView: ImageView
    private lateinit var halalDescriptionTextView: TextView
    private lateinit var nutriScoreImageView: ImageView
    private lateinit var nutriScoreProfilingTextView: TextView
    private lateinit var nutriScoreLabelTextView: TextView
    private lateinit var totalFatSummaryTextView: TextView
    private lateinit var satFatSummaryTextView: TextView
    private lateinit var sugarSummaryTextView: TextView
    private lateinit var saltSummaryTextView: TextView
    private lateinit var energyTextView: TextView
    private lateinit var totalCarbohydrateTextView: TextView
    private lateinit var sugarTextView: TextView
    private lateinit var totalFatTextView: TextView
    private lateinit var saturatedFatTextView: TextView
    private lateinit var transFatTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var sodiumTextView: TextView
    private lateinit var nutritionFactImageView: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var totalFatStatusImageView: ImageView
    private lateinit var satFatStatusImageView: ImageView
    private lateinit var sugarStatusImageView: ImageView
    private lateinit var saltStatusImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_detail, container, false)

        productImageView = view.findViewById(R.id.ivProductImage)
        productNameTextView = view.findViewById(R.id.tvProductName)
        brandNameTextView = view.findViewById(R.id.tvBrandName)
        timestampTextView = view.findViewById(R.id.tvTimeStamp)
        confidenceTextView = view.findViewById(R.id.tvConfidence)
        barcodeTextView = view.findViewById(R.id.tvBarcode)
        barcodeImageView = view.findViewById(R.id.ivBarcode)
        halalImageView = view.findViewById(R.id.ivHalalImage)
        halalDescriptionTextView = view.findViewById(R.id.tvHalalImage)
        nutriScoreImageView = view.findViewById(R.id.ivNutriScore)
        nutriScoreProfilingTextView = view.findViewById(R.id.tvNutriScoreprofiling)
        nutriScoreLabelTextView = view.findViewById(R.id.tvNutriScorelabel)
        totalFatSummaryTextView = view.findViewById(R.id.tvTotalfatSummary)
        satFatSummaryTextView = view.findViewById(R.id.tvSatfatSummary)
        sugarSummaryTextView = view.findViewById(R.id.tvSugarSummary)
        saltSummaryTextView = view.findViewById(R.id.tvSaltSummary)
        energyTextView = view.findViewById(R.id.tvEnergy)
        totalCarbohydrateTextView = view.findViewById(R.id.tvTotalCarbohydrate)
        sugarTextView = view.findViewById(R.id.tvSugar)
        totalFatTextView = view.findViewById(R.id.tvTotalFat)
        saturatedFatTextView = view.findViewById(R.id.tvSaturatedFat)
        transFatTextView = view.findViewById(R.id.tvTransFat)
        proteinTextView = view.findViewById(R.id.tvProtein)
        sodiumTextView = view.findViewById(R.id.tvSodium)
//        nutritionFactImageView = view.findViewById(R.id.ivNutritionFactImage)
        backButton = view.findViewById(R.id.btn_back)
        totalFatStatusImageView = view.findViewById(R.id.ivStatustotalFat)
        satFatStatusImageView = view.findViewById(R.id.ivStatusSatFat)
        sugarStatusImageView = view.findViewById(R.id.ivStatusSugar)
        saltStatusImageView = view.findViewById(R.id.ivStatusSalt)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }

        val historyId = arguments?.getString(ARG_HISTORY_ID)
        historyId?.let {
            fetchHistoryDetail(it)
        }

        return view
    }

    private fun fetchHistoryDetail(historyId: String) {
        ApiClient.apiService.getHistoryDetail(historyId).enqueue(object : Callback<UserUploadData> {
            override fun onResponse(call: Call<UserUploadData>, response: Response<UserUploadData>) {
                if (response.isSuccessful) {
                    val historyDetail = response.body()?.data
                    historyDetail?.let {
                        updateUI(it)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch detail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserUploadData>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: Data) {
        Glide.with(this).load(data.productImage).fitCenter().into(productImageView)
        productNameTextView.text = data.productName
        brandNameTextView.text = data.brandName
        timestampTextView.text = data.displayTimestamp
        confidenceTextView.text = data.confidence
        barcodeTextView.text = data.barcode
        Glide.with(this).load(data.barcodeUrl).fitCenter().into(barcodeImageView)
        Glide.with(this).load(data.statusLogoUrl).fitCenter().into(halalImageView)
        halalDescriptionTextView.text = data.halalDescription
        Glide.with(this).load(data.nutriScore).fitCenter().into(nutriScoreImageView)
        nutriScoreProfilingTextView.text = data.nutritionProfilingClass
        nutriScoreLabelTextView.text = data.nutriScoreLabelDescription
        totalFatSummaryTextView.text = data.summary.totalFatSummary
        satFatSummaryTextView.text = data.summary.satFatSummary
        sugarSummaryTextView.text = data.summary.sugarSummary
        saltSummaryTextView.text = data.summary.saltSummary
        energyTextView.text = data.nutrition.energy
        totalCarbohydrateTextView.text = data.nutrition.carbohydrate.total
        sugarTextView.text = data.nutrition.carbohydrate.sugar
        totalFatTextView.text = data.nutrition.fat.total
        saturatedFatTextView.text = data.nutrition.fat.saturated
        transFatTextView.text = data.nutrition.fat.trans
        proteinTextView.text = data.nutrition.protein
        sodiumTextView.text = data.nutrition.sodium
       // Glide.with(this).load(data.nutritionFactImage).into(nutritionFactImageView)
        Glide.with(this)
            .load(data.summary.totalFatStatusUrl)
            .fitCenter()
            .into(totalFatStatusImageView)

        Glide.with(this)
            .load(data.summary.satFatStatusUrl)
            .fitCenter()
            .into(satFatStatusImageView)

        Glide.with(this)
            .load(data.summary.sugarStatusUrl)
            .fitCenter()
            .into(sugarStatusImageView)

        Glide.with(this)
            .load(data.summary.saltStatusUrl)
            .fitCenter()
            .into(saltStatusImageView)
    }

    companion object {
        private const val ARG_HISTORY_ID = "history_id"

        @JvmStatic
        fun newInstance(historyId: String) =
            HistoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HISTORY_ID, historyId)
                }
            }
    }
}
