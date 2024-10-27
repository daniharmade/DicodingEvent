package com.example.dicodingevent.ui.finished

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinishedViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val allEvents = mutableListOf<ListEventsItem>()

    private companion object {
        const val TAG = "FinishedViewModel"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.listEvents?.let { events ->
                        val finishedEvents = events.filter { event ->
                            LocalDateTime.now().isAfter(LocalDateTime.parse(event.endTime, DateTimeFormatter.ofPattern(DATE_FORMAT)))
                        }
                        allEvents.clear()
                        allEvents.addAll(finishedEvents)
                        _listEvents.value = finishedEvents
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchEvents(query: String) {
        _isLoading.value = true  // Set loading to true when search starts

        // Perform the search operation
        if (query.isEmpty()) {
            _listEvents.value = allEvents
        } else {
            _listEvents.value = allEvents.filter { event ->
                event.name.contains(query, ignoreCase = true) || event.description.contains(query, ignoreCase = true)
            }
        }

        // Delay before setting loading to false for better UX
        Handler(Looper.getMainLooper()).postDelayed({
            _isLoading.value = false  // Set loading to false after delay
        }, 500) // 500 milliseconds delay
    }


}
