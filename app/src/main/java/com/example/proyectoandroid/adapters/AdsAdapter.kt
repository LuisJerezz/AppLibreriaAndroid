package com.example.proyectoandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.databinding.ItemAdBinding

class AdsAdapter(private val adsList: List<String>) : RecyclerView.Adapter<AdsAdapter.AdViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = ItemAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(adsList[position])
    }

    override fun getItemCount(): Int {
        return adsList.size
    }

    inner class AdViewHolder(private val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ad: String) {
            binding.tvAdTitle.text = ad
        }
    }
}