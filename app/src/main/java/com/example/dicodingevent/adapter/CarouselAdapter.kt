package com.example.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.CarouselViewEventBinding

class CarouselAdapter(private val onItemClicked: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, CarouselAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CarouselViewEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClicked)
    }

    class MyViewHolder(private val binding: CarouselViewEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem, onItemClicked: (ListEventsItem) -> Unit) {
            binding.tvCarouselItemName.text = event.name
            Glide.with(binding.root.context).load(event.imageLogo)
                .into(binding.imgCarouselItemPhoto)

            val remainingQuota = event.quota - event.registrants
            binding.tvDetailQuota.text = if (remainingQuota > 0) {
                "Available: $remainingQuota Slot"
            } else {
                "Full Slot"
            }

            binding.root.setOnClickListener { onItemClicked(event) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) =
                oldItem == newItem
        }
    }
}