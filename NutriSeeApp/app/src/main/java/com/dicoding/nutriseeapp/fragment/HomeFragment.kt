package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nutriseeapp.BuildConfig
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.adapter.NewsAdapter
import com.dicoding.nutriseeapp.adapter.RecentScansAdapter
import com.dicoding.nutriseeapp.api.ApiClient
import com.dicoding.nutriseeapp.api.ApiService
import com.dicoding.nutriseeapp.databinding.FragmentHomeBinding
import com.dicoding.nutriseeapp.model.Article
import com.dicoding.nutriseeapp.model.HistoryResponse
import com.dicoding.nutriseeapp.model.NewsResponse
import com.dicoding.nutriseeapp.model.Data
import com.dicoding.nutriseeapp.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), NewsAdapter.OnItemClickListener, RecentScansAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recentScansAdapter: RecentScansAdapter
    private lateinit var newsApiService: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewScan.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        sessionManager = SessionManager(requireContext())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsApiService = retrofit.create(ApiService::class.java)
        fetchNews(newsApiService)
        fetchUserName()
        fetchRecentScans()

        binding.scanNowButton.setOnClickListener {
            navigateToCameraFragment()
        }
        binding.ButtonSeeAll.setOnClickListener {
            navigateToHistoryFragment()
        }
    }

    private fun fetchNews(newsApiService: ApiService) {
        val apiKey = BuildConfig.NEWS_API_KEY
        val query = "Diabetes OR Nutrition"
        val call = newsApiService.getNews(query, apiKey)

        // Show Lottie animation for loading news
        binding.lottieAnimationViewArticles.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                // Hide Lottie animation
                binding.lottieAnimationViewArticles.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    newsAdapter = NewsAdapter(articles, this@HomeFragment)
                    binding.recyclerView.adapter = newsAdapter
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                binding.lottieAnimationViewArticles.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = user.displayName
            binding.userNameTextView.text = "$name"
        }
    }

    private fun fetchRecentScans() {
        binding.lottieAnimationViewRecentScan.visibility = View.VISIBLE
        binding.recyclerViewScan.visibility = View.VISIBLE
        ApiClient.apiService.getHistory().enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {

                binding.lottieAnimationViewRecentScan.visibility = View.GONE
                binding.recyclerViewScan.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val historyItems = response.body()?.data?.values?.toList() ?: emptyList()
                    recentScansAdapter = RecentScansAdapter(historyItems, this@HomeFragment)
                    binding.recyclerViewScan.adapter = recentScansAdapter
                    binding.tvProductsScanned.text = "${historyItems.size} Products Scanned"
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch recent scans", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                binding.lottieAnimationViewRecentScan.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteHistoryItem(item: Data) {
        ApiClient.apiService.deleteHistory(item.historyId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
                    fetchRecentScans()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToCameraFragment() {
        val fragment = CameraFragment()
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }
    private fun navigateToHistoryFragment() {
        val fragment = HistoryFragment()
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

    override fun onItemClick(article: Article) {
        val fragment = NewsDetailFragment.newInstance(article)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onItemClick(item: Data) {
        val fragment = HistoryDetailFragment.newInstance(item.historyId)
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

    override fun onDeleteClick(item: Data) {
        deleteHistoryItem(item)
    }
}
