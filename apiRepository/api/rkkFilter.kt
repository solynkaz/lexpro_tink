package com.example.lexpro_mobile.apiRepository.api

import com.example.lexpro_mobile.jsonDTO.RkkFilter
import retrofit2.http.*
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.rkkFilterJSON

interface rkkFilter {
    @POST("rkk/filter")
    suspend fun filterRkk(
        @Body rkkFilter: RkkFilter
    ) : rkkFilterJSON
}