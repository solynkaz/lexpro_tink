package com.example.lexpro_mobile.auth

data class Token(
    val type:String,
    val access: String,
    val refresh: String,
)