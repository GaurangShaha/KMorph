package com.kmorph.example2

import java.math.BigInteger

data class StudentDTO(var lastName: String, var firstName: String, var idNumber: BigInteger, var phone: String, var email: String, var jobTitle: String, var departmentName: String, var supervisor: String, var workLocation: String, var employeeType: Int, var startDate: String, var endDate: String, var documentLink: String, var active: Boolean)

