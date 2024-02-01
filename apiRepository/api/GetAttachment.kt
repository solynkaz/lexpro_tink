package com.example.lexpro_mobile.apiRepository.api

import com.example.lexpro_mobile.jsonDTO.RkkFilterAttachment
import com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment.AttachmentList
import retrofit2.http.GET
import retrofit2.http.Path

interface GetAttachment {
    @GET("rkk/{id_doc_rkk}/doc_rkk_files")
    suspend fun getAttachment(
        @Path("id_doc_rkk") id: Int
    ): List<AttachmentList>
}