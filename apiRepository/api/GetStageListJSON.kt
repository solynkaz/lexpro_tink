package com.example.lexpro_mobile.apiRepository.api

import com.example.lexpro_mobile.jsonDTO.responseJSON.StageListJSON
import retrofit2.http.GET


interface GetStageListJSON {
    @GET("stage_list")
    suspend fun getStageList(): List<StageListJSON>
}
