package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { selectedEvent ->
            val action = FinishedFragmentDirections.actionNavigationFinishedToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FinishedFragment.adapter
        }
    }

    private fun observeViewModel() {
        finishedViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
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