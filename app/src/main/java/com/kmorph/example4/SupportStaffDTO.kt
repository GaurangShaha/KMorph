package com.kmorph.example4

import java.math.BigInteger

data class SupportStaffDTO(var lastName: String, var firstName: String, var idNumber: BigInteger, var phone: String, var jobTitle: String, var departmentName: String, var supervisor: String, var workLocation: String, var employeeType: Int, var startDate: String, var endDate: String, var documentLink: String, var active: Boolean)

