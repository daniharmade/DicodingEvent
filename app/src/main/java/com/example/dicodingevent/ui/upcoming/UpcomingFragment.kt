package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { selectedEvent ->
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(context)
        binding.rvUpcomingEvent.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchBarEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { upcomingViewModel.searchEvents(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            binding.cvSearchResult.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
            binding.tvResultEmpty.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(events)  // Update RecyclerView with new events
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.upcomingLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
