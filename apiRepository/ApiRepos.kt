package com.example.lexpro_mobile.apiRepository

import android.util.Log
import com.example.lexpro_mobile.apiRepository.api.*
import com.example.lexpro_mobile.jsonDTO.RkkFilter
import com.example.lexpro_mobile.jsonDTO.RkkFilterAttachment
import com.example.lexpro_mobile.jsonDTO.RkkFilterMailingList
import com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment.AttachmentList
import com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList.Mailing
import com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList.MailingList
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.rkkFilterJSON
import com.example.lexpro_mobile.viewmodel.RkkAttachmentState


import javax.inject.Inject
class ApiRepos @Inject constructor(
    private val PostLoginJSON: PostLoginJSON,
    private val GetStageJSON: GetStageListJSON,
    private val filterRkkJSON: rkkFilter,
    private val getAttachment: GetAttachment,
    private val getMailingList: GetMailingList
) {

    suspend fun postApi(login: String, password: String): List<Any> {
        try {
            val originalResponse = PostLoginJSON.postLoginPass(login,password)
            return if (originalResponse.headers().values("Location")[0].equals("http://188.72.76.241:8050/lexpro/login?error=true")) {
                listOf(200, originalResponse.headers().values("Set-Cookie")[0].split(";")[0])
            } else {
                listOf(302,"NULL")
            }
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
        }
        return listOf("404","NULL")
    }


    suspend fun getStage() {
        try {
            var list = GetStageJSON.getStageList()
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
        }
    }

    suspend fun rkkFilter(body: RkkFilter): rkkFilterJSON? {
        return try {
            filterRkkJSON.filterRkk(body)
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
            null
        }
    }

    suspend fun rkkGetAttachment(body: RkkFilterAttachment) : List<AttachmentList>? {
        return try {
            getAttachment.getAttachment(body.id_doc_rkk)
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
            null
        }
    }
    suspend fun rkkGetMailingList(body: RkkFilterMailingList) : MailingList? {
        return try {
            getMailingList.getMailingList(body.id_doc_rkk)
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
            null
        }
    }


}