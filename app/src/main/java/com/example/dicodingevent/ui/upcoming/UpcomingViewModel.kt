package com.example.dicodingevent.ui.upcoming

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

class UpcomingViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents : LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "UpcomingViewModel"
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

                    val upComingEvents = response.body()?.listEvents?.filter { event ->
                        val endDateTime = LocalDateTime.parse(event.endTime, formatter)
                        currentTime.isBefore(endDateTime)
                    }

                    _listEvents.value = upComingEvents!!
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