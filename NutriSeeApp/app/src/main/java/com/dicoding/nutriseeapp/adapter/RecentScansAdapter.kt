package com.dicoding.nutriseeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.model.Data

class RecentScansAdapter(
    private val items: List<Data>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecentScansAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Data)
        fun onDeleteClick(item: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_recent_scan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val productNameTextView: TextView = view.findViewById(R.id.tvProductName)
        private val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
        private val productImageView: ImageView = view.findViewById(R.id.ivProductImage)
        private val nutriScoreImageView: ImageView = view.findViewById(R.id.ivNutriScore)
        private val deleteImageView: ImageView = view.findViewById(R.id.ivDelete)

        fun bind(item: Data, clickListener: OnItemClickListener) {
            productNameTextView.text = item.productName
            timestampTextView.text = item.displayTimestamp
            Glide.with(itemView.context)
                .load(item.productImage)
                .into(productImageView)

            Glide.with(itemView.context)
                .load(item.nutriScore)
                .into(nutriScoreImageView)

            itemView.setOnClickListener {
                clickListener.onItemClick(item)
            }

            deleteImageView.setOnClickListener {
                clickListener.onDeleteClick(item)
            }
        }
    }
}
