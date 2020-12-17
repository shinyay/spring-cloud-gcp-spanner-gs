package com.google.shinyay.controller;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController {

    @GetMapping("/employees")
    fun findAllEmployees() {}
}
