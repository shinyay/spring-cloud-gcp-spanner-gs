package com.google.shinyay.entity

import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey
import com.google.cloud.spring.data.spanner.core.mapping.Table

@Table(name = "employee")
data class Employee(@PrimaryKey(keyOrder = 1)
                    val id: Long,
                    val name: String,
                    val role: String,
                    val department_id: Long)