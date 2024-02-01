package com.example.lexpro_mobile.viewmodel

import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.RkkData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON.rkkFilterJSON
import androidx.lifecycle.ViewModel
import com.example.lexpro_mobile.apiRepository.ApiRepos
import com.example.lexpro_mobile.jsonDTO.RkkFilter
import com.example.lexpro_mobile.jsonDTO.RkkFilterAttachment
import com.example.lexpro_mobile.jsonDTO.RkkFilterMailingList
import com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment.AttachmentList
import com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList.MailingList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


sealed class RkkFilterEvents {
    data class RkkFilterEvent(val body: RkkFilter) : RkkFilterEvents()
    data class RkkAttachmentEvent(val body: RkkFilterAttachment) : RkkFilterEvents()
    data class RkkMailingEvent(val body: RkkFilterMailingList) : RkkFilterEvents()
}


data class RkkFilterState(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val rkkFilterList: rkkFilterJSON? = null,
    var pickedCard: RkkData? = null,
)

data class RkkAttachmentState(
    val isAttachmentLoading: Boolean = false,
    val isAttachmentLoaded: Boolean = false,
    val rkkID: Int? = null,
    val rkkAttachmentList: List<AttachmentList>? = null
)

data class RkkMailingList(
    val isMailingLoading: Boolean = false,
    val isMailingLoaded: Boolean = false,
    val rkkID: Int? = null,
    val rkkMailingList: MailingList? = null
)
@HiltViewModel
class RkkFilterViewModel @Inject constructor(private val repos: ApiRepos) : ViewModel() {
    var state by mutableStateOf(RkkFilterState())
        private set
    var attachmentListState by mutableStateOf(RkkAttachmentState())
        private set
    var mailingListState by mutableStateOf(RkkMailingList())
        private set

    fun onEvent(event: RkkFilterEvents) {
        var code = 0
        state = state.copy(isLoading = true)
        when (event) {
            is RkkFilterEvents.RkkFilterEvent -> {
                rkkFilter(event.body)
            }
            is RkkFilterEvents.RkkAttachmentEvent -> {
                rkkFilterAttachment(event.body)
            }
            is RkkFilterEvents.RkkMailingEvent -> {
                rkkFilterMailingList(event.body)
            }
            else -> {}
        }
    }

    fun rkkFilterAttachment(body: RkkFilterAttachment) {
        var attachmentList: List<AttachmentList>? = null

        viewModelScope.launch(Dispatchers.IO) {
            attachmentList = repos.rkkGetAttachment(body)
            attachmentListState = attachmentListState.copy(
                rkkID = body.id_doc_rkk,
                rkkAttachmentList = attachmentList,
                isAttachmentLoaded = true,
                isAttachmentLoading = false
            )
        }
    }

    fun rkkFilterMailingList(body: RkkFilterMailingList) {
        var mailingList: MailingList? = null
        viewModelScope.launch(Dispatchers.IO) {
            mailingList = repos.rkkGetMailingList(body)
            mailingListState = mailingListState.copy(
                rkkID = body.id_doc_rkk,
                rkkMailingList = mailingList,
                isMailingLoaded = true,
                isMailingLoading = false
            )
        }
    }

    fun rkkFilter(body: RkkFilter) {
        var list: rkkFilterJSON? = null
        state = state.copy(isLoading = true, isLoaded = false)
        viewModelScope.launch(Dispatchers.IO) {
            list = repos.rkkFilter(body)
            state = state.copy(rkkFilterList = list, isLoading = false, isLoaded = true)
        }
    }
}
