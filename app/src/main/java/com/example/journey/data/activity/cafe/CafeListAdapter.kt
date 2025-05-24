package com.example.journey.data.activity.cafe

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.journey.R
import com.example.journey.data.remote.model.cafe.KakaoPlace

class CafeListAdapter : ListAdapter<KakaoPlace, CafeListAdapter.CafeViewHolder>(CafeListDiffCallback()) {

    inner class CafeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.cafeTitle)
        val address: TextView = view.findViewById(R.id.cafeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cafe_card, parent, false)
        return CafeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.name
        holder.address.text = item.roadAddress

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CafewebviewActivity::class.java)
            intent.putExtra("url", item.url)
            context.startActivity(intent)
        }

    }
}

class CafeListDiffCallback : DiffUtil.ItemCallback<KakaoPlace>() {
    override fun areItemsTheSame(oldItem: KakaoPlace, newItem: KakaoPlace): Boolean {
        return oldItem.name == newItem.name && oldItem.roadAddress == newItem.roadAddress
    }

    override fun areContentsTheSame(oldItem: KakaoPlace, newItem: KakaoPlace): Boolean {
        return oldItem == newItem
    }
}
