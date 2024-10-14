package com.example.dicodingevent.ui.finished

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
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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
                    val finishedEvents = response.body()?.listEvents?.filter { event ->
                        LocalDateTime.now().isAfter(LocalDateTime.parse(event.endTime, DateTimeFormatter.ofPattern(DATE_FORMAT)))
                    }
                    _listEvents.value = finishedEvents ?: emptyList()
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
}