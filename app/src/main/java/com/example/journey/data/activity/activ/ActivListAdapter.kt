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

class ActivListAdapter : ListAdapter<KakaoPlace, ActivListAdapter.ActivViewHolder>(ActivListDiffCallback()) {

    inner class ActivViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.activTitle)
        val address: TextView = view.findViewById(R.id.activAddress)
        val phone: TextView = view.findViewById(R.id.activPhone)
        val distance: TextView = view.findViewById(R.id.activDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activ_card, parent, false)
        return ActivViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivViewHolder, position: Int) {
        val item = getItem(position)

        holder.title.text = item.name
        holder.address.text = item.roadAddress.ifEmpty { item.addressName.ifEmpty { "주소 없음" } }
        holder.phone.text = item.phone.ifEmpty { "전화번호 없음" }
        holder.distance.text = if (item.distance.isNotEmpty()) "${item.distance}m" else "거리 정보 없음"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CafewebviewActivity::class.java)
            intent.putExtra("url", item.url)
            context.startActivity(intent)
        }
    }
}

class ActivListDiffCallback : DiffUtil.ItemCallback<KakaoPlace>() {
    override fun areItemsTheSame(oldItem: KakaoPlace, newItem: KakaoPlace): Boolean {
        return oldItem.name == newItem.name && oldItem.roadAddress == newItem.roadAddress
    }

    override fun areContentsTheSame(oldItem: KakaoPlace, newItem: KakaoPlace): Boolean {
        return oldItem == newItem
    }
}
