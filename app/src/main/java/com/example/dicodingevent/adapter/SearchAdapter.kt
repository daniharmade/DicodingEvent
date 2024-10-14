package com.example.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemViewEventBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchAdapter(private val onItemClicked: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, SearchAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClicked)
    }

    class MyViewHolder(private val binding: ItemViewEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClicked: (ListEventsItem) -> Unit) {
            binding.tvItemName.text = event.name
            binding.tvItemDescription.text = event.summary
            binding.tvItemType.text = event.category
            binding.tvItemStatus.text = getEventStatus(event)

            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.imgItemPhoto)

            binding.root.setOnClickListener { onItemClicked(event) }
        }

        private fun getEventStatus(event: ListEventsItem): String {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val beginDateTime = LocalDateTime.parse(event.beginTime, formatter)
            val endDateTime = LocalDateTime.parse(event.endTime, formatter)

            return when {
                currentTime.isAfter(endDateTime) -> "Completed"
                currentTime.isBefore(beginDateTime) -> {
                    val timeUntilStart = java.time.Duration.between(currentTime, beginDateTime)
                    if (timeUntilStart.toHours() <= 24) "${timeUntilStart.toHours()} Hour Left"
                    else "${timeUntilStart.toDays()} Day Left"
                }
                else -> "Ongoing"
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
        }
    }
}