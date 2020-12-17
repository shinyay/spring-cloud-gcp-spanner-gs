package com.google.shinyay.entity

import com.google.cloud.spring.data.spanner.core.mapping.Table
import org.springframework.data.annotation.Id

@Table(name = "employee")
data class Employee(@Id val id: Long,
                    val name: String,
                    val role: String,
                    val department_id: Long)