package com.google.shinyay.controller;

import com.google.shinyay.entity.Employee
import com.google.shinyay.repository.EmployeeRepository
import org.springframework.web.bind.annotation.*

@RestController
class EmployeeController(val repository: EmployeeRepository) {

    @GetMapping("/employees")
    fun findAllEmployees(): MutableIterable<Employee> = repository.findAll()

    @PostMapping("/employee")
    fun registerEmployee(@RequestBody employee: Employee) {
        repository.save(employee)
    }

    @PutMapping("/employees/{id}")
    fun updateEmployee() {
        
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long) {
        repository.deleteById(id)
    }
}
