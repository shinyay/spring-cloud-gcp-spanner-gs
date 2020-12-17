package com.google.shinyay.repository

import com.google.cloud.spring.data.spanner.repository.SpannerRepository
import com.google.shinyay.entity.Employee
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : SpannerRepository<Employee, Long>