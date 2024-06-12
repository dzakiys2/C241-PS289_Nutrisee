package com.dicoding.nutriseeapp.fragment
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nutriseeapp.BuildConfig
import com.dicoding.nutriseeapp.adapter.NewsAdapter
import com.dicoding.nutriseeapp.api.ApiService
import com.dicoding.nutriseeapp.databinding.FragmentHomeBinding
import com.dicoding.nutriseeapp.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsApiService: ApiService

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
        newsAdapter = NewsAdapter(emptyList())
        binding.recyclerView.adapter = newsAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsApiService = retrofit.create(ApiService::class.java)
        fetchNews(newsApiService)
        fetchUserName()
    }

    private fun fetchNews(newsApiService: ApiService) {
        val apiKey = BuildConfig.NEWS_API_KEY
        val query = "Diabetes OR Nutrition"
        val call = newsApiService.getNews(query, apiKey)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    newsAdapter = NewsAdapter(articles)
                    binding.recyclerView.adapter = newsAdapter

                    Log.d("HomeFragment", "Number of articles received: ${articles.size}")
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = user.displayName
            binding.userNameTextView.text = "Hey, Welcome Back to NutriSee, $name"
        }
    }
}



