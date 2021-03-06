package com.google.shinyay.repository

import com.google.cloud.Timestamp
import com.google.cloud.spanner.Statement
import com.google.cloud.spring.data.spanner.core.SpannerPageableQueryOptions
import com.google.cloud.spring.data.spanner.core.SpannerQueryOptions
import com.google.cloud.spring.data.spanner.core.SpannerTemplate
import com.google.shinyay.entity.Employee
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam

@Component
class EmployeeCustomRepository(val spannerTemplate: SpannerTemplate) {

    fun findEmployeeAllwithSort(sort: String?): MutableList<Employee>? {
        return spannerTemplate.queryAll(Employee::class.java,
                SpannerPageableQueryOptions().setSort(Sort.by(sort)))
    }

    fun findEmployeeById(id: Long): MutableList<Employee>? {
        return spannerTemplate.query(Employee::class.java,
                Statement.of("SELECT * FROM employee WHERE employee_id = $id"),
                SpannerQueryOptions().setTimestamp(Timestamp.now()))
    }

//    fun findEmployeeAllWithLimit(limit: Int): MutableList<Employee>? {
//        return spannerTemplate.queryAll(Employee::class.java,
//                SpannerPageableQueryOptions().setLimit(limit))
//    }

    fun findEmployeeAllWithLimitAndOffset(limit: Int, offset: Long?): MutableList<Employee>? {
        return spannerTemplate.queryAll(Employee::class.java,
                SpannerPageableQueryOptions().setLimit(limit).setOffset(offset))
    }

    fun readEmployees(): MutableList<Employee>? {
        return spannerTemplate.readAll(Employee::class.java)
    }
}