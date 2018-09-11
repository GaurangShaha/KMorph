package com.kmorph.example1


import com.kmorph.DepartmentIdToNameTransformer
import com.kmorph.MillisToDateStringTransformer
import com.kmorph.annotation.FieldTransformer
import com.kmorph.annotation.MorphTo
import com.kmorph.annotation.MorphToField

import java.math.BigInteger

@MorphTo(EmployeeDTO::class)
class EmployeeDAO {
    var firstName: String = ""
    var lastName: String = ""
    var idNumber: BigInteger = BigInteger("-1")
    var phone: String = ""
    var email: String = ""
    var jobTitle: String = ""
    @MorphToField("departmentName")
    @FieldTransformer(DepartmentIdToNameTransformer::class)
    var departmentId: Int = 0
    var supervisor: String = ""
    @MorphToField("workLocation")
    var location: String = ""
    var employeeType: Int = 0
    var active: Boolean = false
    @FieldTransformer(MillisToDateStringTransformer::class)
    var startDate: Long = 0
    @FieldTransformer(MillisToDateStringTransformer::class)
    var endDate: Long = 0
    var documentLink: String = ""
}
