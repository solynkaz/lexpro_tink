package com.example.lexpro_mobile.apiRepository.api

import com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList.MailingList
import retrofit2.http.GET
import retrofit2.http.Path

interface GetMailingList {
    @GET("rkk/{id_doc_rkk}/mailing_list")
    suspend fun getMailingList(
        @Path("id_doc_rkk") id: Int
    ): MailingList?
}