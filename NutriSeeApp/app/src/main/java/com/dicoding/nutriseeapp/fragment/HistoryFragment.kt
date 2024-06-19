package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.databinding.FragmentHistoryBinding
import com.dicoding.nutriseeapp.model.Data
import com.dicoding.nutriseeapp.model.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var lottieLoading: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        lottieLoading = binding.root.findViewById(R.id.lottieLoading)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        fetchHistory()
    }

    private fun fetchHistory() {
        lottieLoading.visibility = View.VISIBLE
        binding.recyclerViewHistory.visibility = View.GONE

        ApiClient.apiService.getHistory().enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                lottieLoading.visibility = View.GONE
                if (response.isSuccessful) {
                    val historyMap = response.body()?.data ?: emptyMap()
                    val historyItems = historyMap.values.toList()
                    setupRecyclerView(historyItems)
                    binding.recyclerViewHistory.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                lottieLoading.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(historyItems: List<Data>) {
        binding.recyclerViewHistory.adapter = object : RecyclerView.Adapter<HistoryViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
                return HistoryViewHolder(view)
            }

            override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
                val historyItem = historyItems[position]
                holder.productNameTextView.text = historyItem.productName
                holder.timestampTextView.text = historyItem.displayTimestamp
                Glide.with(holder.itemView.context)
                    .load(historyItem.productImage)
                    .into(holder.productImageView)

                Glide.with(holder.itemView.context)
                    .load(historyItem.nutriScore)
                    .into(holder.nutriScoreImageView)

                holder.itemView.setOnClickListener {
                    val fragment = HistoryDetailFragment.newInstance(historyItem.historyId)
                    parentFragmentManager.commit {
                        replace(R.id.frame_layout, fragment)
                        addToBackStack(null)
                    }
                }

                holder.deleteButton.setOnClickListener {
                    deleteHistory(historyItem.historyId)
                }
            }

            override fun getItemCount() = historyItems.size
        }
    }

    private fun deleteHistory(historyId: String) {
        ApiClient.apiService.deleteHistory(historyId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "History item deleted", Toast.LENGTH_SHORT).show()
                    fetchHistory() // Refresh the history list
                } else {
                    Toast.makeText(requireContext(), "Failed to delete history item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImageView: ImageView = view.findViewById(R.id.ivProductImage)
        val productNameTextView: TextView = view.findViewById(R.id.tvProductName)
        val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
        val nutriScoreImageView: ImageView = view.findViewById(R.id.ivNutriScore)
        val deleteButton: ImageView = view.findViewById(R.id.ivDelete)
    }
}
