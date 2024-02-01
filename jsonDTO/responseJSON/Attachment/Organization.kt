package com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment

data class Organization(val path: String = "",
                        val organizationTypeDto: OrganizationTypeDto,
                        val isLawSubject: Boolean = false,
                        val name: String = "",
                        val id: Int = 0,
                        val email: String = "",
                        val idParent: Int = 0)