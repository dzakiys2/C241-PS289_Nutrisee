package com.dicoding.nutriseeapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.model.UserUploadStory
import com.dicoding.nutriseeapp.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadFragment : Fragment() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnUpload: Button
    private lateinit var sessionManager: SessionManager
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)

        imgPreview = view.findViewById(R.id.imgPreview)
        btnUpload = view.findViewById(R.id.btnUpload)

        sessionManager = SessionManager(requireContext())

        arguments?.getString(ARG_IMAGE_URI)?.let {
            imageUri = Uri.parse(it)
            imgPreview.setImageURI(imageUri)
        }

        btnUpload.setOnClickListener {
            val token = sessionManager.getUserToken()
            if (token != null) {
                showLoadingFragment()  // Show loading fragment
                uploadImage(imageUri, token)
            } else {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun uploadImage(imageUri: Uri, token: String) {
        val file = File(imageUri.path!!)

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        ApiClient.apiService.uploadImage("Bearer $token", body).enqueue(object : Callback<UserUploadStory> {
            override fun onResponse(call: Call<UserUploadStory>, response: Response<UserUploadStory>) {
                hideLoadingFragment()  // Hide loading fragment
                if (response.isSuccessful) {
                    val uploadStory = response.body()
                    if (uploadStory != null && !uploadStory.error) {
                        Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show()
                        // Navigate to ResultFragment
                        val data = uploadStory.data
                        if (data != null) {
                            navigateToResultFragment(
                                data.productImage,
                                data.barcode,
                                data.barcodeUrl,
                                data.nutritionProfilingClass,
                                data.halalDescription,
                                data.statusLogoUrl,
                                data.nutriScore,
                                data.nutriScoreLabelDescription,
                                data.productName,
                                data.displayTimestamp,
                                data.nutrition.energy,
                                data.nutrition.carbohydrate.sugar,
                                data.nutrition.fat.total,
                                data.nutrition.fat.saturated,
                                data.nutrition.fat.trans,
                                data.nutrition.protein,
                                data.nutrition.sodium,
                                data.confidence,
                                data.nutritionFactImage,
                                data.summary.totalFatSummary,
                                data.summary.satFatSummary,
                                data.summary.sugarSummary,
                                data.summary.saltSummary,
                                data.summary.totalFatStatusUrl,
                                data.summary.satFatStatusUrl,
                                data.summary.sugarStatusUrl,
                                data.summary.saltStatusUrl
                            )
                        }
                    } else {
                        Toast.makeText(requireContext(), "Upload failed: ${uploadStory?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Upload failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserUploadStory>, t: Throwable) {
                hideLoadingFragment()  // Hide loading fragment
                Toast.makeText(requireContext(), "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoadingFragment() {
        parentFragmentManager.commit {
            replace(R.id.frame_layout, LoadingFragment())
            addToBackStack(null)
        }
    }

    private fun hideLoadingFragment() {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToResultFragment(
        imageUri: String,
        barcode: String,
        barcodeUrl: String,
        nutritionProfilingClass: String,
        halalDescription: String,
        statusLogoUrl: String,
        nutriScore: String,
        nutriScoreLabelDescription: String,
        productName: String,
        displayTimestamp: String,
        energy: String,
        sugar: String,
        totalFat: String,
        saturatedFat: String,
        transFat: String,
        protein: String,
        sodium: String,
        confidence: String,
        nutritionFactImage: String,
        totalFatSummary: String,
        satFatSummary: String,
        sugarSummary: String,
        saltSummary: String,
        totalFatStatusUrl: String,
        satFatStatusUrl: String,
        sugarStatusUrl: String,
        saltStatusUrl: String
    ) {
        val fragment = ResultFragment.newInstance(
            imageUri, barcode, barcodeUrl, nutritionProfilingClass, halalDescription, statusLogoUrl, nutriScore, nutriScoreLabelDescription, productName,
            displayTimestamp, energy, sugar, totalFat, saturatedFat, transFat, protein, sodium, confidence, nutritionFactImage, totalFatSummary, satFatSummary, sugarSummary, saltSummary, totalFatStatusUrl, satFatStatusUrl, sugarStatusUrl, saltStatusUrl
        )
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        private const val ARG_IMAGE_URI = "image_uri"

        @JvmStatic
        fun newInstance(imageUri: String) =
            UploadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URI, imageUri)
                }
            }
    }
}
