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
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    private lateinit var adapter: EventAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(binding.root.context)

        adapter = EventAdapter { selectedEvent ->
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }

        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { consumerEvents ->
            setEventData(consumerEvents)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setEventData(consumerEvents: List<ListEventsItem>) {
        adapter.submitList(consumerEvents)
        binding.rvUpcomingEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.upcomingLoading.visibility = View.VISIBLE
        } else {
            binding.upcomingLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}