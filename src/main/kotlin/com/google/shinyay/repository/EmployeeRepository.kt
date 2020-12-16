package com.google.shinyay.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EmployeeRepository(val jdbcTemplate: JdbcTemplate) {

    fun getEmployees() = jdbcTemplate.queryForList("SELECT * FROM employee")
            .map { m -> m.values.toString() }
            .toList()
}