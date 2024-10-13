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

class EventAdapter(private val onItemClicked: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemViewEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ItemViewEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val event = getItem(position)

        val currentTime = LocalDateTime.now()

        // Membuat formatter untuk memformat/menguraikan tanggal dengan pola atau format "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Mengubah (parse) string beginTime dari event menjadi objek LocalDateTime menggunakan formatter
        val beginDateTime = LocalDateTime.parse(event.beginTime, formatter)

        // Mengubah (parse) string endTime dari event menjadi objek LocalDateTime menggunakan formatter
        val endDateTime = LocalDateTime.parse(event.endTime, formatter)

        holder.binding.tvItemStatus.text = when{
            // Jika waktu saat ini setelah endDateTime, tampilkan "Selesai"
            currentTime.isAfter(endDateTime) -> "Selesai"
            currentTime.isBefore(beginDateTime) -> {

                // Jika selisih waktu lebih dari 24 jam, tampilkan dalam hitungan hari
                val timeUntilStart = java.time.Duration.between(currentTime, beginDateTime)

                // Jika selisih waktu kurang dari atau sama dengan 24 jam, tampilkan dalam hitungan jam
                if (timeUntilStart.toHours() <= 24){
                    "${timeUntilStart.toHours()} Jam Lagi"

                } else {
                    // Jika selisih waktu lebih dari 24 jam, tampilkan dalam hitungan hari
                    "${timeUntilStart.toDays()} Hari Lagi"
                }
            }

            // Jika waktu saat ini antara beginDateTime dan endDateTime, tampilkan "Sedang Berlangsung"
            currentTime.isAfter(beginDateTime) && currentTime.isBefore(endDateTime) -> "Sedang Berlangsung"
            // Jika tidak memenuhi kondisi di atas, tampilkan "Dibatalkan"
            else -> "Dibatalkan"
        }

        holder.binding.tvItemName.text = event.name
        holder.binding.tvItemDescription.text = event.summary
        holder.binding.tvItemType.text = event.category
        Glide.with(holder.binding.root.context)
            .load(event.imageLogo)
            .into(holder.binding.imgItemPhoto)

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