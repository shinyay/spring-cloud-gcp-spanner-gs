package com.google.shinyay.entity

import org.springframework.data.annotation.Id

data class Employee(@Id val id: Long,
                    val name: String,
                    val role: String,
                    val department_id: Long)