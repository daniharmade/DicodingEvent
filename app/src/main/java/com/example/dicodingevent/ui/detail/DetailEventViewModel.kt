package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.ListEventsItem

class DetailEventViewModel : ViewModel() {

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> = _event

    fun setEvent(eventData: ListEventsItem) {
        _event.value = eventData
    }
}