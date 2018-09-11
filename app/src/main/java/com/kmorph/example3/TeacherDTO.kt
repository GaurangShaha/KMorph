package com.kmorph.example3

import java.math.BigInteger

class TeacherDTO {
    var lastName: String = ""
    var firstName: String = ""
    var idNumber: BigInteger = BigInteger("1")
    var phone: String = ""
    var email: String = ""
    var jobTitle: String = ""
    var departmentName: String = ""
    var supervisor: String = ""
    var workLocation: String = ""
    var employeeType: Float? = 1.toFloat()
    var startDate: String = ""
    var endDate: String = ""
    var documentLink: String = ""
    var active: Boolean = false
}

