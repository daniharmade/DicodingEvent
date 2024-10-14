package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.EventAdapter
import com.example.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel by viewModels<UpcomingViewModel>()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentUpcomingBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { selectedEvent ->
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UpcomingFragment.adapter
        }
    }

    private fun setupObserver() {
        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { adapter.submitList(it) }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { binding.upcomingLoading.visibility = if (it) View.VISIBLE else View.GONE }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}