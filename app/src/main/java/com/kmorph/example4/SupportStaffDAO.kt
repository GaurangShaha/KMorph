package com.kmorph.example4

import com.kmorph.DepartmentIdToNameTransformer
import com.kmorph.MillisToDateStringTransformer
import com.kmorph.annotation.FieldTransformer
import com.kmorph.annotation.MorphTo
import com.kmorph.annotation.MorphToField
import java.math.BigInteger

@MorphTo(SupportStaffDTO::class)
data class SupportStaffDAO(var firstName: String, var lastName: String, var idNumber: BigInteger, var phone: String, var email: String = "", var jobTitle: String, @MorphToField("departmentName") @FieldTransformer(DepartmentIdToNameTransformer::class) var departmentId: Int, var supervisor: String, @MorphToField("workLocation") var location: String, var employeeType: Int = 0, var active: Boolean, @FieldTransformer(MillisToDateStringTransformer::class) var startDate: Long, @FieldTransformer(MillisToDateStringTransformer::class) var endDate: Long, var documentLink: String)