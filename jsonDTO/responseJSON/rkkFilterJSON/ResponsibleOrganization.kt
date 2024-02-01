package com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON

data class ResponsibleOrganization(val organizationType: OrganizationType,
                                   val path: String = "",
                                   val name: String = "",
                                   val fullName: String = "",
                                   val id: Int = 0,
                                   val email: String = "",
                                   val idParent: Int = 0)