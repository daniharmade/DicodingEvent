package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("events")
    fun getEvents(): Call<EventResponse>

    @GET("detail/{id}")
    fun getRestaurant(
        @Path("id") id: String
    ): Call<EventResponse>
}