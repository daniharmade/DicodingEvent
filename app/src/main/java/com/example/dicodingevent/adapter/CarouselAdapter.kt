package com.example.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.CarouselViewEventBinding

class CarouselAdapter (private val onItemClicked: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, CarouselAdapter.MyViewHolder>(
    DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CarouselViewEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: CarouselViewEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)

        holder.binding.tvCarouselItemName.text = event.name
        Glide.with(holder.binding.root.context)
            .load(event.imageLogo)
            .into(holder.binding.imgCarouselItemPhoto)

        holder.binding.root.setOnClickListener {
            onItemClicked(event)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}