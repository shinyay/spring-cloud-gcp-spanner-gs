package com.google.shinyay.entity

import com.google.cloud.spring.data.spanner.core.mapping.Column
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey
import com.google.cloud.spring.data.spanner.core.mapping.Table

@Table(name = "employee")
data class Employee(@PrimaryKey(keyOrder = 1)
                    @Column(name="employee_id")
                    val id: Long,
                    @Column(name = "employee_name")
                    val name: String?,
                    val role: String?,
                    val department_id: Long?)