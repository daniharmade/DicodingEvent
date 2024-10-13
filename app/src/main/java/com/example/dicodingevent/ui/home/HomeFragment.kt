package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.adapter.CarouselAdapter
import com.example.dicodingevent.adapter.EventAdapter
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: EventAdapter
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set layout manager for upcoming events (horizontal)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHomeUpcoming.layoutManager = layoutManager

        // Set layout manager for finished events (vertical, single column)
        binding.rvHomeFinished.layoutManager = LinearLayoutManager(context)

        // Initialize adapters with click listener
        adapter = EventAdapter { selectedEvent ->
            val action = HomeFragmentDirections.actionNavigationHomeToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }

        carouselAdapter = CarouselAdapter { selectedEvent ->
            val action = HomeFragmentDirections.actionNavigationHomeToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }

        // Observe ViewModel data
        homeViewModel.listUpcomingEvents.observe(viewLifecycleOwner) { consumerEvents ->
            setUpcomingEventData(consumerEvents)
        }

        homeViewModel.listFinishedEvents.observe(viewLifecycleOwner) { consumerEvents ->
            setFinishedEventData(consumerEvents)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setUpcomingEventData(consumerEvents: List<ListEventsItem>) {
        // Set data for upcoming events
        carouselAdapter.submitList(consumerEvents)
        binding.rvHomeUpcoming.adapter = carouselAdapter
    }

    private fun setFinishedEventData(consumerEvents: List<ListEventsItem>) {
        // Set data for finished events
        adapter.submitList(consumerEvents)
        binding.rvHomeFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        // Show or hide loading indicator
        if (isLoading) {
            binding.homeLoading.visibility = View.VISIBLE
        } else {
            binding.homeLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
