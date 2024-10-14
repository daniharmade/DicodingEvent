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
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    private val adapter by lazy {
        EventAdapter { selectedEvent -> navigateToDetail(selectedEvent) }
    }

    private val carouselAdapter by lazy {
        CarouselAdapter { selectedEvent -> navigateToDetail(selectedEvent) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvHomeUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvHomeFinished.layoutManager = LinearLayoutManager(context)
            rvHomeUpcoming.adapter = carouselAdapter
            rvHomeFinished.adapter = adapter
        }

        homeViewModel.apply {
            listUpcomingEvents.observe(viewLifecycleOwner) { carouselAdapter.submitList(it) }
            listFinishedEvents.observe(viewLifecycleOwner) { adapter.submitList(it) }
            isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        }
    }

    private fun navigateToDetail(selectedEvent: ListEventsItem) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.homeLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}