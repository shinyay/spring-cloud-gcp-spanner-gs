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
    fun updateEmployee(@RequestBody updateEmployee: Employee, @PathVariable id: Long) {
        repository.findById(id)
                .map { employee ->
                    employee.name = updateEmployee.name
                    employee.role = updateEmployee.role
                    repository.save(employee)
                }.orElseGet {
                    updateEmployee.id = id
                    repository.save(updateEmployee)
                }
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long) {
        repository.deleteById(id)
    }
}
