package com.google.shinyay.controller;

import com.google.shinyay.entity.Employee
import com.google.shinyay.repository.EmployeeCustomRepository
import com.google.shinyay.repository.EmployeeRepository
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class EmployeeController(val repository: EmployeeRepository, val employeeCustomRepository: EmployeeCustomRepository) {

    @GetMapping("/employees")
    fun findAllEmployees(): MutableIterable<Employee> = repository.findAll()

    @GetMapping("/all")
    fun findAll(): MutableList<Employee>? = employeeCustomRepository.findEmployeeAll()

    @GetMapping("/employees/{id}")
    fun findEmployeeById(@PathVariable id: Long): Employee? {
        return repository.findById(id).orElseThrow()
    }

    @PostMapping("/employee")
    fun registerEmployee(@RequestBody employee: Employee): Employee {
        return repository.save(employee)
    }

    @PutMapping("/employees/{id}")
    fun updateEmployee(@RequestBody updateEmployee: Employee, @PathVariable id: Long): Employee? {
        return repository.findById(id)
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
