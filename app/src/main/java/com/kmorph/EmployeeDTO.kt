package com.kmorph

import java.math.BigInteger

class EmployeeDTO {
    var lastName: String= ""
    var firstName: String= ""
    var idNumber: BigInteger= BigInteger("-1")
    var phone: String= ""
    var email: String= ""
    var jobTitle: String= ""
    var departmentName: String= ""
    var supervisor: String= ""
    var workLocation: String= ""
    var employeeType: Char = 0.toChar()
    var startDate: String= ""
    var endDate: String= ""
    var documentLink: String= ""
    var active: Boolean= false
}

