package com.dicoding.nutriseeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.model.Article

class NewsAdapter(
    private val articleList: List<Article>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = articleList[position]
        holder.bind(news, itemClickListener)
    }

    override fun getItemCount(): Int = articleList.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val newsImageView: ImageView = itemView.findViewById(R.id.iv_news)
        private val newsTitleTextView: TextView = itemView.findViewById(R.id.tv_title)
        private val newsAuthorTextView: TextView = itemView.findViewById(R.id.tv_author)
        private val newsTimeTextView: TextView = itemView.findViewById(R.id.tv_time)

        fun bind(article: Article, clickListener: OnItemClickListener) {
            newsTitleTextView.text = article.title
            newsAuthorTextView.text = article.author
            newsTimeTextView.text = article.publishedAt
            Glide.with(itemView.context).load(article.urlToImage).into(newsImageView)
            itemView.setOnClickListener {
                clickListener.onItemClick(article)
            }
        }
    }
}
