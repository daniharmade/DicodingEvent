package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.ListEventsItem

class DetailEventViewModel : ViewModel() {

    // MutableLiveData untuk menyimpan data acara
    private val _event = MutableLiveData<ListEventsItem>()

    // LiveData untuk membagikan data acara
    val event: LiveData<ListEventsItem> = _event

    //  mengatur atau mengupdate data acara
    fun setEvent(eventData: ListEventsItem) {
        _event.value = eventData
    }
}