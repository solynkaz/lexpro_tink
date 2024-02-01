package com.example.lexpro_mobile.jsonDTO.responseJSON.rkkFilterJSON

data class User (

	val id : Int,
	val lastname : String,
	val firstname : String,
	val patronymic : String,
	val login : String,
	val password : String,
	val timeCreate : String,
	val isDeleted : String,
	val fullName : String
)