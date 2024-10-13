package com.example.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingevent.adapter.EventAdapter
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val finishedViewModel by viewModels<FinishedViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFinishedEvent.layoutManager = GridLayoutManager(binding.root.context, 2)

        adapter = EventAdapter { selectedEvent ->
            val action = FinishedFragmentDirections.actionNavigationFinishedToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }

        finishedViewModel.listEvents.observe(viewLifecycleOwner) { consumerEvents ->
            setEventData(consumerEvents)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }


    private fun setEventData(consumerEvents: List<ListEventsItem>) {
        adapter.submitList(consumerEvents)
        binding.rvFinishedEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.finishedLoading.visibility = View.VISIBLE
        } else {
            binding.finishedLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}