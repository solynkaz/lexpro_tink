package com.example.lexpro_mobile.jsonDTO.responseJSON.MailingList

data class Roe(val organization: Organization,
               val position: Position,
               val employee: String = "",
               val employeeStatus: EmployeeStatus)