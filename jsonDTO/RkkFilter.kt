package com.example.lexpro_mobile.jsonDTO

import javax.security.auth.Subject

data class RkkFilter(
    val size: String? = null,
    val page: String? = null,
    val codeStatus: List<String>? = listOf(),
    val visaType: String? = null,
    val idLawSubject: String? = null,
    val idResponsibleOrganization: String? = null,
    val idStatus: String? = null,
    val idSession: String? = null,
    val codeNpaType: String? = null,
    val codeStage: List<String?>? = null,
    val headSigned: String? = null,
    val chairmanSigned: String? = null,
    val published: String? = null,
    var isDeleted: String? = null, //bool
    val readyForSession: String? = null,//bool
    val isReadyToPublish: String? = null,//bool
    val textSearch: String? = null
)