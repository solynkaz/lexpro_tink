package com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment

data class AttachmentList(
    val originalFileName: String? = "",
    val idOperator: Int? = 0,
    val pageCount: Int? = 0,
    val idType: Int? = 0,
    val signature: String? = "",
    val upload: String? = "",
    val hashSignature: String? = "",
    val conclusionDate: String? = "",
    val signingDate: String? = "",
    val idDocRkk: Int = 0,
    val type: Type?,
    val docDateAttachment: String? = "",
    val participant: Participant?,
    val operator: Operator?,
    val numberAttachment: String? = "",
    val idParticipant: Int? = 0,
    val certificateInformation: String? = "",
    val idGroup: Int? = 0,
    val isReplaceFile: Boolean? = false,
    val fileExtension: String? = "",
    val idFileSignature: Int? = 0,
    val id: Int = 0,
    val user: User?,
    val group: Group?
)