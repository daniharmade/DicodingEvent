package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(): Call<EventResponse>

    @GET("detail/{id}")
    fun getRestaurant(
        @Path("id") id: String
    ): Call<EventResponse>

    @GET("events")
    fun searchEvents(
        @Query("q") keyword: String,
        @Query("active") active: Int = -1
    ): Call<EventResponse>
}