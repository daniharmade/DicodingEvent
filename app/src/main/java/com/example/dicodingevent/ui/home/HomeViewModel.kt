package com.example.dicodingevent.ui.home

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

class HomeViewModel : ViewModel() {

    private val _listUpcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val listUpcomingEvents: LiveData<List<ListEventsItem>> get() = _listUpcomingEvents

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents: LiveData<List<ListEventsItem>> get() = _listFinishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val currentTime get() = LocalDateTime.now()

    companion object {
        private const val TAG = "HomeViewModel"
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
                        _listFinishedEvents.value = events.filter { isFinished(it) }.take(5)
                        _listUpcomingEvents.value = events.filter { isUpcoming(it) }.take(5)
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

    private fun isFinished(event: ListEventsItem) = currentTime.isAfter(LocalDateTime.parse(event.endTime, formatter))
    private fun isUpcoming(event: ListEventsItem) = currentTime.isBefore(LocalDateTime.parse(event.endTime, formatter))
}