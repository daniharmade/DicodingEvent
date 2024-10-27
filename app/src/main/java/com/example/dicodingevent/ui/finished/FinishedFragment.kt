package com.example.dicodingevent.ui.finished

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
import com.example.dicodingevent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel by viewModels<FinishedViewModel>()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
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
            val action = FinishedFragmentDirections.actionNavigationFinishedToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvFinishedEvent.layoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvent.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchBarEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { finishedViewModel.searchEvents(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        finishedViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            binding.cvSearchResult.visibility = if (events.isEmpty()) View.GONE else View.VISIBLE
            binding.tvResultEmpty.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(events)  // Update RecyclerView with new events
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.finishedLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
