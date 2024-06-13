package com.dicoding.nutriseeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.databinding.FragmentNewsDetailBinding
import com.dicoding.nutriseeapp.model.Article

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding

    companion object {
        private const val ARG_ARTICLE = "article"

        fun newInstance(article: Article): NewsDetailFragment {
            val fragment = NewsDetailFragment()
            val bundle = Bundle().apply {
                putParcelable(ARG_ARTICLE, article)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = arguments?.getParcelable<Article>(ARG_ARTICLE)
        article?.let {
            binding.newsTitleTextView.text = it.title
            binding.newsCategoryTextView.text = it.author
            binding.newsContentTextView.text = it.description
            Glide.with(this).load(it.urlToImage).into(binding.newsImageView)
        }
        val btnBack: ImageButton = view.findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
