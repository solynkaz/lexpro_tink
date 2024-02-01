package com.example.lexpro_mobile.apiRepository.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PostLoginJSON {

    @FormUrlEncoded
    @POST("perform_login")
    suspend fun postLoginPass(
        @Field ("username") username: String,
        @Field ("password") password: String
    ) : Response<ResponseBody>
}

