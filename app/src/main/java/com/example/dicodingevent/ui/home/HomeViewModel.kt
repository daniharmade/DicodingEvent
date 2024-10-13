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
    val listUpcomingEvents : LiveData<List<ListEventsItem>> = _listUpcomingEvents

    private val _listFInishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents : LiveData<List<ListEventsItem>> = _listFInishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val currentTime = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                    val finishedEvents = response.body()?.listEvents?.filter { event ->
                        val endDateTime = LocalDateTime.parse(event.endTime, formatter)
                        currentTime.isAfter(endDateTime)
                    }?.take(5)

                    val upComingEvents = response.body()?.listEvents?.filter { event ->
                        val endDateTime = LocalDateTime.parse(event.endTime, formatter)
                        currentTime.isBefore(endDateTime)
                    }?.take(5)

                    _listUpcomingEvents.value = upComingEvents!!
                    _listFInishedEvents.value = finishedEvents!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}