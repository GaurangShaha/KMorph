package com.kmorph

fun EmployeeDAO.morphToEmployeeDTOManual(): EmployeeDTO {
    val target = EmployeeDTO()
    target.lastName = lastName
    target.firstName = firstName
    target.idNumber = idNumber
    target.phone = phone
    target.email = email
    target.jobTitle = jobTitle
    target.departmentName = DepartmentIdToNameTransformer().transform(departmentId)
    target.supervisor = supervisor
    target.workLocation = location
    target.employeeType = employeeType.toChar()
    target.startDate = MillisToDateStringTransformer().reverseTransform(startDate)
    target.endDate = MillisToDateStringTransformer().reverseTransform(endDate)
    target.documentLink = documentLink
    target.active = active
    return target
}


//fun StudentDAO.morphToStudentDTO(): StudentDTO {
//    return StudentDTO(lastName, firstName, idNumber, phone, email, jobTitle, DepartmentIdToNameTransformer().transform(departmentId), supervisor, location, employeeType, MillisToDateStringTransformer().reverseTransform(startDate), MillisToDateStringTransformer().reverseTransform(endDate), documentLink, active!!)
//}