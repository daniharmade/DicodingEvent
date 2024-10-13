package com.example.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.FragmentDetailEventBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("DEPRECATION")
class DetailEventFragment : Fragment() {
    private val args: DetailEventFragmentArgs by navArgs()
    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!

    private val detailEventViewModel: DetailEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetTextI18n") // Menyuppress peringatan tentang penggunaan metode deprecated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventData = args.selectedEvent
        detailEventViewModel.setEvent(eventData)

        detailEventViewModel.event.observe(viewLifecycleOwner, Observer{ event ->
            // Menampilkan nama acara
            binding.tvDetailName.text = event.name

            // Menampilkan nama penyelenggara
            binding.tvDetailOrganizer.text = "Oleh: ${event.ownerName}"

            // Menampilkan sisa kuota
            binding.tvDetailQuota.text = "Sisa Kuota: ${event.quota - event.registrants}"

            // Menampilkan kategori acara
            binding.tvDetailType.text = event.category

            // Membersihkan deskripsi acara dari karakter escape
            val cleanedDescription = event.description
                .replace("\\u003C", "<")
                .replace("\\u003E", ">")

            // Menampilkan deskripsi acara dalam format HTML
            binding.tvDetailDescription.text = Html.fromHtml(cleanedDescription, Html.FROM_HTML_MODE_COMPACT)

            // Formatter untuk parsing dan formatting tanggal
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("id", "ID"))

            // Parsing waktu mulai dan selesai acara
            val beginDateTime = LocalDateTime.parse(event.beginTime, inputFormatter)
            val endDateTime = LocalDateTime.parse(event.endTime, inputFormatter)

            // Memformat tanggal dan waktu
            val formattedDate = beginDateTime.format(outputFormatter)
            val formattedEndTime = endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            // Menampilkan waktu acara
            binding.tvDetailTime.text = "$formattedDate - $formattedEndTime WIB"

            // Menggunakan Glide untuk memuat gambar acara
            Glide.with(requireContext())
                .load(event.imageLogo)
                .into(binding.imgDetailPhoto)

            // Button Pendaftaran
            binding.btnRegister.setOnClickListener {
                // Membuka link acara di browser
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(browserIntent)
            }
        })
    }

    @Deprecated("Deprecated in Java") // Menyuppress peringatan tentang metode yang sudah deprecated
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}