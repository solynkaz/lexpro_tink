package com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON

data class Session(val date: String? = "",
                   val number: String = "",
                   val timeCreate: String = "",
                   val convocation: Convocation,
                   val isDeleted: Boolean = false,
                   val id: Int? = 0)