package com.example.lexpro_mobile.jsonDTO.responseJSON.Attachment

data class User(val firstname: String = "",
                val patronymic: String = "",
                val reu: Reu,
                val isDeleted: Boolean = false,
                val newPassword: String = "",
                val id: Int = 0,
                val login: String = "",
                val userRoleList: List<UserRoleListItem>?,
                val lastname: String = "")