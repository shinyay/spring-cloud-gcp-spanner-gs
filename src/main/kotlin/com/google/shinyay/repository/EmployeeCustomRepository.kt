package com.google.shinyay.repository

import com.google.cloud.Timestamp
import com.google.cloud.spanner.Statement
import com.google.cloud.spring.data.spanner.core.SpannerQueryOptions
import com.google.cloud.spring.data.spanner.core.SpannerTemplate
import com.google.shinyay.entity.Employee
import org.springframework.stereotype.Component

@Component
class EmployeeCustomRepository(val spannerTemplate: SpannerTemplate) {

    fun findEmployeeAll(): MutableList<Employee>? {
        val spannerQueryOptions = SpannerQueryOptions().setTimestamp(Timestamp.now())
        return spannerTemplate.query(Employee::class.java, Statement.of("SELECT * FROM employee"), spannerQueryOptions)
    }
}