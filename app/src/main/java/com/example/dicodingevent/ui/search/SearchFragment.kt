package com.example.dicodingevent.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.R
import com.example.dicodingevent.adapter.SearchAdapter
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentSearchBinding

@SuppressLint("SetTextI18n")
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private val adapter by lazy { SearchAdapter { navigateToDetail(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSearchBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearchResult.layoutManager = LinearLayoutManager(context)
            rvSearchResult.adapter = adapter

            searchBarEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = performSearch(query)
                override fun onQueryTextChange(newText: String) = performSearch(newText)
            })

            searchViewModel.apply {
                isLoading.observe(viewLifecycleOwner, ::showLoading)
                listEvents.observe(viewLifecycleOwner, ::handleEventList)
            }
        }
    }

    private fun performSearch(query: String): Boolean {
        searchViewModel.searchEvent(query)
        return true
    }

    private fun handleEventList(events: List<ListEventsItem>?) {
        with(binding) {
            if (events.isNullOrEmpty()) {
                rvSearchResult.visibility = View.INVISIBLE
                tvResultEmpty.visibility = View.VISIBLE
                tvSearchResultTitle.visibility = View.INVISIBLE
                cvSearchResult.visibility = View.INVISIBLE
            } else {
                setEventData(events)
                rvSearchResult.visibility = View.VISIBLE
                tvResultEmpty.visibility = View.GONE
                tvSearchResultTitle.visibility = View.VISIBLE
                cvSearchResult.visibility = View.VISIBLE
            }
        }
    }

    private fun setEventData(events: List<ListEventsItem>) {
        adapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToDetail(selectedEvent: ListEventsItem) {
        val action = SearchFragmentDirections.actionNavigationSearchToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}