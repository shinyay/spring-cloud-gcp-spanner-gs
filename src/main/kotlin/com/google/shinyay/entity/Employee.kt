package com.google.shinyay.entity

import com.google.cloud.spring.data.spanner.core.mapping.Column
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey
import com.google.cloud.spring.data.spanner.core.mapping.Table

@Table(name = "employee")
data class Employee(@PrimaryKey(keyOrder = 1)
                    @Column(name="employee_id")
                    var id: Long,
                    @Column(name = "employee_name")
                    var name: String?,
                    var role: String?,
                    val department_id: Long?)