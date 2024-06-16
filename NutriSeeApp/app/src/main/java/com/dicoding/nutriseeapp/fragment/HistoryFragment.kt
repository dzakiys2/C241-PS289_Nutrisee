package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.databinding.FragmentHistoryBinding
import com.dicoding.nutriseeapp.model.HistoryItem
import com.dicoding.nutriseeapp.model.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        fetchHistory()
    }

    private fun fetchHistory() {
        ApiClient.apiService.getHistory().enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    val historyMap = response.body()?.data ?: emptyMap()
                    val historyItems = historyMap.values.toList()
                    setupRecyclerView(historyItems)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(historyItems: List<HistoryItem>) {
        binding.recyclerViewHistory.adapter = object : RecyclerView.Adapter<HistoryViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
                return HistoryViewHolder(view)
            }

            override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
                val historyItem = historyItems[position]
                holder.productNameTextView.text = historyItem.namaProduk
                holder.timestampTextView.text = historyItem.displayTimestamp
                Glide.with(holder.itemView.context)
                    .load(historyItem.productImage)
                    .into(holder.productImageView)

                Glide.with(holder.itemView.context)
                    .load(historyItem.nutriscore)
                    .into(holder.nutriScoreImageView)
            }

            override fun getItemCount() = historyItems.size
        }
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImageView: ImageView = view.findViewById(R.id.ivProductImage)
        val productNameTextView: TextView = view.findViewById(R.id.tvProductName)
        val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
        val nutriScoreImageView: ImageView = view.findViewById(R.id.ivNutriScore)
    }
}
