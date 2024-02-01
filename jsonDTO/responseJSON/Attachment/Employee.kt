package com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment

data class Employee(val firstname: String = "",
                    val patronymic: String = "",
                    val isDeleted: Boolean = false,
                    val isLawSubject: Boolean = false,
                    val name: String = "",
                    val roe: Roe,
                    val id: Int = 0,
                    val email: String = "",
                    val lastname: String = "")